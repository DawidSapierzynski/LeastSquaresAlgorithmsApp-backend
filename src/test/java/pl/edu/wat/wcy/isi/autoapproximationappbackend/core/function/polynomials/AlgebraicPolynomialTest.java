package pl.edu.wat.wcy.isi.autoapproximationappbackend.core.function.polynomials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.AlgebraicPolynomial;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgebraicPolynomialTest {
    private AlgebraicPolynomial algebraicPolynomial1;
    private AlgebraicPolynomial algebraicPolynomial2;

    @BeforeEach
    void setUp() {
        List<Double> coefficients1 = List.of(-1d, 5d, 4d, 8d);
        List<Double> coefficients2 = List.of(3d, 0d, 0d, 8d);
        algebraicPolynomial1 = new AlgebraicPolynomial(coefficients1);
        algebraicPolynomial2 = new AlgebraicPolynomial(coefficients2);
    }

    @AfterEach
    void tearDown() {
        algebraicPolynomial1 = null;
        algebraicPolynomial2 = null;
    }

    @Test
    void mapCoefficients() {
        double[] doubles = new double[]{25.0, 152.5, 236985.25};
        List<Double> correctResult = List.of(25.0, 152.5, 236985.25);

        List<Double> result = AlgebraicPolynomial.mapCoefficients(doubles);

        assertEquals(correctResult, result);
    }

    @Test
    void plus() {
        List<Double> coefficientsResult = List.of(2d, 5d, 4d, 16d);
        AlgebraicPolynomial correctResult = new AlgebraicPolynomial(coefficientsResult);

        AlgebraicPolynomial result = algebraicPolynomial1.plus(algebraicPolynomial2);

        assertEquals(correctResult, result);
    }

    @Test
    void minus() {
        List<Double> coefficientsResult = List.of(-4d, 5d, 4d);
        AlgebraicPolynomial correctResult = new AlgebraicPolynomial(coefficientsResult);

        AlgebraicPolynomial result = algebraicPolynomial1.minus(algebraicPolynomial2);

        assertEquals(correctResult, result);
    }

    @Test
    void times() {
        List<Double> coefficientsResult1 = List.of(-3d, 15d, 12d, 16d, 40d, 32d, 64d);
        List<Double> coefficientsResult2 = List.of(-1.5, 7.5, 6d, 12d);
        AlgebraicPolynomial correctResult1 = new AlgebraicPolynomial(coefficientsResult1);
        AlgebraicPolynomial correctResult2 = new AlgebraicPolynomial(coefficientsResult2);

        AlgebraicPolynomial result1 = algebraicPolynomial1.times(algebraicPolynomial2);
        AlgebraicPolynomial result2 = algebraicPolynomial1.times(1.5);

        assertEquals(correctResult1, result1);
        assertEquals(correctResult2, result2);
    }

    @Test
    void evaluate() {
        assertEquals(128.0, algebraicPolynomial2.evaluate(2.5));
    }
}