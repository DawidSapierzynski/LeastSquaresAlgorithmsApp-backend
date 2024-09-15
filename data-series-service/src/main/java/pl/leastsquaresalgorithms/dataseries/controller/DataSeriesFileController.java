package pl.leastsquaresalgorithms.dataseries.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ForbiddenException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.dataseries.configuration.exception.SizeException;
import pl.leastsquaresalgorithms.dataseries.dto.DataSeriesFileDTO;
import pl.leastsquaresalgorithms.dataseries.mapper.DataSeriesFileMapper;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;
import pl.leastsquaresalgorithms.dataseries.service.DataSeriesFileService;
import pl.leastsquaresalgorithms.dataseries.service.StorageService;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/dataSeriesFile")
public class DataSeriesFileController {
    private final Logger logger = LoggerFactory.getLogger(DataSeriesFileController.class);
    private final StorageService storageService;
    private final DataSeriesFileService dataSeriesFileService;
    private final UserService userService;
    private final DataSeriesFileMapper dataSeriesFileMapper;

    public DataSeriesFileController(StorageService storageService, DataSeriesFileService dataSeriesFileService, UserService userService, DataSeriesFileMapper dataSeriesFileMapper) {
        this.storageService = storageService;
        this.dataSeriesFileService = dataSeriesFileService;
        this.userService = userService;
        this.dataSeriesFileMapper = dataSeriesFileMapper;
    }

    @Transactional
    @PostMapping(produces = "application/json")
    public ResponseEntity<DataSeriesFileDTO> uploadFile(@RequestParam("dataSeriesFile") MultipartFile dataSeriesFile) throws SizeException {
        UserEntity userEntity = userService.getLoggedUser();
        DataSeriesFileEntity dataSeriesFileEntity = new DataSeriesFileEntity();
        dataSeriesFileEntity.setDateSent(new Timestamp(System.currentTimeMillis()));
        dataSeriesFileEntity.setDeleted((byte) 0);
        dataSeriesFileEntity.setName(dataSeriesFile.getOriginalFilename());
        dataSeriesFileEntity.setHashName(RandomStringUtils.random(100));
        dataSeriesFileEntity.setUser(userEntity);

        dataSeriesFileService.readMultipartFile(dataSeriesFile, dataSeriesFileEntity);
        dataSeriesFileService.propertiesCalculate(dataSeriesFileEntity);

        dataSeriesFileEntity = dataSeriesFileService.save(dataSeriesFileEntity);
        storageService.store(dataSeriesFile, dataSeriesFileEntity.getDataSeriesFileId() + DataSeriesFileService.FILE_EXTENSION);

        DataSeriesFileDTO dataSeriesFileDTO = dataSeriesFileMapper.buildDataSeriesFileDTO(dataSeriesFileEntity);
        logger.debug("The file was successfully added.");
        return new ResponseEntity<>(dataSeriesFileDTO, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json", value = "/all")
    public ResponseEntity<List<DataSeriesFileDTO>> getAll() {
        List<DataSeriesFileEntity> dataSeriesFileEntities = dataSeriesFileService.findAll();
        List<DataSeriesFileDTO> dataSeriesFileDTOs = dataSeriesFileMapper.buildDataSeriesFileDTOs(dataSeriesFileEntities);

        logger.debug("Getting all the files successfully completed. Size: {}", dataSeriesFileDTOs.size());
        return new ResponseEntity<>(dataSeriesFileDTOs, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<DataSeriesFileDTO>> getAllForUser() {
        UserEntity userEntity = userService.getLoggedUser();
        List<DataSeriesFileEntity> dataSeriesFileEntities = dataSeriesFileService.findByUserAndDeleted(userEntity, (byte) 0);
        List<DataSeriesFileDTO> dataSeriesFileDTOs = dataSeriesFileMapper.buildDataSeriesFileDTOs(dataSeriesFileEntities);

        logger.debug("Getting all (for user) the files successfully completed. Size: {}", dataSeriesFileDTOs.size());
        return new ResponseEntity<>(dataSeriesFileDTOs, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{dataSeriesFileId}")
    public ResponseEntity<ResponseMessage> deletedDataSeriesFile(@PathVariable(value = "dataSeriesFileId") BigInteger dataSeriesFileId) throws ResourceNotFoundException, ForbiddenException {
        DataSeriesFileEntity dataSeriesFile = dataSeriesFileService.findById(dataSeriesFileId)
                .orElseThrow(() -> new ResourceNotFoundException("DataSeriesFileEntity not found for this id: " + dataSeriesFileId));
        UserEntity loggedUser = userService.getLoggedUser();
        if (!(loggedUser.equals(dataSeriesFile.getUser()) || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to delete this data series file");
        }
        this.dataSeriesFileService.delete(dataSeriesFile);
        this.storageService.deleteFile(dataSeriesFile.getDataSeriesFileId() + DataSeriesFileService.FILE_EXTENSION);

        logger.debug("Deleted data series file with id: {}", dataSeriesFileId);
        return ResponseEntity.ok(new ResponseMessage("Deleted data series file with id: " + dataSeriesFileId));
    }
}
