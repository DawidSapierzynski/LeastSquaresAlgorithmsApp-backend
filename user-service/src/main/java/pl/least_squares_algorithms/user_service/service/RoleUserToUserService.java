package pl.least_squares_algorithms.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.user_service.model.RoleUserToUserEntity;
import pl.least_squares_algorithms.user_service.model.UserEntity;
import pl.least_squares_algorithms.user_service.repository.RoleUserToUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleUserToUserService {
    private final RoleUserToUserRepository roleUserToUserRepository;

    public RoleUserToUserEntity save(RoleUserToUserEntity roleUserToUserEntity) {
        return roleUserToUserRepository.save(roleUserToUserEntity);
    }

    public Optional<RoleUserToUserEntity> findById(Long id) {
        return roleUserToUserRepository.findById(id);
    }

    public List<RoleUserToUserEntity> findByUser(UserEntity userEntity) {
        return roleUserToUserRepository.findByUserByUserId(userEntity);
    }

    public void deleteByUser(UserEntity userEntity) {
        roleUserToUserRepository.deleteByUserByUserId(userEntity);
    }
}
