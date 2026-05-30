package pl.least_squares_algorithms.approximation_properties_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.least_squares_algorithms.approximation_properties_service.client.DataSeriesFileClient;
import pl.least_squares_algorithms.approximation_properties_service.configuration.exception.ResourceNotFoundException;
import pl.least_squares_algorithms.approximation_properties_service.dto.ApproximationPropertiesDto;
import pl.least_squares_algorithms.approximation_properties_service.dto.DataSeriesFileDto;
import pl.least_squares_algorithms.approximation_properties_service.dto.ResponseMessage;
import pl.least_squares_algorithms.approximation_properties_service.mapper.ApproximationPropertiesMapper;
import pl.least_squares_algorithms.approximation_properties_service.model.ApproximationPropertiesEntity;
import pl.least_squares_algorithms.approximation_properties_service.service.ApproximationPropertiesService;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/approximation-properties")
public class ApproximationPropertiesController {
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

    @GetMapping(produces = "application/json")
    public List<ApproximationPropertiesDto> getApproximationProperties() {
//        UserEntity userEntity = userService.getLoggedUser();

        List<ApproximationPropertiesEntity> approximationPropertiesEntities = approximationPropertiesService.findByUserAndDeleted(1L, false);
        List<ApproximationPropertiesDto> approximationPropertiesDtos = approximationPropertiesMapper.buildApproximationPropertiesDTOs(approximationPropertiesEntities);

        log.debug("Getting all (for user) the approximation properties successfully completed. Size: {}", approximationPropertiesDtos.size());
        return approximationPropertiesDtos;
    }

    @GetMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ApproximationPropertiesDto getApproximationProperties(@PathVariable Long approximationPropertiesId) throws ResourceNotFoundException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findByIdAndDeleted(approximationPropertiesId, false)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
//            throw new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId);
//        }

        DataSeriesFileDto dataSeriesFileEntity = dataSeriesFileClient.getDataSeriesFile(approximationProperties.getDataSeriesFileId());
        log.debug("Get approximation properties successfully completed. Id: {}", approximationPropertiesId);
        return approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties, dataSeriesFileEntity);
    }

    @Transactional
    @PostMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ApproximationPropertiesDto createApproximationProperties(@RequestParam("dataSeriesFileId") Long dataSeriesFileId, @RequestParam("degree") int degree) throws ResourceNotFoundException {
        if (dataSeriesFileId.compareTo(1L) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data series file id must be positive integer");
        }
        if (degree < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Degree must be positive integer");
        }
        DataSeriesFileDto dataSeriesFileEntity = dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId);
//                .orElseThrow(() -> new ResourceNotFoundException("Data series file not found for this id: " + dataSeriesFileId));
//        UserEntity userEntity = userService.getLoggedUser();

        ApproximationPropertiesEntity approximationProperties = new ApproximationPropertiesEntity();
        approximationProperties.setDataSeriesFileId(dataSeriesFileId);
        approximationProperties.setDeleted(Boolean.FALSE);
        approximationProperties.setUserId(1L);
        approximationProperties.setDegreeApproximation(degree);
        approximationProperties.setDateCreate(new Timestamp(System.currentTimeMillis()));

        approximationProperties = approximationPropertiesService.save(approximationProperties);

        log.debug("Adding approximation properties completed successfully.");
        return approximationPropertiesMapper.buildApproximationPropertiesDTO(approximationProperties, dataSeriesFileEntity);
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{approximationPropertiesId}")
    public ResponseMessage deletedApproximationProperties(@PathVariable Long approximationPropertiesId) throws ResourceNotFoundException {
        ApproximationPropertiesEntity approximationProperties = approximationPropertiesService.findById(approximationPropertiesId)
                .orElseThrow(() -> new ResourceNotFoundException("Approximation properties not found for this id: " + approximationPropertiesId));

//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(loggedUser.equals(approximationProperties.getUser()) || loggedUser.isAdmin())) {
//            throw new ForbiddenException("No permission to delete this approximation properties");
//        }

        this.approximationPropertiesService.delete(approximationProperties);
        log.debug("Deleted approximation properties with id: {}", approximationPropertiesId);
        return new ResponseMessage("Deleted approximation properties with id: " + approximationPropertiesId);
    }
}
