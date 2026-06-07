package pl.least_squares_algorithms.approximation_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.core.LeastSquaresMethod;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChooseMethodService {
    public List<ChosenMethodDto> getChosenMethod(int degree) {
        return Arrays.stream(LeastSquaresMethod.values())
                .map(leastSquaresMethod -> new ChosenMethodDto(leastSquaresMethod, degree))
                .toList();
    }
}
