package pl.edu.wat.wcy.isi.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pl.edu.wat.wcy.isi.app.configuration.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableJpaRepositories(basePackages = "pl.edu.wat.wcy.isi.app.repository")
@EnableTransactionManagement
@EntityScan(basePackages = "pl.edu.wat.wcy.isi.app.model.entityModels")
public class LeastSquaresAlgorithmsBackend {

    public static void main(String[] args) {
        SpringApplication.run(LeastSquaresAlgorithmsBackend.class, args);
    }

}
