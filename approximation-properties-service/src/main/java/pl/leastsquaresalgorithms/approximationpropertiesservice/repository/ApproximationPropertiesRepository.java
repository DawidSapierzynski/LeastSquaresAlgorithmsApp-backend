package pl.leastsquaresalgorithms.approximationpropertiesservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.leastsquaresalgorithms.approximationpropertiesservice.model.ApproximationPropertiesEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApproximationPropertiesRepository extends JpaRepository<ApproximationPropertiesEntity, BigInteger> {
    List<ApproximationPropertiesEntity> findByUserIdAndDeleted(BigInteger userId, byte deleted);

    Optional<ApproximationPropertiesEntity> findByApproximationPropertiesIdAndDeleted(BigInteger approximationPropertiesId, byte deleted);
}
