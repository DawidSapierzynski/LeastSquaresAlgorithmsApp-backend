package pl.least_squares_algorithms.core.function;

import pl.least_squares_algorithms.core.function.polynomials.Polynomial;

public record MathematicalFunction(Polynomial polynomial, DomainFunction domainFunction) {
}
