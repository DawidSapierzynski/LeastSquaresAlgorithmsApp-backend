package pl.leastsquaresalgorithms.approximationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.least_squares_algorithms.core.dto.ApproximationDto;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationForm;
import pl.leastsquaresalgorithms.approximationservice.service.ApproximationService;
import pl.leastsquaresalgorithms.approximationservice.service.ChooseMethodService;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping(value = "/approximation")
@RequiredArgsConstructor
@Slf4j
public class ApproximationController {
    private final ApproximationService approximationService;
    private final ChooseMethodService chooseMethodService;

    @PostMapping
    public ApproximationDto doApproximation(@RequestBody ApproximationForm approximationForm) {
        validateApproximationForm(approximationForm);
        ApproximationDto approximationDTO = approximationService.doApproximations(approximationForm.getChosenMethod(), approximationForm.getPoints());
        log.info("Approximation successful - {}.", approximationForm.getChosenMethod().getLeastSquaresMethod());
        return approximationDTO;
    }

    @GetMapping
    public List<ChosenMethodDto> getMethods(@RequestParam Integer degree) {
        if (degree == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No degree chose.");
        }
        List<ChosenMethodDto> chosenMethodDtos = chooseMethodService.getChosenMethod(degree);
        log.debug("Choosing successful methods. Number of methods: {}", chosenMethodDtos.size());
        return chosenMethodDtos;
    }

    @PostMapping(value = "/download", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<InputStreamResource> downloadApproximationResult(@RequestBody List<MathematicalFunctionDto> mathematicalFunctionDtos) {
        validateMathematicalFunction(mathematicalFunctionDtos);
        byte[] text = approximationService.getApproximationResult(mathematicalFunctionDtos);
        log.debug("Downloading approximation results completed successfully.");
        ContentDisposition disposition = ContentDisposition.attachment().filename("approximation-points.txt").build();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .headers(httpHeaders -> httpHeaders.setContentDisposition(disposition))
                .body(new InputStreamResource(new ByteArrayInputStream(text)));
    }

    private static void validateApproximationForm(ApproximationForm approximationForm) {
        if (approximationForm.getPoints() == null || approximationForm.getPoints().size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of points must be greater than 1.");
        }
        if (approximationForm.getPoints().size() > 10_000) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Number of points must be less than 10 000.");
        }
        if (approximationForm.getChosenMethod() == null || approximationForm.getChosenMethod().getLeastSquaresMethod() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No method chose.");
        }
        if (approximationForm.getChosenMethod().getDegree() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No degree chose.");
        }
    }

    private static void validateMathematicalFunction(List<MathematicalFunctionDto> mathematicalFunctionDtos) {
        if (mathematicalFunctionDtos == null || mathematicalFunctionDtos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No mathematical functions provided.");
        }
        for (MathematicalFunctionDto mathematicalFunctionDto : mathematicalFunctionDtos) {
            if (mathematicalFunctionDto.getDomainFunction() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No domain function provided.");
            }
            if (mathematicalFunctionDto.getPolynomialDto() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No polynomial coefficients provided.");
            }
        }
    }
}
