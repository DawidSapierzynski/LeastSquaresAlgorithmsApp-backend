package pl.edu.wat.wcy.isi.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PolynomialDTO {
    private List<Double> coefficients;
    private int degree;
}
