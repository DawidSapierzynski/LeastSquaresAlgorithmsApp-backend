package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.util.List;

@Repository
public interface DataSeriesFileRepository extends JpaRepository<DataSeriesFileEntity, Long> {
    List<DataSeriesFileEntity> findByUserAndDeleted(UserEntity userEntity, byte deleted);
}
