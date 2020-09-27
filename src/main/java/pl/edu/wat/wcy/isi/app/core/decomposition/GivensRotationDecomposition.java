package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public class GivensRotationDecomposition extends Decomposition {
    public GivensRotationDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {

    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return new Matrix(this.matrixX.getColumnDimension(), 1, 1);
    }
}
