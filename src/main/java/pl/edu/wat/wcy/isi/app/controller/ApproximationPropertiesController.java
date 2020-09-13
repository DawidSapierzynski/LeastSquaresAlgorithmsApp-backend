package pl.edu.wat.wcy.isi.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.isi.app.configuration.exception.ForbiddenException;
import pl.edu.wat.wcy.isi.app.configuration.exception.ResourceNotFoundException;
import pl.edu.wat.wcy.isi.app.configuration.exception.SizeException;
import pl.edu.wat.wcy.isi.app.dto.ApproximationPropertiesDTO;
import pl.edu.wat.wcy.isi.app.dto.message.response.ResponseMessage;
import pl.edu.wat.wcy.isi.app.mapper.ApproximationPropertiesMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.ApproximationPropertiesEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.service.ApproximationPropertiesService;
import pl.edu.wat.wcy.isi.app.service.DataSeriesFileService;
import pl.edu.wat.wcy.isi.app.service.UserService;

import java.sql.Timestamp;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/approximationProperties")
public class ApproximationPropertiesController {
    private static final Logger logger = LoggerFactory.getLogger(ApproximationPropertiesController.class);

    private final ApproximationPropertiesService approximationPropertiesService;
    private final DataSeriesFileService dataSeriesFileService;
    private final UserService userService;
    private final ApproximationPropertiesMapper approximationPropertiesMapper;

    public ApproximationPropertiesController(ApproximationPropertiesService approximationPropertiesService,
                                             DataSeriesFileService dataSeriesFileService,
                                             UserService userService,
                                             ApproximationPropertiesMapper approximationPropertiesMapper) {
        this.approximationPropertiesService = approximationPropertiesService;
        this.dataSeriesFileService = dataSeriesFileService;
        this.userService = userService;
        this.approximationPropertiesMapper = approximationPropertiesMapper;
    }

    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<ApproximationPropertiesDTO> postApproximationProperties(@RequestParam("dataSeriesFileId") long dataSeriesFileId, @RequestParam("degree") int degree) throws ResourceNotFoundException {

        ApproximationPropertiesEntity approximationProperties = new ApproximationPropertiesEntity();
        DataSeriesFileEntity dataSeriesFileEntity = dataSeriesFileService.findById(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("Data series file not found for this id: " + dataSeriesFileId));
        UserEntity userEntity = userService.getLoggedUser();

        approximationProperties.setDataSeriesFile(dataSeriesFileEntity);
        approximationProperties.setDeleted((byte) 0);
        approximationProperties.setUser(userEntity);
        approximationProperties.setDegreeApproximation(degree);
        approximationProperties.setDateCreate(new Timestamp(System.currentTimeMillis()));

        approximationProperties = approximationPropertiesService.save(approximationProperties);

        logger.info("Adding approximation properties completed successfully.");
        return new ResponseEntity<>(approximationPropertiesMapper.bulidApproximationPropertiesDTO(approximationProperties), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ApproximationPropertiesDTO>> getApproximationPropertiesUser() {
        UserEntity userEntity = userService.getLoggedUser();

        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findByUserAndDeleted(userEntity, (byte) 0);
        List<ApproximationPropertiesDTO> approximationPropertiesDTOS = approximationPropertiesMapper.bulidApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.info("Getting all (for user) the approximation properties successfully completed. Size: {}", approximationPropertiesDTOS.size());
        return new ResponseEntity<>(approximationPropertiesDTOS, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json", value = "/all")
    public ResponseEntity<List<ApproximationPropertiesDTO>> getApproximationPropertiesAdmin() {
        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findAll();
        List<ApproximationPropertiesDTO> approximationPropertiesDTOs = approximationPropertiesMapper.bulidApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.info("Getting all the approximation properties successfully completed. Size: {}", approximationPropertiesDTOs.size());
        return new ResponseEntity<>(approximationPropertiesDTOs, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ApproximationPropertiesDTO> getApproximationProperties(@PathVariable(value = "approximationPropertiesId") long approximationPropertiesId) throws ResourceNotFoundException, ForbiddenException, SizeException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findByIdAndDeleted(approximationPropertiesId, (byte) 0)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

        UserEntity loggedUser = userService.getLoggedUser();
        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to open this approximation properties");
        }

        dataSeriesFileService.readFile(approximationProperties.getDataSeriesFile().getDataSeriesFileId(), approximationProperties.getDataSeriesFile());

        logger.info("Get approximation properties successfully completed. Id: {}", approximationPropertiesId);
        return new ResponseEntity<>(approximationPropertiesMapper.bulidApproximationPropertiesDTO(approximationProperties), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ResponseMessage> deletedApproximationProperties(@PathVariable(value = "approximationPropertiesId") long approximationPropertiesId) throws ResourceNotFoundException, ForbiddenException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findById(approximationPropertiesId)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

        UserEntity loggedUser = userService.getLoggedUser();
        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to delete this approximation properties");
        }

        this.approximationPropertiesService.delete(approximationProperties);

        logger.info("Deleted approximation properties with id: {}", approximationPropertiesId);
        return ResponseEntity.ok(new ResponseMessage("Deleted approximation properties with id: " + approximationPropertiesId));
    }
}
