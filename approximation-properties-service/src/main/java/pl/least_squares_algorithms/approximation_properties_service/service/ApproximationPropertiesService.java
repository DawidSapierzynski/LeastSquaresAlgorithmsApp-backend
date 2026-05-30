package pl.least_squares_algorithms.approximation_properties_service.service;

import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.approximation_properties_service.model.ApproximationPropertiesEntity;
import pl.least_squares_algorithms.approximation_properties_service.repository.ApproximationPropertiesRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ApproximationPropertiesService {
    private final ApproximationPropertiesRepository approximationPropertiesRepository;

    public ApproximationPropertiesService(ApproximationPropertiesRepository approximationPropertiesRepository) {
        this.approximationPropertiesRepository = approximationPropertiesRepository;
    }

    public ApproximationPropertiesEntity save(ApproximationPropertiesEntity approximationPropertiesEntity) {
        return approximationPropertiesRepository.save(approximationPropertiesEntity);
    }

    public List<ApproximationPropertiesEntity> findByUserAndDeleted(Long userId, Boolean deleted) {
        return approximationPropertiesRepository.findByUserIdAndDeleted(userId, deleted);
    }

    public List<ApproximationPropertiesEntity> findAll() {
        return approximationPropertiesRepository.findAll();
    }

    public Optional<ApproximationPropertiesEntity> findById(Long id) {
        return approximationPropertiesRepository.findById(id);
    }

    public Optional<ApproximationPropertiesEntity> findByIdAndDeleted(Long id, Boolean deleted) {
        return approximationPropertiesRepository.findByApproximationPropertiesIdAndDeleted(id, deleted);
    }

    public void delete(ApproximationPropertiesEntity approximationProperties) {
        approximationProperties.setDeleted(true);
        save(approximationProperties);
    }

    public void delete(Collection<ApproximationPropertiesEntity> approximationPropertiesList) {
        approximationPropertiesList.forEach(this::delete);
    }
}
