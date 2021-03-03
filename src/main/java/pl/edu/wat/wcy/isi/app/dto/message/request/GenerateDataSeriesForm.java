package pl.edu.wat.wcy.isi.app.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.dto.MathematicalFunctionDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateDataSeriesForm {
    @Max(value = 10000, message = "Number of points must be no more than 10000")
    @Min(value = 5, message = "Number of points mustn't be less than 5")
    private int numberPoints;
    private MathematicalFunctionDTO mathematicalFunctionDTO;
    private String weightDistribution;
    private String distanceX;
}
