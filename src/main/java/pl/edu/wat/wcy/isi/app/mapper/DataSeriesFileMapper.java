package pl.edu.wat.wcy.isi.app.mapper;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.dto.DataSeriesFileDTO;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;

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
                .errorPolynomial(dataSeriesFile.getErrorPolynomial())
                .errorTrigonometric(dataSeriesFile.getErrorTrigonometric())
                .periodicity(dataSeriesFile.getPeriodicity().equals((byte) 1))
                .variance(dataSeriesFile.getVariance())
                .standardDeviation(dataSeriesFile.getStandardDeviation())
                .points(dataSeriesFile.getPoints())
                .artefacts(dataSeriesFile.getArtefacts())
                .build();
    }
}
