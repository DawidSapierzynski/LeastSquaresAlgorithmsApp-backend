package pl.edu.wat.wcy.isi.app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.core.LeastSquaresMethod;
import pl.edu.wat.wcy.isi.app.dto.ApproximationPropertiesDTO;
import pl.edu.wat.wcy.isi.app.dto.ChosenMethodDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChooseMethodService {
    public List<ChosenMethodDTO> selectMethods(ApproximationPropertiesDTO approximationPropertiesDTO) {
        List<ChosenMethodDTO> chosenMethodDTOS = getChosenMethodDTOs(approximationPropertiesDTO.getDegree());

        log.debug("Selected methods: {}", chosenMethodDTOS.stream()
                .map(ChosenMethodDTO::getLeastSquaresMethod)
                .collect(Collectors.toList()));
        return chosenMethodDTOS;
    }

    public static List<ChosenMethodDTO> getChosenMethodDTOs(int degree) {
        List<ChosenMethodDTO> chosenMethodDTOs = new ArrayList<>();

        Arrays.stream(LeastSquaresMethod.values())
                .map(leastSquaresMethod -> new ChosenMethodDTO(leastSquaresMethod, degree))
                .forEach(chosenMethodDTOs::add);

        return chosenMethodDTOs;
    }
}
