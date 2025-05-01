package pl.leastsquaresalgorithms.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.user.configuration.exception.LoginException;
import pl.leastsquaresalgorithms.user.dto.SignUpForm;
import pl.leastsquaresalgorithms.user.dto.UserDto;
import pl.leastsquaresalgorithms.user.mapper.UserMapper;
import pl.leastsquaresalgorithms.user.model.UserEntity;
import pl.leastsquaresalgorithms.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public boolean findByEmailAndLoginNot(String email, String login) {
        return userRepository.findByEmailAndLoginNot(email, login).isPresent();
    }

    public Optional<UserEntity> findByUserIdAndDeleted(Long userId, Boolean deleted) {
        return userRepository.findByUserIdAndDeleted(userId, deleted);
    }

    public boolean existsByLogin(String login) {
        return userRepository.findByLogin(login).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public UserEntity create(SignUpForm signUpRequest) throws LoginException {
        if (existsByLogin(signUpRequest.getLogin())) {
            throw new LoginException("Fail - Username is already in use!");
        }
        if (existsByEmail(signUpRequest.getEmail())) {
            throw new LoginException("Fail - Email is already in use!");
        }
        UserEntity user = userMapper.buildUserEntity(signUpRequest);
        return save(user);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    public UserEntity update(UserEntity user, UserDto userDTO) throws LoginException {
        if (findByEmailAndLoginNot(userDTO.getEmail(), userDTO.getLogin())) {
            throw new LoginException("Fail - Email is already in use!");
        }
        userMapper.updateUserEntity(user, userDTO);
        return save(user);
    }

    public void delete(UserEntity userEntity) {
        userEntity.setDeleted(Boolean.TRUE);
        save(userEntity);
    }
}
