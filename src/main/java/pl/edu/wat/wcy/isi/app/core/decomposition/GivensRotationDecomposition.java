package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GivensRotationDecomposition extends Decomposition {
    private Matrix Q, R;

    public GivensRotationDecomposition(Matrix matrixX) {
        super(matrixX);
    }

    @Override
    protected void decompose() {
        int m = this.matrixX.getRowDimension();
        int n = this.matrixX.getColumnDimension();
        this.Q = Matrix.identity(m, m);
        this.R = this.matrixX.copy();
        GivensRotation givensRotation;
        Matrix g;

        for (int j = 0; j < n; j++) {
            for (int i = m - 1; i > j; i--) {
                if (this.R.get(i, j) != 0.0) {
                    givensRotation = GivensRotation.givens(this.R.get(i - 1, j), this.R.get(i, j));
                    g = givensRotation.getGivensRotationMatrix(i, j, m);
                    this.R = g.times(this.R);
                    this.Q = this.Q.times(g);
                }
            }
        }
    }

    @Override
    protected Matrix solveUsingLeastSquares(Matrix matrixY) {
        return this.R.inverse()
                .times(this.Q.transpose())
                .times(matrixY);
    }
}
