package pl.least_squares_algorithms.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.least_squares_algorithms.core.PointXY;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproximationDto {
    private List<MathematicalFunctionDto> mathematicalFunctionDtos;
    private List<PointXY> points;
    private double absoluteError;
    private double convergenceCoefficient;
}
