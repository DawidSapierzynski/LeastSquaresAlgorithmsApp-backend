package pl.edu.wat.wcy.isi.app.core.approximation;

import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;
import pl.edu.wat.wcy.isi.app.core.function.MathematicalFunction;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.ArrayList;
import java.util.List;

public abstract class Approximation {
    private List<MathematicalFunction> mathematicalFunctions;
    private final List<PointXY> points;
    private int degree;
    private final int size;
    protected double absoluteError;
    protected double rSquared;

    public Approximation(List<PointXY> points, int degree) {
        this.points = points;
        this.size = points.size();
        this.degree = degree;
    }

    public abstract List<MathematicalFunction> doApproximations(LeastSquaresMethod leastSquaresMethod);

    public List<PointXY> getApproximationsPoints(int approximationsPointsSize) {
        List<PointXY> approximationsPoints = new ArrayList<>();
        double x0 = points.get(0).getX();
        double xn = points.get(size - 1).getX();
        double x = x0;

        double step = (xn - x0) / (approximationsPointsSize - 1);

        for (int i = 0; i < approximationsPointsSize; i++) {
            approximationsPoints.add(new PointXY(x, getPolynomial().evaluate(x)));
            x += step;
        }

        return approximationsPoints;
    }

    public double calculateError(LeastSquaresMethod leastSquaresMethod) {
        if (getPolynomial() == null) {
            doApproximations(leastSquaresMethod);
        }

        this.absoluteError = getPoints().stream()
                .mapToDouble(p -> Math.pow(p.getY() - getPolynomial().evaluate(p.getX()), 2))
                .sum();

        return this.absoluteError;
    }

    public double calculateRSquared(LeastSquaresMethod leastSquaresMethod) {
        if (getPolynomial() == null) {
            doApproximations(leastSquaresMethod);
        }
        double average = getPoints().stream()
                .mapToDouble(PointXY::getY)
                .average().orElse(0);
        double SSm = getPoints().stream()
                .mapToDouble(p -> Math.pow(getPolynomial().evaluate(p.getX()) - average, 2))
                .sum();
        double SSt = getPoints().stream()
                .mapToDouble(p -> Math.pow(p.getY() - average, 2))
                .sum();

        this.rSquared = SSm / SSt;

        return this.rSquared;
    }

    public Polynomial getPolynomial() {
        return mathematicalFunctions.get(0).getPolynomial();
    }

    public List<PointXY> getPoints() {
        return points;
    }

    public int getSize() {
        return size;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public List<MathematicalFunction> getMathematicalFunctions() {
        return mathematicalFunctions;
    }

    public void setMathematicalFunctions(List<MathematicalFunction> mathematicalFunctions) {
        this.mathematicalFunctions = mathematicalFunctions;
    }

    public double getAbsoluteError() {
        return absoluteError;
    }

    public double getrSquared() {
        return rSquared;
    }
}
