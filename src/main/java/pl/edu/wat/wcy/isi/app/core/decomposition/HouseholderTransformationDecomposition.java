package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import Jama.QRDecomposition;

public class HouseholderTransformationDecomposition extends Decomposition {
    private QRDecomposition qrDecomposition;

    public HouseholderTransformationDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {
        qrDecomposition = matrixX.qr();
    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return qrDecomposition.solve(matrixY);
    }
}
