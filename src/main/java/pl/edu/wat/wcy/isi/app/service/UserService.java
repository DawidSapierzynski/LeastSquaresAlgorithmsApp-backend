package pl.edu.wat.wcy.isi.app.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.configuration.security.UserPrinciple;
import pl.edu.wat.wcy.isi.app.dto.UserDTO;
import pl.edu.wat.wcy.isi.app.mapper.RoleUserMapper;
import pl.edu.wat.wcy.isi.app.mapper.UserMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserEntity;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.repository.UserRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleUserToUserService roleUserToUserService;
    private final RoleUserMapper roleUserMapper;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, RoleUserToUserService roleUserToUserService, RoleUserMapper roleUserMapper, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleUserToUserService = roleUserToUserService;
        this.roleUserMapper = roleUserMapper;
        this.userMapper = userMapper;
    }

    public Optional<UserEntity> findById(BigInteger id) {
        return userRepository.findById(id);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserEntity getLoggedUser() {
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(userPrinciple.getId()).orElseThrow();
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity delete(UserEntity userEntity) {
        userEntity.setDeleted((byte) 1);
        return save(userEntity);
    }

    public UserEntity update(UserEntity user, UserDTO userDTO) {
        List<RoleUserEntity> rolesUser = this.roleUserMapper.mapRoleUserEntities(userDTO.getRolesUserDto());
        this.userMapper.updateUserEntity(user, userDTO);

        this.roleUserToUserService.deleteByUser(user);
        this.roleUserToUserService.addRoleToUser(user, rolesUser);

        return save(user);
    }

    public boolean findByEmailAndLoginNot(String email, String login) {
        return this.userRepository.findByEmailAndLoginNot(email, login).isPresent();
    }

    public Optional<UserEntity> findByUserIdAndDeleted(BigInteger userId, byte deleted) {
        return this.userRepository.findByUserIdAndDeleted(userId, deleted);
    }
}
