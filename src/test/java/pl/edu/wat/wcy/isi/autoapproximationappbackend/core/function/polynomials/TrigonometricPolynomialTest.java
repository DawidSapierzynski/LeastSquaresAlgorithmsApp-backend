package pl.edu.wat.wcy.isi.autoapproximationappbackend.core.function.polynomials;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.TrigonometricPolynomial;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrigonometricPolynomialTest {
    private TrigonometricPolynomial trigonometricPolynomial1;
    private TrigonometricPolynomial trigonometricPolynomial2;

    @BeforeEach
    void setUp() {
        List<Double> coefficients1 = List.of(5.5, 3.25, 10d, 2d, -4d);
        List<Double> coefficients2 = List.of(3d, 8.75, 3d, -11d);
        trigonometricPolynomial1 = new TrigonometricPolynomial(coefficients1);
        trigonometricPolynomial2 = new TrigonometricPolynomial(coefficients2);
    }

    @AfterEach
    void tearDown() {
        trigonometricPolynomial1 = null;
        trigonometricPolynomial2 = null;
    }

    @Test
    void plus() {
        List<Double> coefficientsResult = List.of(8.5, 12d, 13d, -9d, -4d);
        TrigonometricPolynomial correctResult = new TrigonometricPolynomial(coefficientsResult);

        TrigonometricPolynomial result = trigonometricPolynomial1.plus(trigonometricPolynomial2);

        assertEquals(correctResult, result);
    }

    @Test
    void minus() {
        List<Double> coefficientsResult = List.of(2.5, -5.5, 7d, 13d, -4d);
        TrigonometricPolynomial correctResult = new TrigonometricPolynomial(coefficientsResult);

        TrigonometricPolynomial result = trigonometricPolynomial1.minus(trigonometricPolynomial2);

        assertEquals(correctResult, result);
    }

    @Test
    void evaluate() {
        assertEquals(10.534026160091036, trigonometricPolynomial1.evaluate(2.5));
        assertEquals(-5.2747322237715455, trigonometricPolynomial2.evaluate(2.5));
    }
}