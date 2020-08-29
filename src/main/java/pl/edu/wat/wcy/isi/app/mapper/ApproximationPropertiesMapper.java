package pl.edu.wat.wcy.isi.app.mapper;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.dto.ApproximationPropertiesDTO;
import pl.edu.wat.wcy.isi.app.model.entityModels.ApproximationPropertiesEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApproximationPropertiesMapper {
    private final DataSeriesFileMapper dataSeriesFileMapper;

    public ApproximationPropertiesMapper(DataSeriesFileMapper dataSeriesFileMapper) {
        this.dataSeriesFileMapper = dataSeriesFileMapper;
    }

    public List<ApproximationPropertiesDTO> bulidApproximationPropertiesDTOs(Collection<ApproximationPropertiesEntity> approximationProperties) {
        return approximationProperties.stream()
                .map(this::bulidApproximationPropertiesDTO)
                .collect(Collectors.toList());
    }

    public ApproximationPropertiesDTO bulidApproximationPropertiesDTO(ApproximationPropertiesEntity approximationProperties) {
        return ApproximationPropertiesDTO.builder()
                .id(approximationProperties.getApproximationPropertiesId())
                .userId(approximationProperties.getUser().getUserId())
                .dataSeriesFileId(approximationProperties.getDataSeriesFile().getDataSeriesFileId())
                .degree(approximationProperties.getPrecisionApproximation())
                .dateCreate(approximationProperties.getDateCreate())
                .deleted(approximationProperties.getDeleted().equals((byte) 1))
                .dataSeriesFileDTO(dataSeriesFileMapper.buildDataSeriesFileDTO(approximationProperties.getDataSeriesFile()))
                .build();
    }
}
