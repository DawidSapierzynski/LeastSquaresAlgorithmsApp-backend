package pl.edu.wat.wcy.isi.app.core.function;

import pl.edu.wat.wcy.isi.app.core.function.polynomials.Polynomial;

public class MathematicalFunction {
    private final Polynomial polynomial;
    private final DomainFunction domainFunction;

    public MathematicalFunction(Polynomial polynomial, DomainFunction domainFunction) {
        this.polynomial = polynomial;
        this.domainFunction = domainFunction;
    }

    public Polynomial getPolynomial() {
        return polynomial;
    }

    public DomainFunction getDomainFunction() {
        return domainFunction;
    }
}
