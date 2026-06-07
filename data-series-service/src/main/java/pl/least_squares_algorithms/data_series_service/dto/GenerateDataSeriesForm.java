package pl.least_squares_algorithms.data_series_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDataSeriesForm {
    @Max(value = 10000, message = "Number of points must be no more than 10000")
    @Min(value = 5, message = "Number of points mustn't be less than 5")
    private int numberPoints;
    private MathematicalFunctionDto mathematicalFunctionDTO;
    private String weightDistribution;
    private String distanceX;
    private Boolean noise;
}
