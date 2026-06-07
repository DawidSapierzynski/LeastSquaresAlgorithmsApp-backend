package pl.least_squares_algorithms.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.least_squares_algorithms.user_service.model.RoleUserToUserEntity;
import pl.least_squares_algorithms.user_service.model.UserEntity;

import java.util.List;

@Repository
public interface RoleUserToUserRepository extends JpaRepository<RoleUserToUserEntity, Long> {
    List<RoleUserToUserEntity> findByUserByUserId(UserEntity userEntity);

    void deleteByUserByUserId(UserEntity userEntity);
}