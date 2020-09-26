package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;

public abstract class Decomposition {
    protected final Matrix matrixX;

    public Decomposition(Matrix matrixX) {
        this.matrixX = matrixX;
    }

    public abstract Matrix solve(Matrix matrixY);
}
