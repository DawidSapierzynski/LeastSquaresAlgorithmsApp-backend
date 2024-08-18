package pl.leastsquaresalgorithms.approximationservice.core.decomposition;

import Jama.Matrix;

public class Normalization extends Decomposition {
    private Matrix normalizationMatrix;

    public Normalization(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {
        this.normalizationMatrix = this.matrixX.transpose().times(this.matrixX);
    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return this.normalizationMatrix.chol()
                .solve(this.matrixX.transpose().times(matrixY));
    }
}
