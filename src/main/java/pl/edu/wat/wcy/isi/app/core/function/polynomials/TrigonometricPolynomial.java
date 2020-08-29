package pl.edu.wat.wcy.isi.app.core.function.polynomials;

import java.util.List;

public class TrigonometricPolynomial extends Polynomial {
    public static int chooseTrigonometricMaxDegree(int size) {
        if (size % 2 == 0) {
            return size / 2;
        } else {
            return (size - 1) / 2;
        }
    }

    public TrigonometricPolynomial(List<Double> coefficients) {
        super(coefficients);
        setDegree(chooseTrigonometricMaxDegree(coefficients.size()));
    }

    @Override
    public double evaluate(double x) {
        double result = 0.0;
        List<Double> coefficients = this.getCoefficients();

        if (!coefficients.isEmpty()) {
            result += 0.5 * coefficients.get(0);
            for (int i = 1; i < coefficients.size() / 2.0; i++) {
                result += coefficients.get(2 * i - 1) * Math.cos(i * x);
                result += coefficients.get(2 * i) * Math.sin(i * x);
            }
            if (coefficients.size() % 2 == 0) {
                int sizeCoefficient = coefficients.size();
                result += 0.5 * coefficients.get(sizeCoefficient - 1) * Math.cos((sizeCoefficient / 2.0) * x);
            }
        }
        return result;
    }

    public double evaluate(double x, AlgebraicPolynomial linearFunction) {
        if (isNotEmpty(linearFunction)) {
            x = linearFunction.evaluate(x);
        }
        return evaluate(x);
    }

    public TrigonometricPolynomial plus(TrigonometricPolynomial trigonometricPolynomial) {
        return super.plus(trigonometricPolynomial, TrigonometricPolynomial.class);
    }

    public TrigonometricPolynomial plus(double value) {
        return super.plus(value, TrigonometricPolynomial.class);
    }

    public TrigonometricPolynomial minus(TrigonometricPolynomial trigonometricPolynomial) {
        return super.minus(trigonometricPolynomial, TrigonometricPolynomial.class);
    }

    public TrigonometricPolynomial minus(double value) {
        return super.minus(value, TrigonometricPolynomial.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TrigonometricPolynomial) {
            return obj == this || (this.getDegree() == ((TrigonometricPolynomial) obj).getDegree() && this.getCoefficients().equals(((TrigonometricPolynomial) obj).getCoefficients()));
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        List<Double> coefficients = this.getCoefficients();
        StringBuilder stringBuilder = new StringBuilder("TrigonometricPolynomial: ");

        if (!coefficients.isEmpty()) {
            stringBuilder.append(coefficients.get(0)).append("*").append(0.5);
            for (int i = 1; i < coefficients.size() / 2.0; i++) {
                stringBuilder.append(" + ").append(coefficients.get(2 * i - 1)).append("*cos(").append(i).append("x").append(")");
                stringBuilder.append(" + ").append(coefficients.get(2 * i)).append("*sin(").append(i).append("x").append(")");
            }
            if (coefficients.size() % 2 == 0) {
                int sizeCoefficient = coefficients.size();
                stringBuilder.append(" + ").append(coefficients.get(sizeCoefficient - 1) / 2).append("*cos(").append(sizeCoefficient / 2).append("x").append(")");
            }
            return stringBuilder.toString();
        }
        return "TrigonometricPolynomial is empty";
    }
}
