package pl.leastsquaresalgorithms.approximationpropertiesservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ApproximationPropertiesDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApproximationPropertiesMapper {
    public List<ApproximationPropertiesDto> buildApproximationPropertiesDTOs(Collection<ApproximationPropertiesEntity> approximationProperties) {
        return approximationProperties.stream()
                .map(entity -> buildApproximationPropertiesDTO(entity, null))
                .collect(Collectors.toList());
    }

    public ApproximationPropertiesDto buildApproximationPropertiesDTO(ApproximationPropertiesEntity approximationProperties, DataSeriesFileDto dataSeriesFileDto) {
        return ApproximationPropertiesDto.builder()
                .id(approximationProperties.getApproximationPropertiesId())
                .userId(approximationProperties.getUserId())
                .dataSeriesFile(dataSeriesFileDto)
                .degree(approximationProperties.getDegreeApproximation())
                .dateCreate(approximationProperties.getDateCreate())
                .deleted(Boolean.TRUE.equals(approximationProperties.getDeleted()))
                .build();
    }
}
