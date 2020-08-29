package pl.edu.wat.wcy.isi.app.dto.message.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.wat.wcy.isi.app.dto.ChosenMethodDTO;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApproximationForm {
    private ChosenMethodDTO chosenMethod;
    private List<PointXY> points;
}
