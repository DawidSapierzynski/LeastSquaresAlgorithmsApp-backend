package pl.leastsquaresalgorithms.approximationpropertiesservice.mapper;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApproximationPropertiesMapper {
    private final DataSeriesFileMapper dataSeriesFileMapper;

    public ApproximationPropertiesMapper(DataSeriesFileMapper dataSeriesFileMapper) {
        this.dataSeriesFileMapper = dataSeriesFileMapper;
    }

    public List<ApproximationPropertiesDTO> buildApproximationPropertiesDTOs(Collection<ApproximationPropertiesEntity> approximationProperties) {
        return approximationProperties.stream()
                .map(this::buildApproximationPropertiesDTO)
                .collect(Collectors.toList());
    }

    public ApproximationPropertiesDTO buildApproximationPropertiesDTO(ApproximationPropertiesEntity approximationProperties) {
        return ApproximationPropertiesDTO.builder()
                .id(approximationProperties.getApproximationPropertiesId())
                .userId(approximationProperties.getUser().getUserId())
                .dataSeriesFileId(approximationProperties.getDataSeriesFile().getDataSeriesFileId())
                .degree(approximationProperties.getDegreeApproximation())
                .dateCreate(approximationProperties.getDateCreate())
                .deleted(approximationProperties.getDeleted().equals((byte) 1))
                .dataSeriesFileDTO(dataSeriesFileMapper.buildDataSeriesFileDTO(approximationProperties.getDataSeriesFile()))
                .build();
    }
}
