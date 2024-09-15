package pl.leastsquaresalgorithms.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.leastsquaresalgorithms.user.model.RoleUserToUserEntity;
import pl.leastsquaresalgorithms.user.model.UserEntity;

import java.util.List;

@Repository
public interface RoleUserToUserRepository extends JpaRepository<RoleUserToUserEntity, Long> {
    List<RoleUserToUserEntity> findByUserByUserId(UserEntity userEntity);

    void deleteByUserByUserId(UserEntity userEntity);
}