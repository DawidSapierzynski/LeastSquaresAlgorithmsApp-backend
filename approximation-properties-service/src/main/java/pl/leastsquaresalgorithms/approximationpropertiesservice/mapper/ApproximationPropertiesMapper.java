package pl.leastsquaresalgorithms.approximationpropertiesservice.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ApproximationPropertiesDTO;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ApproximationPropertiesMapper {
    public List<ApproximationPropertiesDTO> buildApproximationPropertiesDTOs(Collection<ApproximationPropertiesEntity> approximationProperties) {
        return approximationProperties.stream()
                .map(this::buildApproximationPropertiesDTO)
                .collect(Collectors.toList());
    }

    public ApproximationPropertiesDTO buildApproximationPropertiesDTO(ApproximationPropertiesEntity approximationProperties) {
        return ApproximationPropertiesDTO.builder()
                .id(approximationProperties.getApproximationPropertiesId())
                .userId(approximationProperties.getUserId())
                .dataSeriesFileId(approximationProperties.getDataSeriesFileId())
                .degree(approximationProperties.getDegreeApproximation())
                .dateCreate(approximationProperties.getDateCreate())
                .deleted(approximationProperties.getDeleted().equals((byte) 1))
                .build();
    }
}
