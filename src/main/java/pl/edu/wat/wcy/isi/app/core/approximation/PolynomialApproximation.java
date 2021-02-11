package pl.edu.wat.wcy.isi.app.core.approximation;

import Jama.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;
import pl.edu.wat.wcy.isi.app.core.decomposition.*;
import pl.edu.wat.wcy.isi.app.core.function.DomainFunction;
import pl.edu.wat.wcy.isi.app.core.function.MathematicalFunction;
import pl.edu.wat.wcy.isi.app.core.function.polynomials.AlgebraicPolynomial;
import pl.edu.wat.wcy.isi.app.model.PointXY;

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

        matrixX = setMatrixBaseFunction(mapPoints.stream().mapToDouble(PointXY::getX).toArray());
        logger.debug("Matrix X:\n {}", matrixX);

        matrixY = setMatrixY(mapPoints.stream().mapToDouble(PointXY::getY).toArray());
        logger.debug("Matrix Y:\n {}", matrixY);

        Decomposition decomposition = getDecomposition(matrixX, leastSquaresMethod);
        matrixA = decomposition.solve(matrixY);
        logger.debug("Decomposition - Matrix A:\n {}", matrixA);

        setMathematicalFunctions(createMathematicalFunction(mapMatrixAToList(matrixA)));

        logger.info("Absolute error Polynomial Approximation = {}", calculateError(leastSquaresMethod));

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
            case HOUSEHOLDER_TRANSFORMATION: {
                return new HouseholderTransformationDecomposition(matrixX);
            }
            case GIVENS_ROTATION: {
                return new GivensRotationDecomposition(matrixX);
            }
            case SINGULAR_VALUE_DECOMPOSITION: {
                return new SingularValueDecomposition(matrixX);
            }
            default: {
                return new Standardization(matrixX);
            }
        }
    }

    private Matrix setMatrixBaseFunction(double[] pointsX) {
        int size = pointsX.length;
        double[][] matrix = new double[size][getDegree() + 1];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j <= getDegree(); j++) {
                matrix[i][j] = AlgebraicPolynomial.getValueMonomial(pointsX[i], j);
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

    private Matrix setMatrixY(double[] pointsY) {
        double[][] y = new double[pointsY.length][1];

        for (int i = 0; i < pointsY.length; i++) {
            y[i][0] = pointsY[i];
        }

        return new Matrix(y);
    }

}
