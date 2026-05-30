package pl.least_squares_algorithms.approximation_properties_service.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import pl.least_squares_algorithms.approximation_properties_service.dto.DataSeriesFileDto;

public interface DataSeriesFileClient {

    @GetExchange("/data-series-file/{dataSeriesFileId}")
    DataSeriesFileDto getDataSeriesFile(@PathVariable Long dataSeriesFileId);

}
