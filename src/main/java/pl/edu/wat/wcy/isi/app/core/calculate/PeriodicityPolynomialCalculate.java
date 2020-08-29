package pl.edu.wat.wcy.isi.app.core.calculate;

import lombok.extern.slf4j.Slf4j;
import pl.edu.wat.wcy.isi.app.core.approximation.PolynomialApproximation;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;

import static java.lang.Math.*;

@Slf4j
public class PeriodicityPolynomialCalculate implements Runnable {
    private static final int MIN_DEGREE = 4;
    private final int degree;
    private final DataSeriesFileEntity dataSeriesFile;

    public PeriodicityPolynomialCalculate(DataSeriesFileEntity dataSeriesFile) {
        this.dataSeriesFile = dataSeriesFile;
        this.degree = calculateDegree(dataSeriesFile.getSize());
    }

    @Override
    public void run() {
        double errorPolynomial;

        log.debug("PeriodicityPolynomialCalculate degree: {}", this.degree);
        PolynomialApproximation polynomialApproximation = new PolynomialApproximation(dataSeriesFile.getPoints(), degree);

        polynomialApproximation.doApproximations();

        errorPolynomial = polynomialApproximation.calculateError();
        log.debug("Calculated errorPolynomial: {}", errorPolynomial);

        dataSeriesFile.setErrorPolynomial(errorPolynomial);
        log.info("Set errorPolynomial: {}", errorPolynomial);
    }

    public static int calculateDegree(int size) {
        int d = (int) ceil(log(pow(size, 3)));
        int maxDegree = size - 1;

        if (d < MIN_DEGREE) {
            d = MIN_DEGREE;
        } else if (d > maxDegree) {
            d = maxDegree;
        }

        return d;
    }
}
