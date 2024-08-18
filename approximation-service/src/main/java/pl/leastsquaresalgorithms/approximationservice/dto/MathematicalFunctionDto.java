package pl.leastsquaresalgorithms.approximationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.leastsquaresalgorithms.approximationservice.core.function.DomainFunction;

@Getter
@Setter
@Builder
public class MathematicalFunctionDto {
    private PolynomialDto polynomialDTO;
    private DomainFunction domainFunction;
}
