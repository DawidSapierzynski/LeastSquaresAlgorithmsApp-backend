package pl.edu.wat.wcy.isi.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChosenMethodDTO implements Serializable {
    private LeastSquaresMethod leastSquaresMethod;
    private int degree;
    private boolean isUsed;

    public ChosenMethodDTO(LeastSquaresMethod leastSquaresMethod, int degree) {
        this.leastSquaresMethod = leastSquaresMethod;
        this.degree = degree;
        this.isUsed = false;
    }
}
