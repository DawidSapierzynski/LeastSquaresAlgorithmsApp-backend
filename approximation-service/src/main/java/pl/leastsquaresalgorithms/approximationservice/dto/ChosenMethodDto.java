package pl.leastsquaresalgorithms.approximationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.leastsquaresalgorithms.approximationservice.core.LeastSquaresMethod;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChosenMethodDto implements Serializable {
    private LeastSquaresMethod leastSquaresMethod;
    private int degree;
    private boolean isUsed;

    public ChosenMethodDto(LeastSquaresMethod leastSquaresMethod, int degree) {
        this.leastSquaresMethod = leastSquaresMethod;
        this.degree = degree;
        this.isUsed = false;
    }
}
