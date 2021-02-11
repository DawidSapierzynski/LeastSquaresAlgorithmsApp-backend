package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public class Standardization extends Decomposition {
    private Matrix standardizationMatrix;

    public Standardization(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {
        this.standardizationMatrix = this.matrixX.transpose().times(this.matrixX);
    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return this.standardizationMatrix
                .inverse()
                .times(this.matrixX.transpose())
                .times(matrixY);
    }
}
