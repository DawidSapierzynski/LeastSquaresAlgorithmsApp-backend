package pl.edu.wat.wcy.isi.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.core.DistanceX;
import pl.edu.wat.wcy.isi.app.core.Distribution;
import pl.edu.wat.wcy.isi.app.core.WeightDistribution;
import pl.edu.wat.wcy.isi.app.core.function.DomainFunction;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;
import pl.edu.wat.wcy.isi.app.dto.MathematicalFunctionDTO;
import pl.edu.wat.wcy.isi.app.mapper.PolynomialMapper;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.util.stream.IntStream.range;

@Service
public class DownloadService {
    private static final Logger logger = LoggerFactory.getLogger(DownloadService.class);
    public static final double MEAN = 6.0;
    public static final double STANDARD_DEVIATION = 2.0;

    private final PolynomialMapper polynomialMapper;

    public DownloadService(PolynomialMapper polynomialMapper) {
        this.polynomialMapper = polynomialMapper;
    }

    public byte[] getApproximationResult(List<MathematicalFunctionDTO> mathematicalFunctionDTOs) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Double> coefficients;

        for (MathematicalFunctionDTO m : mathematicalFunctionDTOs) {
            stringBuilder.append(m.getDomainFunction()).append("\n");
            coefficients = m.getPolynomialDTO().getCoefficients();
            for (int i = 0; i < coefficients.size(); i++) {
                stringBuilder.append("a")
                        .append(i)
                        .append("=")
                        .append(coefficients.get(i))
                        .append("\n");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString().getBytes();
    }

    public byte[] generateDataSeries(DistanceX distanceX, WeightDistribution weightDistribution, MathematicalFunctionDTO mathematicalFunctionDTO, int numberPoints) {
        StringBuilder stringBuilder = new StringBuilder();
        Polynomial polynomial = polynomialMapper.mapToPolynomial(mathematicalFunctionDTO.getPolynomialDTO(), false);
        logger.debug("Generate polynomial {}", polynomial);

        List<PointXY> points = generatePoints(distanceX, weightDistribution, mathematicalFunctionDTO.getDomainFunction(), numberPoints, polynomial);

        stringBuilder.append("//")
                .append(polynomial)
                .append("\n");

        if (WeightDistribution.NONE.equals(weightDistribution)) {
            points.forEach(p ->
                    stringBuilder.append(p.getX())
                            .append(";")
                            .append(p.getY())
                            .append("\n")
            );
        } else {
            points.forEach(p ->
                    stringBuilder.append(p.getX())
                            .append(";")
                            .append(p.getY())
                            .append(";")
                            .append(p.getWeight())
                            .append("\n")
            );
        }

        return stringBuilder.toString().getBytes();
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

        return points;
    }

    private List<BigDecimal> getXs(DistanceX distanceX, int numberPoints, DomainFunction domainFunction) {
        BigDecimal beginningInterval = BigDecimal.valueOf(domainFunction.getBeginningInterval());
        BigDecimal endInterval = BigDecimal.valueOf(domainFunction.getEndInterval());
        BigDecimal difference = endInterval.subtract(beginningInterval)
                .setScale(20, RoundingMode.HALF_UP);
        if (DistanceX.NORMAL.equals(distanceX)) {
            BigDecimal x;
            double mean = (endInterval.doubleValue() - beginningInterval.doubleValue()) / 2;
            double standardDeviation = difference.doubleValue() / 5.3;
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
            return range(0, numberPoints)
                    .mapToObj(i -> beginningInterval.add(step.multiply(new BigDecimal(String.valueOf(i)))))
                    .collect(Collectors.toList());
        }
    }

    private BigDecimal getZeroChebyshevPolynomial(int i, int n) {
        return BigDecimal.valueOf(cos(((2.0 * i + 1) / (2.0 * n)) * PI));
    }
}
