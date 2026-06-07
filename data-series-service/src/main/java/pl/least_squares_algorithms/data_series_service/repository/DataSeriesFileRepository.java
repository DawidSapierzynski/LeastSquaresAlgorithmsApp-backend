package pl.least_squares_algorithms.data_series_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.least_squares_algorithms.data_series_service.model.DataSeriesFileEntity;

import java.util.List;

@Repository
public interface DataSeriesFileRepository extends JpaRepository<DataSeriesFileEntity, Long> {
    List<DataSeriesFileEntity> findByUserIdAndDeleted(Long userId, Boolean deleted);
}
