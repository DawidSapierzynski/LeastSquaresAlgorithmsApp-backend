package pl.leastsquaresalgorithms.dataseries.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface DataSeriesFileRepository extends JpaRepository<DataSeriesFileEntity, Long> {
    List<DataSeriesFileEntity> findByUserIdAndDeleted(Long userId, Boolean deleted);
}
