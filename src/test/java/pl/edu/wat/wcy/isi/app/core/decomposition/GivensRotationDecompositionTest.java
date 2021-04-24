package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GivensRotationDecompositionTest {
    public static final int N = 10;
    public static final double DELTA = 1e-10;
    public static final double G = 10000;
    private Matrix m;
    private Random random;

    @BeforeEach
    void setUp() {
        this.random = new Random();
        this.m = new Matrix(getRandomArray());
    }

    private double[][] getRandomArray() {
        double[][] array = new double[N * 2][N];
        for (int i = 0; i < N * 2; i++) {
            for (int j = 0; j < N; j++) {
                array[i][j] = this.random.nextDouble() * G - G / 2;
            }
        }
        return array;
    }

    @Test
    void decompose() {
        GivensRotationDecomposition givensRotationDecomposition = new GivensRotationDecomposition(this.m);
        givensRotationDecomposition.decompose();
        Matrix matrixR = givensRotationDecomposition.R;
        Matrix matrixQ = givensRotationDecomposition.Q;

        upperTriangularMatrixTest(matrixR);
        orthogonalMatrixTest(matrixQ);
        matrixCompatibilityTest(matrixR, matrixQ);
    }

    void upperTriangularMatrixTest(Matrix matrixR) {
        for (int i = 0; i < matrixR.getColumnDimension(); i++) {
            for (int j = matrixR.getRowDimension() - 1; j > i; j--) {
                assertEquals(0.0, matrixR.get(j, i), DELTA);
            }
        }
    }

    void orthogonalMatrixTest(Matrix matrixQ) {
        int n = matrixQ.getRowDimension();
        Matrix indetity = Matrix.identity(n, n);
        Matrix q = matrixQ.times(matrixQ.transpose());

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                assertEquals(indetity.get(i, j), q.get(i, j), DELTA);
            }
        }
    }

    void matrixCompatibilityTest(Matrix matrixR, Matrix matrixQ) {
        Matrix matrixA = matrixQ.times(matrixR);

        for (int i = 0; i < this.m.getRowDimension(); i++) {
            for (int j = 0; j < this.m.getColumnDimension(); j++) {
                assertEquals(this.m.get(i, j), matrixA.get(i, j), DELTA);
            }
        }
    }
}