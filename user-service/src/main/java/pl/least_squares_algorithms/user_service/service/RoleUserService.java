package pl.least_squares_algorithms.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.user_service.model.RoleUserEntity;
import pl.least_squares_algorithms.user_service.repository.RoleUserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleUserService {
    private final RoleUserRepository roleUserRepository;

    public RoleUserEntity save(RoleUserEntity roleUserEntity) {
        return roleUserRepository.save(roleUserEntity);
    }

    public Optional<RoleUserEntity> findById(Long id) {
        return roleUserRepository.findById(id);
    }

    public Optional<RoleUserEntity> findByCode(String code) {
        return roleUserRepository.findByCode(code);
    }

    public List<RoleUserEntity> getAll() {
        return roleUserRepository.findAll();
    }
}
