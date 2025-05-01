package pl.leastsquaresalgorithms.dataseries;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.leastsquaresalgorithms.dataseries.configuration.FileStorageProperties;

@EnableConfigurationProperties(FileStorageProperties.class)
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class DataSeriesApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataSeriesApplication.class, args);
    }

}
