package pl.edu.wat.wcy.isi.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.isi.app.dto.ApproximationDTO;
import pl.edu.wat.wcy.isi.app.dto.message.request.ApproximationForm;
import pl.edu.wat.wcy.isi.app.service.ApproximationService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/doApproximations")
public class ApproximationController {
    private static final Logger logger = LoggerFactory.getLogger(ApproximationController.class);

    private final ApproximationService approximationService;

    public ApproximationController(ApproximationService approximationService) {
        this.approximationService = approximationService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ApproximationDTO> getApproximation(@RequestBody ApproximationForm approximationForm) {
        ApproximationDTO approximationDTO = new ApproximationDTO();

        approximationService.doApproximations(approximationForm.getChosenMethod(), approximationForm.getPoints(), approximationDTO);
        logger.info("Approximation successful - {}.", approximationForm.getChosenMethod().getLeastSquaresMethod());

        return new ResponseEntity<>(approximationDTO, HttpStatus.OK);
    }
}
