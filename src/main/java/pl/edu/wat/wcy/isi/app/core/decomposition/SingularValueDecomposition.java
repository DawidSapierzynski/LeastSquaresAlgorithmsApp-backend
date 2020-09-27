package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public class SingularValueDecomposition extends Decomposition {
    public SingularValueDecomposition(Matrix matrixX) {
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
