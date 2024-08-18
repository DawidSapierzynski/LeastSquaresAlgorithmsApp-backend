package pl.leastsquaresalgorithms.approximationpropertiesservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ApproximationPropertiesDTO;
import pl.leastsquaresalgorithms.approximationpropertiesservice.mapper.ApproximationPropertiesMapper;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;
import pl.leastsquaresalgorithms.approximationpropertiesservice.service.ApproximationPropertiesService;


import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

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
    public ResponseEntity<ApproximationPropertiesDTO> postApproximationProperties(@RequestParam("dataSeriesFileId") BigInteger dataSeriesFileId, @RequestParam("degree") int degree) throws ResourceNotFoundException {

        DataSeriesFileEntity dataSeriesFileEntity = dataSeriesFileService.findById(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("Data series file not found for this id: " + dataSeriesFileId));
        UserEntity userEntity = userService.getLoggedUser();

        ApproximationPropertiesEntity approximationProperties = new ApproximationPropertiesEntity();
        approximationProperties.setDataSeriesFile(dataSeriesFileEntity);
        approximationProperties.setDeleted((byte) 0);
        approximationProperties.setUser(userEntity);
        approximationProperties.setDegreeApproximation(degree);
        approximationProperties.setDateCreate(new Timestamp(System.currentTimeMillis()));

        approximationProperties = approximationPropertiesService.save(approximationProperties);

        logger.debug("Adding approximation properties completed successfully.");
        return new ResponseEntity<>(approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ApproximationPropertiesDTO>> getApproximationProperties() {
        UserEntity userEntity = userService.getLoggedUser();

        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findByUserAndDeleted(userEntity, (byte) 0);
        List<ApproximationPropertiesDTO> approximationPropertiesDTOS = approximationPropertiesMapper.buildApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.debug("Getting all (for user) the approximation properties successfully completed. Size: {}", approximationPropertiesDTOS.size());
        return ResponseEntity.ok(approximationPropertiesDTOS);
    }

    @GetMapping(produces = "application/json", value = "/all")
    public ResponseEntity<List<ApproximationPropertiesDTO>> getAllApproximationProperties() {
        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findAll();
        List<ApproximationPropertiesDTO> approximationPropertiesDTOs = approximationPropertiesMapper.buildApproximationPropertiesDTOs(approximationPropertiesEntities);

        logger.debug("Getting all the approximation properties successfully completed. Size: {}", approximationPropertiesDTOs.size());
        return ResponseEntity.ok(approximationPropertiesDTOs);
    }

    @GetMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ApproximationPropertiesDTO> getApproximationProperties(@PathVariable(value = "approximationPropertiesId") BigInteger approximationPropertiesId) throws ResourceNotFoundException, ForbiddenException, SizeException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findByIdAndDeleted(approximationPropertiesId, (byte) 0)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

        UserEntity loggedUser = userService.getLoggedUser();
        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to open this approximation properties");
        }

        dataSeriesFileService.readFile(approximationProperties.getDataSeriesFile().getDataSeriesFileId(), approximationProperties.getDataSeriesFile());

        logger.debug("Get approximation properties successfully completed. Id: {}", approximationPropertiesId);
        return ResponseEntity.ok(approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties));
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseEntity<ResponseMessage> deletedApproximationProperties(@PathVariable(value = "approximationPropertiesId") BigInteger approximationPropertiesId) throws ResourceNotFoundException, ForbiddenException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findById(approximationPropertiesId)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

        UserEntity loggedUser = userService.getLoggedUser();
        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to delete this approximation properties");
        }

        this.approximationPropertiesService.delete(approximationProperties);

        logger.debug("Deleted approximation properties with id: {}", approximationPropertiesId);
        return ResponseEntity.ok(new ResponseMessage("Deleted approximation properties with id: " + approximationPropertiesId));
    }
}
