package pl.least_squares_algorithms.approximation_properties_service.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pl.least_squares_algorithms.approximation_properties_service.client.DataSeriesFileClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    @Value("${dataSeriesFile.service.url:http://localhost:8080/api}")
    private String dataSeriesFileServiceUrl;

    @Bean
    public DataSeriesFileClient dataSeriesFileClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(dataSeriesFileServiceUrl)
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return httpServiceProxyFactory.createClient(DataSeriesFileClient.class);
    }
}
