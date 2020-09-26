package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public class StandardizationDecomposition extends Decomposition {
    public StandardizationDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    public Matrix solve(Matrix matrixY) {
        return (matrixX.transpose().times(matrixX))
                .inverse()
                .times(matrixX.transpose())
                .times(matrixY);
    }
}
