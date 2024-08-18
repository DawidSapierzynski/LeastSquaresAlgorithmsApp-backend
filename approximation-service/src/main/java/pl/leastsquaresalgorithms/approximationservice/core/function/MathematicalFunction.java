package pl.leastsquaresalgorithms.approximationservice.core.function;

import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.Polynomial;

public record MathematicalFunction(Polynomial polynomial, DomainFunction domainFunction) {
}
