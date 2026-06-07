package pl.least_squares_algorithms.data_series_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.core.DistanceX;
import pl.least_squares_algorithms.core.Distribution;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.WeightDistribution;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;
import pl.least_squares_algorithms.core.function.DomainFunction;
import pl.least_squares_algorithms.core.function.polynomials.Polynomial;
import pl.least_squares_algorithms.core.mapper.PolynomialMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

@Slf4j
@Service
public class DataSeriesGenerator {
    public static final double MEAN = 6.0;
    public static final double STANDARD_DEVIATION = 2.0;

    private final SecureRandom random;

    public DataSeriesGenerator() {
        this.random = new SecureRandom();
    }

    public byte[] generateDataSeries(DistanceX distanceX, WeightDistribution weightDistribution, MathematicalFunctionDto mathematicalFunctionDTO, int numberPoints, boolean noise) {
        StringBuilder stringBuilder = new StringBuilder();
        Polynomial polynomial = PolynomialMapper.mapToPolynomial(mathematicalFunctionDTO.getPolynomialDto(), false);
        log.debug("Generate polynomial {}", polynomial);

        List<PointXY> points = generatePoints(distanceX, weightDistribution, mathematicalFunctionDTO.getDomainFunction(), numberPoints, polynomial);
        if (noise) {
            addNoises(points);
        }

        stringBuilder.append("//")
                .append(polynomial)
                .append("\n");

        if (WeightDistribution.NONE.equals(weightDistribution)) {
            points.forEach(p ->
                    stringBuilder.append(p.toStringWithBeforeY())
                            .append("\n")
            );
        } else {
            points.forEach(p ->
                    stringBuilder.append(p.toStringWithWeight())
                            .append("\n")
            );
        }

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }

    private List<PointXY> generatePoints(DistanceX distanceX, WeightDistribution weightDistribution, DomainFunction domainFunction, int numberPoints, Polynomial polynomial) {
        List<PointXY> points = new ArrayList<>();
        List<BigDecimal> xs = getXs(distanceX, numberPoints, domainFunction);

        if (WeightDistribution.NONE.equals(weightDistribution)) {
            xs.stream().mapToDouble(BigDecimal::doubleValue)
                    .mapToObj(x -> new PointXY(x, polynomial.evaluate(x)))
                    .forEach(points::add);
        } else {
            xs.stream().mapToDouble(BigDecimal::doubleValue)
                    .mapToObj(x -> new PointXY(x, polynomial.evaluate(x), weightDistribution.applyValue(MEAN, STANDARD_DEVIATION)))
                    .forEach(points::add);
        }
        Collections.sort(points);
        return points;
    }

    private void addNoises(List<PointXY> points) {
        int b = points.size() / 20 + 1;
        Set<Integer> x = new HashSet<>();

        while (x.size() < b) {
            x.add(random.nextInt(points.size()));
        }

        x.stream().map(points::get).forEach(p -> p.addY((random.nextDouble() - 0.5) * p.getY() * 0.125));
    }

    private List<BigDecimal> getXs(DistanceX distanceX, int numberPoints, DomainFunction domainFunction) {
        BigDecimal beginningInterval = BigDecimal.valueOf(domainFunction.beginningInterval());
        BigDecimal endInterval = BigDecimal.valueOf(domainFunction.endInterval());
        BigDecimal difference = endInterval.subtract(beginningInterval)
                .setScale(20, RoundingMode.HALF_UP);
        if (DistanceX.NORMAL.equals(distanceX)) {
            BigDecimal x;
            double mean = (endInterval.doubleValue() + beginningInterval.doubleValue()) / 2;
            double standardDeviation = difference.doubleValue() / 6;
            Set<BigDecimal> xs = new HashSet<>();
            while (xs.size() < numberPoints) {
                x = BigDecimal.valueOf(Distribution.normal(mean, standardDeviation));
                xs.add(x);
            }
            return new ArrayList<>(xs);
        } else if (DistanceX.CHEBYSHEV.equals(distanceX)) {
            List<BigDecimal> xs = new ArrayList<>();
            BigDecimal a = beginningInterval.subtract(endInterval).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
            BigDecimal b = beginningInterval.add(endInterval).divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);
            for (int i = 0; i < numberPoints; i++) {
                xs.add(getZeroChebyshevPolynomial(i, numberPoints));
            }
            xs = xs.stream().map(x -> x.multiply(a).add(b))
                    .collect(Collectors.toList());
            return xs;
        } else {
            BigDecimal step = difference.divide(BigDecimal.valueOf((double) numberPoints - 1), RoundingMode.HALF_UP);
            if (step.doubleValue() <= 0.0) {
                throw new ArithmeticException("Step less than or equal to 0");
            }
            return IntStream.range(0, numberPoints)
                    .mapToObj(i -> beginningInterval.add(step.multiply(new BigDecimal(String.valueOf(i)))))
                    .collect(Collectors.toList());
        }
    }

    private BigDecimal getZeroChebyshevPolynomial(int i, int n) {
        return BigDecimal.valueOf(cos(((2.0 * i + 1) / (2.0 * n)) * PI));
    }
}
