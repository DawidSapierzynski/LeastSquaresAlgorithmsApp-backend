package pl.leastsquaresalgorithms.dataseries.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.least_squares_algorithms.core.DistanceX;
import pl.least_squares_algorithms.core.WeightDistribution;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ForbiddenException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.SizeException;
import pl.leastsquaresalgorithms.dataseries.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.dataseries.dto.GenerateDataSeriesForm;
import pl.leastsquaresalgorithms.dataseries.dto.ResponseMessage;
import pl.leastsquaresalgorithms.dataseries.mapper.DataSeriesFileMapper;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;
import pl.leastsquaresalgorithms.dataseries.service.DataSeriesFileService;
import pl.leastsquaresalgorithms.dataseries.service.DataSeriesGenerator;
import pl.leastsquaresalgorithms.dataseries.service.StorageService;

import java.io.ByteArrayInputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/data-series-file")
@RequiredArgsConstructor
public class DataSeriesFileController {
    private final StorageService storageService;
    private final DataSeriesFileService dataSeriesFileService;
    //    private final UserService userService;
    private final DataSeriesFileMapper dataSeriesFileMapper;
    private final DataSeriesGenerator dataSeriesGenerator;


    @GetMapping
    public ResponseEntity<List<DataSeriesFileDto>> getAll() {
//        UserEntity userEntity = userService.getLoggedUser();
        List<DataSeriesFileEntity> dataSeriesFileEntities = dataSeriesFileService.findAll();
        List<DataSeriesFileDto> dataSeriesFileDtos = dataSeriesFileMapper.buildDataSeriesFileDTOs(dataSeriesFileEntities);
        log.debug("Getting all (for user) the files successfully completed. Size: {}", dataSeriesFileDtos.size());
        return ResponseEntity.ok(dataSeriesFileDtos);
    }

    @GetMapping("/{dataSeriesFileId}")
    public ResponseEntity<DataSeriesFileDto> getDataSeriesFile(@PathVariable Long dataSeriesFileId) throws ResourceNotFoundException {
//        UserEntity userEntity = userService.getLoggedUser();
        DataSeriesFileEntity dataSeriesFileEntity = dataSeriesFileService.findByIdWithPoints(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("DataSeriesFileEntity not found for this id: " + dataSeriesFileId));
        DataSeriesFileDto dataSeriesFileDTO = dataSeriesFileMapper.buildDataSeriesFileDTO(dataSeriesFileEntity);
        log.trace("Getting file successfully completed. Size: {}", dataSeriesFileDTO);
        return ResponseEntity.ok(dataSeriesFileDTO);
    }

    @Transactional
    @PostMapping
    public DataSeriesFileDto uploadDataSeriesFile(@RequestParam("dataSeriesFile") MultipartFile dataSeriesFile) throws SizeException {
        DataSeriesFileEntity dataSeriesFileEntity = dataSeriesFileService.buildEntity(dataSeriesFile);
        dataSeriesFileService.readMultipartFile(dataSeriesFile, dataSeriesFileEntity);
        dataSeriesFileService.propertiesCalculate(dataSeriesFileEntity);
        dataSeriesFileEntity = dataSeriesFileService.save(dataSeriesFileEntity);
        storageService.store(dataSeriesFile, dataSeriesFileEntity.getDataSeriesFileId() + DataSeriesFileService.FILE_EXTENSION);
        DataSeriesFileDto dataSeriesFileDTO = dataSeriesFileMapper.buildDataSeriesFileDTO(dataSeriesFileEntity);
        log.debug("The file was successfully added.");
        return dataSeriesFileDTO;
    }


    @Transactional
    @DeleteMapping("/{dataSeriesFileId}")
    public ResponseMessage deletedDataSeriesFile(@PathVariable Long dataSeriesFileId) throws ResourceNotFoundException, ForbiddenException {
        DataSeriesFileEntity dataSeriesFile = dataSeriesFileService.findById(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("DataSeriesFileEntity not found for this id: " + dataSeriesFileId));
//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(loggedUser.equals(dataSeriesFile.getUser()) || loggedUser.isAdmin())) {
//            throw new ForbiddenException("No permission to delete this data series file");
//        }
        this.dataSeriesFileService.delete(dataSeriesFile);
        this.storageService.deleteFile(dataSeriesFile.getDataSeriesFileId() + DataSeriesFileService.FILE_EXTENSION);
        log.debug("Deleted data series file with id: {}", dataSeriesFileId);
        return new ResponseMessage("Deleted data series file with id: " + dataSeriesFileId);
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<InputStreamResource> generateDataSeries(@RequestBody GenerateDataSeriesForm dataSeriesForm) {
        WeightDistribution weightDistribution = WeightDistribution.valueOf(dataSeriesForm.getWeightDistribution().toUpperCase());
        DistanceX distanceX = DistanceX.valueOf(dataSeriesForm.getDistanceX().toUpperCase());
        byte[] text = dataSeriesGenerator.generateDataSeries(distanceX, weightDistribution, dataSeriesForm.getMathematicalFunctionDTO(), dataSeriesForm.getNumberPoints(), dataSeriesForm.getNoise());
        log.debug("Generating data series completed successfully.");
        ContentDisposition disposition = ContentDisposition.attachment().filename("points.txt").build();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .headers(httpHeaders -> httpHeaders.setContentDisposition(disposition))
                .body(new InputStreamResource(new ByteArrayInputStream(text)));
    }
}
