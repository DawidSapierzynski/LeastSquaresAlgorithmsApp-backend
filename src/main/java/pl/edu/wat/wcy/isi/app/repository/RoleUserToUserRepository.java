package pl.edu.wat.wcy.isi.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserToUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface RoleUserToUserRepository extends JpaRepository<RoleUserToUserEntity, BigInteger> {
    List<RoleUserToUserEntity> findByUserByUserId(UserEntity userEntity);

    void deleteByUserByUserId(UserEntity userEntity);
}