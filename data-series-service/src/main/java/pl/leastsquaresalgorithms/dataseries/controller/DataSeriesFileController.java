package pl.leastsquaresalgorithms.dataseries.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ForbiddenException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.SizeException;
import pl.leastsquaresalgorithms.dataseries.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.dataseries.dto.ResponseMessage;
import pl.leastsquaresalgorithms.dataseries.mapper.DataSeriesFileMapper;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;
import pl.leastsquaresalgorithms.dataseries.service.DataSeriesFileService;
import pl.leastsquaresalgorithms.dataseries.service.StorageService;

import java.util.List;

@RestController
@RequestMapping(value = "/data-series-file")
public class DataSeriesFileController {
    private final Logger logger = LoggerFactory.getLogger(DataSeriesFileController.class);
    private final StorageService storageService;
    private final DataSeriesFileService dataSeriesFileService;
    //    private final UserService userService;
    private final DataSeriesFileMapper dataSeriesFileMapper;

    public DataSeriesFileController(StorageService storageService, DataSeriesFileService dataSeriesFileService, DataSeriesFileMapper dataSeriesFileMapper) {
        this.storageService = storageService;
        this.dataSeriesFileService = dataSeriesFileService;
        this.dataSeriesFileMapper = dataSeriesFileMapper;
    }

    @GetMapping
    public ResponseEntity<List<DataSeriesFileDto>> getAll() {
//        UserEntity userEntity = userService.getLoggedUser();
        List<DataSeriesFileEntity> dataSeriesFileEntities = dataSeriesFileService.findAll();
        List<DataSeriesFileDto> dataSeriesFileDtos = dataSeriesFileMapper.buildDataSeriesFileDTOs(dataSeriesFileEntities);
        logger.debug("Getting all (for user) the files successfully completed. Size: {}", dataSeriesFileDtos.size());
        return ResponseEntity.ok(dataSeriesFileDtos);
    }

    @GetMapping("/{dataSeriesFileId}")
    public ResponseEntity<DataSeriesFileDto> getDataSeriesFile(@PathVariable Long dataSeriesFileId) throws ResourceNotFoundException {
//        UserEntity userEntity = userService.getLoggedUser();
        DataSeriesFileEntity dataSeriesFileEntity = dataSeriesFileService.findByIdWithPoints(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("DataSeriesFileEntity not found for this id: " + dataSeriesFileId));
        DataSeriesFileDto dataSeriesFileDTO = dataSeriesFileMapper.buildDataSeriesFileDTO(dataSeriesFileEntity);
        logger.trace("Getting file successfully completed. Size: {}", dataSeriesFileDTO);
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
        logger.debug("The file was successfully added.");
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
        logger.debug("Deleted data series file with id: {}", dataSeriesFileId);
        return new ResponseMessage("Deleted data series file with id: " + dataSeriesFileId);
    }
}
