package pl.leastsquaresalgorithms.approximationservice.dto;

import lombok.*;

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
