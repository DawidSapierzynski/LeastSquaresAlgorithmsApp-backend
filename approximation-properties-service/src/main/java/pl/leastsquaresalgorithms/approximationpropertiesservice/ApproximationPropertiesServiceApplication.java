package pl.leastsquaresalgorithms.approximationpropertiesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ApproximationPropertiesServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApproximationPropertiesServiceApplication.class, args);
    }

}
