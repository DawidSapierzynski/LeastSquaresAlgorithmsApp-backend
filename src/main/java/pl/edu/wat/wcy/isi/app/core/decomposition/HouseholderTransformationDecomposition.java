package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public class HouseholderTransformationDecomposition extends Decomposition {
    public HouseholderTransformationDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {

    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return matrixX.qr().solve(matrixY);
    }
}
