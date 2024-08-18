package pl.leastsquaresalgorithms.approximationservice.core.decomposition;

import Jama.Matrix;

public abstract class Decomposition {
    protected final Matrix matrixX;

    public Decomposition(Matrix matrixX) {
        this.matrixX = matrixX;
    }

    protected abstract void decompose();

    protected abstract Matrix solveUsingLeastSquares(Matrix matrixY);

    public Matrix solve(Matrix matrixY) {
        decompose();
        return solveUsingLeastSquares(matrixY);
    }
}
