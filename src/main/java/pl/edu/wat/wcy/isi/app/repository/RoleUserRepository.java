package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserEntity;

import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {
    Optional<RoleUserEntity> findByCode(String code);
}
