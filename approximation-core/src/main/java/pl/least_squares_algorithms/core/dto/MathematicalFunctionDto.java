package pl.least_squares_algorithms.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.least_squares_algorithms.core.function.DomainFunction;

@Getter
@Setter
@Builder
public class MathematicalFunctionDto {
    private PolynomialDto polynomialDto;
    private DomainFunction domainFunction;
}
