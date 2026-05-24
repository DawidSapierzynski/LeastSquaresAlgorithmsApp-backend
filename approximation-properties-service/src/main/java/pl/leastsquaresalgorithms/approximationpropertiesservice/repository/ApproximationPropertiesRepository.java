package pl.leastsquaresalgorithms.approximationpropertiesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApproximationPropertiesRepository extends JpaRepository<ApproximationPropertiesEntity, Long> {
    List<ApproximationPropertiesEntity> findByUserIdAndDeleted(Long userId, Boolean deleted);

    Optional<ApproximationPropertiesEntity> findByApproximationPropertiesIdAndDeleted(Long approximationPropertiesId, Boolean deleted);
}
