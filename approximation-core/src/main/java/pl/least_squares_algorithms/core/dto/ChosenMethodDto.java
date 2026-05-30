package pl.least_squares_algorithms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.least_squares_algorithms.core.LeastSquaresMethod;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChosenMethodDto implements Serializable {
    private LeastSquaresMethod leastSquaresMethod;
    private Integer degree;
    private boolean isUsed;

    public ChosenMethodDto(LeastSquaresMethod leastSquaresMethod, Integer degree) {
        this.leastSquaresMethod = leastSquaresMethod;
        this.degree = degree;
        this.isUsed = false;
    }
}
