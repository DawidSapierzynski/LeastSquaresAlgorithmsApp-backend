package pl.leastsquaresalgorithms.approximationservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PolynomialDto {
    private List<Double> coefficients;
    private int degree;
}
