package pl.edu.wat.wcy.isi.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.isi.app.dto.ApproximationPropertiesDTO;
import pl.edu.wat.wcy.isi.app.dto.ChosenMethodDTO;
import pl.edu.wat.wcy.isi.app.service.ChooseMethodService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/chooseMethod")
public class ChooseMethodController {
    private static final Logger logger = LoggerFactory.getLogger(ChooseMethodController.class);

    private final ChooseMethodService chooseMethodService;

    public ChooseMethodController(ChooseMethodService chooseMethodService) {
        this.chooseMethodService = chooseMethodService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<List<ChosenMethodDTO>> getMethods(@RequestBody ApproximationPropertiesDTO approximationPropertiesDTO) {
        List<ChosenMethodDTO> chosenMethodDTOs;
        chosenMethodDTOs = chooseMethodService.selectMethods(approximationPropertiesDTO);

        logger.info("Choosing successful methods. Number of methods: {}", chosenMethodDTOs.size());
        return new ResponseEntity<>(chosenMethodDTOs, HttpStatus.OK);
    }
}
