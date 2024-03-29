package pl.edu.wat.wcy.isi.app.service;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.model.entityModels.ApproximationPropertiesEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.ApproximationPropertiesRepository;

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

    public Optional<ApproximationPropertiesEntity> findById(long id) {
        return approximationPropertiesRepository.findById(id);
    }

    public Optional<ApproximationPropertiesEntity> findByIdAndDeleted(long id, byte deleted) {
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
