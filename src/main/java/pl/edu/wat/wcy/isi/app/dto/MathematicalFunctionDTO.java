package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.core.function.DomainFunction;

@Getter
@Setter
@Builder
public class MathematicalFunctionDTO {
    private PolynomialDTO polynomialDTO;
    private DomainFunction domainFunction;
}
