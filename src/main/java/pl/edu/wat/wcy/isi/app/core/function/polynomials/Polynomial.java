package pl.edu.wat.wcy.isi.app.core.function.polynomials;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Polynomial {
    private final Logger logger = LoggerFactory.getLogger(Polynomial.class);

    /**
     * coefficients of polynomial
     */
    private final List<Double> coefficients;

    /**
     * degree of polynomial
     */
    private int degree;

    public static boolean isEmpty(Polynomial polynomial) {
        return polynomial == null || polynomial.getCoefficients() == null || polynomial.getCoefficients().size() == 0;
    }

    public static boolean isNotEmpty(Polynomial polynomial) {
        return !isEmpty(polynomial);
    }

    public Polynomial(List<Double> coefficients) {
        this.coefficients = coefficients;
        this.reduce();
    }

    private void reduce() {
        for (int i = coefficients.size() - 1; i > 0; i--) {
            if (coefficients.get(i) != 0) {
                this.degree = i;
                return;
            } else {
                coefficients.remove(i);
            }
        }
    }

    protected <T extends Polynomial> T plus(T polynomial, Class<T> polynomialClass) {
        List<Double> result;
        List<Double> coefficients = this.getCoefficients();
        int sizeThis = this.getCoefficients().size() - 1;
        int sizeThat = polynomial.getCoefficients().size() - 1;

        if (isNotEmpty(polynomial) && coefficients != null) {
            List<Double> maxCoefficients = polynomial.getDegree() > getDegree() ? polynomial.getCoefficients() : this.getCoefficients();
            int minSize = Math.min(sizeThis, sizeThat);
            int maxSize = Math.max(sizeThis, sizeThat);
            result = new ArrayList<>();

            for (int i = 0; i <= minSize; i++) {
                result.add(coefficients.get(i) + polynomial.getCoefficients().get(i));
            }
            for (int i = minSize + 1; i <= maxSize; i++) {
                result.add(maxCoefficients.get(i));
            }

            try {
                return polynomialClass.getDeclaredConstructor(List.class).newInstance(result);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("{}", e.getMessage());
            }
        } else {
            logger.error("Polynomial or coefficients is empty.");
        }
        return null;
    }

    protected <T extends Polynomial> T minus(T polynomial, Class<T> polynomialClass) {
        List<Double> result;
        List<Double> coefficients = this.getCoefficients();
        int sizeThis = this.getCoefficients().size() - 1;
        int sizeThat = polynomial.getCoefficients().size() - 1;

        if (isNotEmpty(polynomial) && coefficients != null) {
            int minSize = Math.min(sizeThis, sizeThat);
            result = new ArrayList<>();

            for (int i = 0; i <= minSize; i++) {
                result.add(coefficients.get(i) - polynomial.getCoefficients().get(i));
            }

            if (sizeThat > minSize) {
                for (int i = minSize + 1; i <= sizeThat; i++) {
                    result.add(-polynomial.getCoefficients().get(i));
                }
            } else {
                for (int i = minSize + 1; i <= sizeThis; i++) {
                    result.add(this.getCoefficients().get(i));
                }
            }

            try {
                return polynomialClass.getDeclaredConstructor(List.class).newInstance(result);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                logger.error("{}", e.getMessage(), e);
            }
        } else {
            logger.error("Polynomial or coefficients is empty.");
        }
        return null;
    }

    protected <T extends Polynomial> T plus(double value, Class<T> polynomialClass) {
        List<Double> result = new ArrayList<>();
        List<Double> coefficients = getCoefficients();

        result.add(coefficients.get(0) + value);

        for (int i = 1; i <= getDegree(); i++) {
            result.add(coefficients.get(i));
        }

        try {
            return polynomialClass.getDeclaredConstructor(List.class).newInstance(result);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return null;
    }

    protected <T extends Polynomial> T minus(double value, Class<T> polynomialClass) {
        return plus(-value, polynomialClass);
    }

    public int getDegree() {
        return degree;
    }

    protected void setDegree(int degree) {
        this.degree = degree;
    }

    public List<Double> getCoefficients() {
        return coefficients;
    }

    public abstract double evaluate(double x);
}
