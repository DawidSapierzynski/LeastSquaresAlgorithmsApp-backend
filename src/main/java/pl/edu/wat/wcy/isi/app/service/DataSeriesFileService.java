package pl.edu.wat.wcy.isi.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.isi.app.configuration.FileStorageProperties;
import pl.edu.wat.wcy.isi.app.configuration.exception.SizeException;
import pl.edu.wat.wcy.isi.app.core.calculate.*;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.DataSeriesFileRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class DataSeriesFileService {
    public static final String FILE_EXTENSION = ".csv";
    private static final int MAX_NUMBER_POINTS = 1000;
    private static final int MIN_NUMBER_POINTS = 5;
    private static final Logger logger = LoggerFactory.getLogger(DataSeriesFileService.class);

    private final ExecutorService threadPool;
    private final FileStorageProperties fileStorageProperties;
    private final DataSeriesFileRepository dataSeriesFileRepository;
    private final ApproximationPropertiesService approximationPropertiesService;

    public DataSeriesFileService(@Value("${number.threads}") int nThreads, FileStorageProperties fileStorageProperties, DataSeriesFileRepository dataSeriesFileRepository, ApproximationPropertiesService approximationPropertiesService) {
        this.dataSeriesFileRepository = dataSeriesFileRepository;
        this.approximationPropertiesService = approximationPropertiesService;
        this.threadPool = Executors.newFixedThreadPool(nThreads);
        this.fileStorageProperties = fileStorageProperties;
    }

    public void readFile(Long dateSeriesFileId, DataSeriesFileEntity dataSeriesFileEntity) throws SizeException {
        List<Callable<Object>> callables = Collections.singletonList(Executors.callable(new ReadSeriesDatesFromFile(dateSeriesFileId.toString() + DataSeriesFileService.FILE_EXTENSION, dataSeriesFileEntity, fileStorageProperties)));
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            logger.debug("ReadSeriesDatesFromFile - isDone: {}", futures.get(0).isDone());
            if (dataSeriesFileEntity.getPoints().size() + dataSeriesFileEntity.getArtefacts().size() < MIN_NUMBER_POINTS) {
                throw new SizeException("Data series is empty or the file has been deleted.");
            }
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public void readMultipartFile(MultipartFile dataSeriesMultipartFile, DataSeriesFileEntity dataSeriesFileEntity) throws SizeException {
        List<Callable<Object>> callables = Collections.singletonList(Executors.callable(new ReadSeriesDatesFromMultipartFile(dataSeriesFileEntity, dataSeriesMultipartFile)));
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            logger.debug("ReadSeriesDatesFromMultipartFile - isDone: {}", futures.get(0).isDone());
            if (!ReadSeriesDates.checkPoints(dataSeriesFileEntity.getPoints())) {
                throw new SizeException("The series contains duplicate x");
            }
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
        }
    }

    private void checkSize(int size) throws SizeException {
        if (size < MIN_NUMBER_POINTS) {
            throw new SizeException("The number of points is less than " + MIN_NUMBER_POINTS);
        } else if (size > MAX_NUMBER_POINTS) {
            throw new SizeException("The number of points is greater than " + MAX_NUMBER_POINTS);
        }
    }

    public void propertiesCalculate(DataSeriesFileEntity dataSeriesFile) throws SizeException {
        checkSize(dataSeriesFile.getSize());

        List<Callable<Object>> callables = Arrays.asList(Executors.callable(new VarianceCalculate(dataSeriesFile)),
                Executors.callable(new PeriodicityPolynomialCalculate(dataSeriesFile))
        );
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            logger.debug("VarianceCalculate - isDone: {}", futures.get(0).isDone());
            logger.debug("PeriodicityPolynomialCalculate - isDone: {}", futures.get(1).isDone());
            logger.debug("PeriodicityTrigonometricCalculate - isDone: {}", futures.get(2).isDone());

            dataSeriesFile.setPeriodicity(dataSeriesFile.getErrorTrigonometric() < dataSeriesFile.getErrorPolynomial() ? (byte) 1 : (byte) 0);
            logger.info("Set periodicity: {}", dataSeriesFile.getPeriodicity());
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public Optional<DataSeriesFileEntity> findById(Long id) {
        return dataSeriesFileRepository.findById(id);
    }

    public Optional<DataSeriesFileEntity> findByIdWithPoints(Long id) {
        Optional<DataSeriesFileEntity> dataSeriesFileOptional = dataSeriesFileRepository.findById(id);

        dataSeriesFileOptional.ifPresent(dataSeriesFileEntity -> {
            try {
                this.readFile(dataSeriesFileEntity.getDataSeriesFileId(), dataSeriesFileEntity);
            } catch (SizeException e) {
                logger.error(e.getMessage(), e);
            }
        });

        return dataSeriesFileOptional;
    }


    public DataSeriesFileEntity save(DataSeriesFileEntity dataSeriesFileEntity) {
        return dataSeriesFileRepository.save(dataSeriesFileEntity);
    }

    public void delete(DataSeriesFileEntity dataSeriesFileEntity) {
        dataSeriesFileEntity.setDeleted((byte) 1);
        approximationPropertiesService.delete(dataSeriesFileEntity.getApproximationProperties());
        save(dataSeriesFileEntity);
    }

    public List<DataSeriesFileEntity> findAll() {
        return dataSeriesFileRepository.findAll();
    }

    public List<DataSeriesFileEntity> findByUserAndDeleted(UserEntity userEntity, byte deleted) {
        return dataSeriesFileRepository.findByUserAndDeleted(userEntity, deleted);
    }
}
