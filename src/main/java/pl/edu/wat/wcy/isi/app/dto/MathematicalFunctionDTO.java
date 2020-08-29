package pl.edu.wat.wcy.isi.app.dto;

import lombok.*;
import pl.edu.wat.wcy.isi.app.core.function.DomainFunction;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MathematicalFunctionDTO {
    private PolynomialDTO polynomialDTO;
    private DomainFunction domainFunction;
}
