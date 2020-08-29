package pl.edu.wat.wcy.isi.app.core;

public enum LeastSquaresMethod {
    STANDARDIZATION("Standardization"),
    HOUSEHOLDER_TRANSFORMATION("Householder Transformation"),
    GIVENS_ROTATION("Givens Rotation"),
    SINGULAR_VALUE_DECOMPOSITION("Singular Value Decomposition");

    private final String name;

    LeastSquaresMethod(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
