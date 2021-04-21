package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GivensRotationDecompositionTest {
    public static final int N = 4;
    public static final double DELTA = 0.000000001;
    private Matrix m;
    private Random random;

    @BeforeEach
    void setUp() {
        this.random = new Random();
        this.m = new Matrix(getRandomArray());
    }

    private double[][] getRandomArray() {
        double[][] array = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                array[i][j] = this.random.nextDouble();
            }
        }
        return array;
    }

    @Test
    void decompose() {
        GivensRotationDecomposition givensRotationDecomposition = new GivensRotationDecomposition(this.m);
        givensRotationDecomposition.decompose();
        Matrix matrixR = givensRotationDecomposition.R;

        for (int i = 0; i < matrixR.getColumnDimension(); i++) {
            for (int j = matrixR.getRowDimension() - 1; j > i; j--) {
                assertEquals(0.0, matrixR.get(j, i), DELTA);
            }
        }
    }
}