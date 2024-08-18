package pl.leastsquaresalgorithms.approximationservice.core.approximation;

import Jama.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.leastsquaresalgorithms.approximationservice.core.LeastSquaresMethod;
import pl.leastsquaresalgorithms.approximationservice.core.decomposition.*;
import pl.leastsquaresalgorithms.approximationservice.core.function.DomainFunction;
import pl.leastsquaresalgorithms.approximationservice.core.function.MathematicalFunction;
import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.AlgebraicPolynomial;
import pl.leastsquaresalgorithms.approximationservice.dto.PointXY;

import java.util.ArrayList;
import java.util.List;

public class PolynomialApproximation extends Approximation {
    private final Logger logger = LoggerFactory.getLogger(PolynomialApproximation.class);

    public PolynomialApproximation(List<PointXY> points, int degree) {
        super(points, degree);
    }

    @Override
    public List<MathematicalFunction> doApproximations(LeastSquaresMethod leastSquaresMethod) {
        Matrix matrixX, matrixY, matrixA;
        List<PointXY> mapPoints = getPoints();

        matrixX = setMatrixBaseFunction(mapPoints);
        logger.debug("Matrix X:\n {}", matrixX);

        matrixY = setMatrixY(mapPoints);
        logger.debug("Matrix Y:\n {}", matrixY);

        Decomposition decomposition = getDecomposition(matrixX, leastSquaresMethod);
        matrixA = decomposition.solve(matrixY);
        logger.debug("Decomposition - Matrix A:\n {}", matrixA);

        setMathematicalFunctions(createMathematicalFunction(mapMatrixAToList(matrixA)));
        logger.info("Absolute error Polynomial Approximation = {}", calculateError(leastSquaresMethod));
        logger.info("Convergence coefficient = {}", calculateConvergenceCoefficient(leastSquaresMethod));

        return getMathematicalFunctions();
    }

    private List<MathematicalFunction> createMathematicalFunction(List<Double> coefficients) {
        AlgebraicPolynomial algebraicPolynomial = new AlgebraicPolynomial(coefficients);
        double minX = getPoints().stream().mapToDouble(PointXY::getX).min().orElse(0);
        double maxX = getPoints().stream().mapToDouble(PointXY::getX).max().orElse(0);
        DomainFunction domainFunction = new DomainFunction(true, minX, maxX, true);
        MathematicalFunction mathematicalFunction = new MathematicalFunction(algebraicPolynomial, domainFunction);

        return List.of(mathematicalFunction);
    }

    private Decomposition getDecomposition(Matrix matrixX, LeastSquaresMethod leastSquaresMethod) {
        switch (leastSquaresMethod) {
            case HOUSEHOLDER_TRANSFORMATION -> {
                return new HouseholderTransformationDecomposition(matrixX);
            }
            case GIVENS_ROTATION -> {
                return new GivensRotationDecomposition(matrixX);
            }
            case SINGULAR_VALUE_DECOMPOSITION -> {
                return new SingularValueDecomposition(matrixX);
            }
            default -> {
                return new Normalization(matrixX);
            }
        }
    }

    private Matrix setMatrixBaseFunction(List<PointXY> pointsXY) {
        int size = pointsXY.size();
        double[][] matrix = new double[size][getDegree() + 1];
        PointXY pointXY;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= getDegree(); j++) {
                pointXY = pointsXY.get(i);
                matrix[i][j] = pointXY.getWeight() * AlgebraicPolynomial.getValueMonomial(pointXY.getX(), j);
            }
        }
        return new Matrix(matrix);
    }

    private List<Double> mapMatrixAToList(Matrix matrixA) {
        List<Double> result = new ArrayList<>();
        for (double[] d : matrixA.getArray()) {
            result.add(d[0]);
        }
        return result;
    }

    private Matrix setMatrixY(List<PointXY> pointsXY) {
        int size = pointsXY.size();
        double[][] y = new double[size][1];
        PointXY pointXY;

        for (int i = 0; i < size; i++) {
            pointXY = pointsXY.get(i);
            y[i][0] = pointXY.getWeight() * pointXY.getY();
        }

        return new Matrix(y);
    }

}
