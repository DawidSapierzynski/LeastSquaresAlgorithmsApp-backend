package pl.leastsquaresalgorithms.dataseries.mapper;

import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.dataseries.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSeriesFileMapper {

    public List<DataSeriesFileDto> buildDataSeriesFileDTOs(List<DataSeriesFileEntity> dataSeriesFiles) {
        return dataSeriesFiles.stream()
                .map(this::buildDataSeriesFileDTO)
                .collect(Collectors.toList());
    }

    public DataSeriesFileDto buildDataSeriesFileDTO(DataSeriesFileEntity dataSeriesFile) {
        return DataSeriesFileDto.builder()
                .id(dataSeriesFile.getDataSeriesFileId())
                .userId(dataSeriesFile.getUserId())
                .name(dataSeriesFile.getName())
                .hashName(dataSeriesFile.getHashName())
                .dateSent(dataSeriesFile.getDateSent())
                .deleted(Boolean.TRUE.equals(dataSeriesFile.getDeleted()))
                .size(dataSeriesFile.getSize())
                .variance(dataSeriesFile.getVariance())
                .standardDeviation(dataSeriesFile.getStandardDeviation())
                .points(dataSeriesFile.getPoints())
                .build();
    }
}
