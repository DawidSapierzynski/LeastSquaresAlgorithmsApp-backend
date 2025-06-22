package pl.leastsquaresalgorithms.approximationpropertiesservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.DataSeriesFileDto;

import java.math.BigInteger;

public interface DataSeriesFileClient {

    @GetExchange("/data-series-file/{dataSeriesFileId}")
    DataSeriesFileDto getDataSeriesFile(@PathVariable(value = "dataSeriesFileId") BigInteger dataSeriesFileId);

}
