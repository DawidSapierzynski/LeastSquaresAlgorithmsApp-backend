package pl.leastsquaresalgorithms.approximationservice.dto;

import lombok.*;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproximationForm {
    private ChosenMethodDto chosenMethod;
    private List<PointXY> points;
}
