package pl.edu.wat.wcy.isi.app.controller;

import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.isi.app.configuration.exception.ForbiddenException;
import pl.edu.wat.wcy.isi.app.configuration.exception.ResourceNotFoundException;
import pl.edu.wat.wcy.isi.app.configuration.exception.SizeException;
import pl.edu.wat.wcy.isi.app.dto.DataSeriesFileDTO;
import pl.edu.wat.wcy.isi.app.dto.message.response.ResponseMessage;
import pl.edu.wat.wcy.isi.app.mapper.DataSeriesFileMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.service.DataSeriesFileService;
import pl.edu.wat.wcy.isi.app.service.StorageService;
import pl.edu.wat.wcy.isi.app.service.UserService;

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
        DataSeriesFileDTO dataSeriesFileDTO;
        DataSeriesFileEntity dataSeriesFileEntity = new DataSeriesFileEntity();
        UserEntity userEntity = userService.getLoggedUser();

        dataSeriesFileEntity.setDateSent(new Timestamp(System.currentTimeMillis()));
        dataSeriesFileEntity.setDeleted((byte) 0);
        dataSeriesFileEntity.setName(dataSeriesFile.getOriginalFilename());
        dataSeriesFileEntity.setHashName(RandomString.make(100));
        dataSeriesFileEntity.setUser(userEntity);

        dataSeriesFileService.readMultipartFile(dataSeriesFile, dataSeriesFileEntity);
        dataSeriesFileService.propertiesCalculate(dataSeriesFileEntity);

        dataSeriesFileEntity = dataSeriesFileService.save(dataSeriesFileEntity);

        storageService.store(dataSeriesFile, dataSeriesFileEntity.getDataSeriesFileId() + DataSeriesFileService.FILE_EXTENSION);

        dataSeriesFileDTO = dataSeriesFileMapper.buildDataSeriesFileDTO(dataSeriesFileEntity);

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
