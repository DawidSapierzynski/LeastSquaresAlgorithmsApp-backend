package pl.leastsquaresalgorithms.approximationservice.core.decomposition;

import Jama.Matrix;

public class SingularValueDecomposition extends Decomposition {
    private Matrix U, S, V;

    public SingularValueDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {
        Jama.SingularValueDecomposition singularValueDecomposition = new Jama.SingularValueDecomposition(this.matrixX);
        this.U = singularValueDecomposition.getU();
        this.S = singularValueDecomposition.getS();
        this.V = singularValueDecomposition.getV();
    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return this.V
                .times(this.S.inverse())
                .times(this.U.transpose())
                .times(matrixY);
    }
}
