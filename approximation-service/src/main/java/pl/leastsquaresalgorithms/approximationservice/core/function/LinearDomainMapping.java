package pl.leastsquaresalgorithms.approximationservice.core.function;

import Jama.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.AlgebraicPolynomial;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LinearDomainMapping {
    private static final Logger logger = LoggerFactory.getLogger(LinearDomainMapping.class);
    public static final double MIN = 0;
    public static final double MAX = 6.28;
    private final List<PointXY> oldPoints;
    private List<PointXY> newPoints;
    private AlgebraicPolynomial linearFunction;

    public LinearDomainMapping(List<PointXY> oldPoints) {
        this.oldPoints = oldPoints;
    }

    public List<PointXY> convert() {
        double x0 = oldPoints.get(0).getX();
        double xn = oldPoints.get(oldPoints.size() - 1).getX();

        this.linearFunction = findLinearFunction(x0, xn);
        newPoints = new ArrayList<>();

        for (PointXY p : oldPoints) {
            newPoints.add(new PointXY(linearFunction.evaluate(p.getX()), p.getY()));
        }

        return newPoints;
    }

    private AlgebraicPolynomial findLinearFunction(double x0, double xn) {
        AlgebraicPolynomial linearFunction;
        double[][] x, y;
        Matrix matrixX, matrixY, matrixA;

        x = new double[][]{{1, x0}, {1, xn}};
        y = new double[][]{{MIN}, {MAX}};

        matrixX = new Matrix(x);
        logger.debug("Matrix X:\n{}", Arrays.toString(matrixX.getArray()));

        matrixY = new Matrix(y);
        logger.debug("Matrix Y:\n{}", Arrays.toString(matrixY.getArray()));

        matrixA = (((matrixX.transpose().times(matrixX)).inverse()).times(matrixX.transpose())).times(matrixY);
        logger.info("Matrix A:\n{}", Arrays.toString(matrixA.getArray()));

        linearFunction = new AlgebraicPolynomial((matrixA.transpose().getArray())[0]);
        logger.debug("Linear function: {}", linearFunction);

        return linearFunction;
    }

    public List<PointXY> getOldPoints() {
        return oldPoints;
    }

    public List<PointXY> getNewPoints() {
        return newPoints;
    }

    public AlgebraicPolynomial getLinearFunction() {
        return linearFunction;
    }
}
