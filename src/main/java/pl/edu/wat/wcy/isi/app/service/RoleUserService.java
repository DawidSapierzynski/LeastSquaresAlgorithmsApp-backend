package pl.edu.wat.wcy.isi.app.service;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserToUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.RoleUserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleUserService {
    private final RoleUserRepository roleUserRepository;
    private final RoleUserToUserService roleUserToUserService;

    public RoleUserService(RoleUserRepository roleUserRepository, RoleUserToUserService roleUserToUserService) {
        this.roleUserRepository = roleUserRepository;
        this.roleUserToUserService = roleUserToUserService;
    }

    public RoleUserEntity save(RoleUserEntity roleUserEntity) {
        return roleUserRepository.save(roleUserEntity);
    }

    public Optional<RoleUserEntity> findById(BigInteger id) {
        return roleUserRepository.findById(id);
    }

    public Optional<RoleUserEntity> findByCode(String code) {
        return roleUserRepository.findByCode(code);
    }

    public List<RoleUserEntity> getAll() {
        return roleUserRepository.findAll();
    }

    public List<RoleUserEntity> findByUser(UserEntity userEntity) {
        return roleUserToUserService.findByUser(userEntity).stream()
                .map(RoleUserToUserEntity::getRoleUserByRoleUserId)
                .collect(Collectors.toList());
    }
}
