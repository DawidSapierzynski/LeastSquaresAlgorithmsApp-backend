package pl.edu.wat.wcy.isi.autoapproximationappbackend.core.approximation;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;
import pl.edu.wat.wcy.isi.app.core.approximation.PolynomialApproximation;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.AlgebraicPolynomial;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.ArrayList;
import java.util.List;

class PolynomialApproximationTest {
    private static final Logger logger = LoggerFactory.getLogger(PolynomialApproximationTest.class);

    private List<PointXY> points = new ArrayList<>();
    private PolynomialApproximation polynomialApproximation;
    private AlgebraicPolynomial function;

    @BeforeEach
    void setUp() {
        function = new AlgebraicPolynomial(List.of(5.6d, -23.2d, -6.1d, 0d, 1d, 2.8612d));

        double[] xs = {0.2, 1.56, 2, 2.2, 3.2, 4, 5.6, 6.25, 7.45, 8.23, 9.5, 10.65, 11.54, 12.2, 13.78, 15.632, 20, 63.16};
        for (double x : xs) {
            points.add(new PointXY(x, getFunctionValue(x)));
        }

        polynomialApproximation = new PolynomialApproximation(points, 5);
    }

    @AfterEach
    void tearDown() {
        points = null;
        polynomialApproximation = null;
    }

    @Test
    void doApproximations() {
        Polynomial resultPolynomial;
        double r, cr;

        resultPolynomial = polynomialApproximation.doApproximations(LeastSquaresMethod.NORMALIZATION).get(0).getPolynomial();

        logger.info("{}", function);
        logger.info("{}", resultPolynomial);

        for (PointXY p : points) {
            cr = p.getY();
            r = resultPolynomial.evaluate(p.getX());

            logger.info("Y={} ; YPolynomial={} ; d={}", cr, r, Math.abs(cr - r));
        }
    }

    private double getFunctionValue(double x) {
        return function.evaluate(x);
    }
}