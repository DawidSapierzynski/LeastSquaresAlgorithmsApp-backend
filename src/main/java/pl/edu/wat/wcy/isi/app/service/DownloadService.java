package pl.edu.wat.wcy.isi.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.core.WeightDistribution;
import pl.edu.wat.wcy.isi.app.core.function.DomainFunction;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;
import pl.edu.wat.wcy.isi.app.dto.MathematicalFunctionDTO;
import pl.edu.wat.wcy.isi.app.mapper.PolynomialMapper;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

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

    public byte[] generateDataSeries(WeightDistribution weightDistribution, MathematicalFunctionDTO mathematicalFunctionDTO, int numberPoints) {
        StringBuilder stringBuilder = new StringBuilder();
        Polynomial polynomial = polynomialMapper.mapToPolynomial(mathematicalFunctionDTO.getPolynomialDTO(), false);
        logger.debug("Generate polynomial {}", polynomial);

        List<PointXY> points = generatePoints(weightDistribution, mathematicalFunctionDTO.getDomainFunction(), numberPoints, polynomial);

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

    private List<PointXY> generatePoints(WeightDistribution weightDistribution, DomainFunction domainFunction, int numberPoints, Polynomial polynomial) {
        List<PointXY> points = new ArrayList<>();
        BigDecimal x = BigDecimal.valueOf(domainFunction.getBeginningInterval());

        BigDecimal step = BigDecimal.valueOf(domainFunction.getEndInterval() - domainFunction.getBeginningInterval()).setScale(20, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf((double) numberPoints - 1), RoundingMode.HALF_UP);

        if (step.doubleValue() <= 0.0) {
            throw new ArithmeticException("Step less than or equal to 0");
        }

        if (WeightDistribution.NONE.equals(weightDistribution)) {
            for (int i = 0; i < numberPoints; i++) {
                points.add(new PointXY(x.doubleValue(), polynomial.evaluate(x.doubleValue())));
                x = x.add(step);
            }
        } else {
            for (int i = 0; i < numberPoints; i++) {
                points.add(new PointXY(x.doubleValue(),
                        polynomial.evaluate(x.doubleValue()),
                        weightDistribution.applyValue(MEAN, STANDARD_DEVIATION)));
                x = x.add(step);
            }
        }


        return points;
    }
}
