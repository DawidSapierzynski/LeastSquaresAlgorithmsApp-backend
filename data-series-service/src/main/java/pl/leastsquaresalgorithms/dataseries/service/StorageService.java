package pl.leastsquaresalgorithms.dataseries.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.wat.wcy.isi.app.configuration.FileStorageProperties;
import pl.edu.wat.wcy.isi.app.configuration.exception.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService {
    private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

    private final Path fileStorageLocation;

    public StorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
    }

    public void store(MultipartFile file, String fileName) {
        Path filePath = fileStorageLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            logger.error("{}", e.getMessage());
        }
    }

    public File loadFile(String fileName) throws ResourceNotFoundException {
        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
        File resource = new File(filePath.toUri());
        if (resource.exists() || resource.canRead()) {
            return resource;
        } else {
            throw new ResourceNotFoundException("The file could not be downloaded because it does not exist.");
        }
    }

    public void deleteAll() {
        FileSystemUtils.deleteRecursively(fileStorageLocation.toFile());
    }

    public void deleteFile(String fileName) {
        FileSystemUtils.deleteRecursively(fileStorageLocation.resolve(fileName).toFile());
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(fileStorageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage.", e);
        }
    }
}
