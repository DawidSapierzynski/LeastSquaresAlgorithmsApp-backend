package pl.edu.wat.wcy.isi.app.service;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserToUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.RoleUserToUserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class RoleUserToUserService {
    private final RoleUserToUserRepository roleUserToUserRepository;

    public RoleUserToUserService(RoleUserToUserRepository roleUserToUserRepository) {
        this.roleUserToUserRepository = roleUserToUserRepository;
    }

    public void addRoleToUser(UserEntity userEntity, List<RoleUserEntity> roleUserEntities) {
        for (RoleUserEntity r : roleUserEntities) {
            RoleUserToUserEntity roleUserToUser = new RoleUserToUserEntity();
            roleUserToUser.setUserByUserId(userEntity);
            roleUserToUser.setRoleUserByRoleUserId(r);

            save(roleUserToUser);
        }
    }

    public RoleUserToUserEntity save(RoleUserToUserEntity roleUserToUserEntity) {
        return roleUserToUserRepository.save(roleUserToUserEntity);
    }

    public Optional<RoleUserToUserEntity> findById(BigInteger id) {
        return roleUserToUserRepository.findById(id);
    }

    public List<RoleUserToUserEntity> findByUser(UserEntity userEntity) {
        return roleUserToUserRepository.findByUserByUserId(userEntity);
    }

    public void deleteByUser(UserEntity userEntity) {
        roleUserToUserRepository.deleteByUserByUserId(userEntity);
    }

}
