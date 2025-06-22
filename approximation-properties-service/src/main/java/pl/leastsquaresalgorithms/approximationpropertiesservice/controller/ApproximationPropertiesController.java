package pl.leastsquaresalgorithms.approximationpropertiesservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.leastsquaresalgorithms.approximationpropertiesservice.client.DataSeriesFileClient;
import pl.leastsquaresalgorithms.approximationpropertiesservice.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ApproximationPropertiesDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ResponseMessage;
import pl.leastsquaresalgorithms.approximationpropertiesservice.mapper.ApproximationPropertiesMapper;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;
import pl.leastsquaresalgorithms.approximationpropertiesservice.service.ApproximationPropertiesService;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/approximation-properties")
public class ApproximationPropertiesController {
    private static final Logger logger = LoggerFactory.getLogger(ApproximationPropertiesController.class);
    private final ApproximationPropertiesService approximationPropertiesService;
    private final DataSeriesFileClient dataSeriesFileClient;
    //    private final UserService userService;
    private final ApproximationPropertiesMapper approximationPropertiesMapper;

    public ApproximationPropertiesController(ApproximationPropertiesService approximationPropertiesService,
                                             DataSeriesFileClient dataSeriesFileClient,
                                             ApproximationPropertiesMapper approximationPropertiesMapper) {
        this.approximationPropertiesService = approximationPropertiesService;
        this.dataSeriesFileClient = dataSeriesFileClient;
        this.approximationPropertiesMapper = approximationPropertiesMapper;
    }

    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<ApproximationPropertiesDto> createApproximationProperties(@RequestParam("dataSeriesFileId") BigInteger dataSeriesFileId, @RequestParam("degree") int degree) throws ResourceNotFoundException {

        DataSeriesFileDto dataSeriesFileEntity = dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId);
//                .orElseThrow(() -> new ResourceNotFoundException("Data series file not found for this id: " + dataSeriesFileId));
//        UserEntity userEntity = userService.getLoggedUser();

        ApproximationPropertiesEntity approximationProperties = new ApproximationPropertiesEntity();
        approximationProperties.setDataSeriesFileId(dataSeriesFileId);
        approximationProperties.setDeleted(Boolean.FALSE);
        approximationProperties.setUserId(BigInteger.ONE);
        approximationProperties.setDegreeApproximation(degree);
        approximationProperties.setDateCreate(new Timestamp(System.currentTimeMillis()));

        approximationProperties = approximationPropertiesService.save(approximationProperties);

        logger.debug("Adding approximation properties completed successfully.");
        return new ResponseEntity<>(approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties, dataSeriesFileEntity), HttpStatus.CREATED);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ApproximationPropertiesDto>> getApproximationProperties() {
//        UserEntity userEntity = userService.getLoggedUser();

        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findByUserAndDeleted(BigInteger.ONE, false);
        List<ApproximationPropertiesDto> approximationPropertiesDtos = approximationPropertiesMapper.buildApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.debug("Getting all (for user) the approximation properties successfully completed. Size: {}", approximationPropertiesDtos.size());
        return ResponseEntity.ok(approximationPropertiesDtos);
    }

    @GetMapping(produces = "application/json", value = "/all")
    public ResponseEntity<List<ApproximationPropertiesDto>> getAllApproximationProperties() {
        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findAll();
        List<ApproximationPropertiesDto> approximationPropertiesDtos = approximationPropertiesMapper.buildApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.debug("Getting all the approximation properties successfully completed. Size: {}", approximationPropertiesDtos.size());
        return ResponseEntity.ok(approximationPropertiesDtos);
    }

    @GetMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ApproximationPropertiesDto> getApproximationProperties(@PathVariable(value = "approximationPropertiesId") BigInteger approximationPropertiesId) throws ResourceNotFoundException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findByIdAndDeleted(approximationPropertiesId, false)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
//            throw new ForbiddenException("No permission to open this approximation properties");
//        }

        DataSeriesFileDto dataSeriesFileEntity = dataSeriesFileClient.getDataSeriesFile(approximationProperties.getDataSeriesFileId());
        logger.debug("Get approximation properties successfully completed. Id: {}", approximationPropertiesId);
        return ResponseEntity.ok(approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties, dataSeriesFileEntity));
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ResponseMessage> deletedApproximationProperties(@PathVariable(value = "approximationPropertiesId") BigInteger approximationPropertiesId) throws ResourceNotFoundException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findById(approximationPropertiesId)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
//            throw new ForbiddenException("No permission to delete this approximation properties");
//        }

        this.approximationPropertiesService.delete(approximationProperties);
        logger.debug("Deleted approximation properties with id: {}", approximationPropertiesId);
        return ResponseEntity.ok(new ResponseMessage("Deleted approximation properties with id: " + approximationPropertiesId));
    }
}
