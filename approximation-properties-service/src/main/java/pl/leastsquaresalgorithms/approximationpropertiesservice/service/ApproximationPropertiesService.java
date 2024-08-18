package pl.leastsquaresalgorithms.approximationpropertiesservice.service;

import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;
import pl.leastsquaresalgorithms.approximationpropertiesservice.repository.ApproximationPropertiesRepository;

import java.math.BigInteger;
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

    public List<ApproximationPropertiesEntity> findByUserAndDeleted(UserEntity userEntity, byte deleted) {
        return approximationPropertiesRepository.findByUserAndDeleted(userEntity, deleted);
    }

    public List<ApproximationPropertiesEntity> findAll() {
        return approximationPropertiesRepository.findAll();
    }

    public Optional<ApproximationPropertiesEntity> findById(BigInteger id) {
        return approximationPropertiesRepository.findById(id);
    }

    public Optional<ApproximationPropertiesEntity> findByIdAndDeleted(BigInteger id, byte deleted) {
        return approximationPropertiesRepository.findByApproximationPropertiesIdAndDeleted(id, deleted);
    }

    public void delete(ApproximationPropertiesEntity approximationProperties) {
        approximationProperties.setDeleted((byte) 1);
        save(approximationProperties);
    }

    public void delete(Collection<ApproximationPropertiesEntity> approximationPropertiesList) {
        approximationPropertiesList.forEach(this::delete);
    }
}
