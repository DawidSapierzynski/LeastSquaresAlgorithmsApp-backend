package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.ApproximationPropertiesEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApproximationPropertiesRepository extends JpaRepository<ApproximationPropertiesEntity, Long> {
    List<ApproximationPropertiesEntity> findByUserAndDeleted(UserEntity userEntity, byte deleted);

    Optional<ApproximationPropertiesEntity> findByApproximationPropertiesIdAndDeleted(long userEntity, byte deleted);
}
