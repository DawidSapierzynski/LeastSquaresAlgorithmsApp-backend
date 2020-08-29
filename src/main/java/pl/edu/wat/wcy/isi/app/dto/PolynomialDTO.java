package pl.edu.wat.wcy.isi.app.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolynomialDTO {
    private List<Double> coefficients;
    private int degree;
}
