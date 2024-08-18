package pl.leastsquaresalgorithms.approximationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationDto;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationForm;
import pl.leastsquaresalgorithms.approximationservice.dto.ChosenMethodDto;
import pl.leastsquaresalgorithms.approximationservice.service.ApproximationService;
import pl.leastsquaresalgorithms.approximationservice.service.ChooseMethodService;

import java.util.List;

@RestController
@RequestMapping(value = "/doApproximations")
@RequiredArgsConstructor
@Slf4j
public class ApproximationController {
    private final ApproximationService approximationService;
    private final ChooseMethodService chooseMethodService;

    @PostMapping(produces = "application/json")
    public ResponseEntity<ApproximationDto> doApproximation(@RequestBody ApproximationForm approximationForm) {
        ApproximationDto approximationDTO = approximationService.doApproximations(approximationForm.getChosenMethod(), approximationForm.getPoints());
        log.info("Approximation successful - {}.", approximationForm.getChosenMethod().getLeastSquaresMethod());
        return ResponseEntity.ok(approximationDTO);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ChosenMethodDto>> getMethods(@RequestParam Integer degree) {
        List<ChosenMethodDto> chosenMethodDtos = chooseMethodService.getChosenMethod(degree);
        log.debug("Choosing successful methods. Number of methods: {}", chosenMethodDtos.size());
        return ResponseEntity.ok(chosenMethodDtos);
    }
}
