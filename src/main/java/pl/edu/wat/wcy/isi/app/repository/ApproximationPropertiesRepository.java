package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.ApproximationPropertiesEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApproximationPropertiesRepository extends JpaRepository<ApproximationPropertiesEntity, BigInteger> {
    List<ApproximationPropertiesEntity> findByUserAndDeleted(UserEntity userEntity, byte deleted);

    Optional<ApproximationPropertiesEntity> findByApproximationPropertiesIdAndDeleted(BigInteger userEntity, byte deleted);
}
