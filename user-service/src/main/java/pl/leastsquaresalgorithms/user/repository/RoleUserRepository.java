package pl.leastsquaresalgorithms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.leastsquaresalgorithms.user.model.RoleUserEntity;

import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {
    Optional<RoleUserEntity> findByCode(String code);
}
