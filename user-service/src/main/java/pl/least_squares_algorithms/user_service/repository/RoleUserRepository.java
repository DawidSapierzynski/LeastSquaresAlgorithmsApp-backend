package pl.least_squares_algorithms.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.least_squares_algorithms.user_service.model.RoleUserEntity;

import java.util.Optional;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {
    Optional<RoleUserEntity> findByCode(String code);
}
