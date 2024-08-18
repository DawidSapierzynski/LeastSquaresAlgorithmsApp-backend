package pl.leastsquaresalgorithms.approximationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationservice.core.LeastSquaresMethod;
import pl.leastsquaresalgorithms.approximationservice.dto.ChosenMethodDto;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChooseMethodService {
    public List<ChosenMethodDto> getChosenMethod(int degree) {
        return Arrays.stream(LeastSquaresMethod.values())
                .map(leastSquaresMethod -> new ChosenMethodDto(leastSquaresMethod, degree))
                .toList();
    }
}
