package pl.edu.wat.wcy.isi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.edu.wat.wcy.isi.app.configuration.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
public class LeastSquaresAlgorithmsBackend {

    public static void main(String[] args) {
        SpringApplication.run(LeastSquaresAlgorithmsBackend.class, args);
    }

}
