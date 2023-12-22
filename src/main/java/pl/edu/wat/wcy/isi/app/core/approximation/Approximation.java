package pl.edu.wat.wcy.isi.app.core.approximation;

import lombok.Getter;
import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;
import pl.edu.wat.wcy.isi.app.core.function.MathematicalFunction;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Approximation {
    private final List<PointXY> points;
    private final int degree;
    private final int size;
    private List<MathematicalFunction> mathematicalFunctions;
    protected double absoluteError;
    protected double convergenceCoefficient;

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

    public double calculateConvergenceCoefficient(LeastSquaresMethod leastSquaresMethod) {
        if (getPolynomial() == null) {
            doApproximations(leastSquaresMethod);
        }
        double average = getPoints().stream()
                .mapToDouble(PointXY::getY)
                .average().orElse(0);
        double SSm = getPoints().stream()
                .mapToDouble(p -> Math.pow(p.getY() - getPolynomial().evaluate(p.getX()), 2))
                .sum();
        double SSt = getPoints().stream()
                .mapToDouble(p -> Math.pow(p.getY() - average, 2))
                .sum();

        this.convergenceCoefficient = SSm / SSt;

        return this.convergenceCoefficient;
    }

    public Polynomial getPolynomial() {
        return mathematicalFunctions.get(0).polynomial();
    }

    public void setMathematicalFunctions(List<MathematicalFunction> mathematicalFunctions) {
        this.mathematicalFunctions = mathematicalFunctions;
    }
}
