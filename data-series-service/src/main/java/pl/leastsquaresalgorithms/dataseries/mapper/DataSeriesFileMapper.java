package pl.leastsquaresalgorithms.dataseries.mapper;

import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.dataseries.dto.DataSeriesFileDTO;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSeriesFileMapper {

    public List<DataSeriesFileDTO> buildDataSeriesFileDTOs(List<DataSeriesFileEntity> dataSeriesFiles) {
        return dataSeriesFiles.stream()
                .map(this::buildDataSeriesFileDTO)
                .collect(Collectors.toList());
    }

    public DataSeriesFileDTO buildDataSeriesFileDTO(DataSeriesFileEntity dataSeriesFile) {
        return DataSeriesFileDTO.builder()
                .id(dataSeriesFile.getDataSeriesFileId())
                .userId(dataSeriesFile.getUser().getUserId())
                .name(dataSeriesFile.getName())
                .hashName(dataSeriesFile.getHashName())
                .dateSent(dataSeriesFile.getDateSent())
                .deleted(dataSeriesFile.getDeleted().equals((byte) 1))
                .size(dataSeriesFile.getSize())
                .variance(dataSeriesFile.getVariance())
                .standardDeviation(dataSeriesFile.getStandardDeviation())
                .points(dataSeriesFile.getPoints())
                .build();
    }
}
