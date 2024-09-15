package pl.leastsquaresalgorithms.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.user.dto.RoleUserDto;
import pl.leastsquaresalgorithms.user.dto.SignUpForm;
import pl.leastsquaresalgorithms.user.dto.UserDto;
import pl.leastsquaresalgorithms.user.model.UserEntity;
import pl.leastsquaresalgorithms.user.model.UserRole;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final BCryptPasswordEncoder encoder;
    private final RoleUserMapper roleUserMapper;

    public UserEntity buildUserEntity(SignUpForm signUpForm) {
        UserEntity userEntity = new UserEntity();
        userEntity.setActive(Boolean.TRUE);
        userEntity.setDeleted(Boolean.FALSE);
        userEntity.setLogin(signUpForm.getLogin());
        userEntity.setFirstName(signUpForm.getFirstName());
        userEntity.setLastName(signUpForm.getLastName());
        userEntity.setPassword(encoder.encode(signUpForm.getPassword()));
        userEntity.setEmail(signUpForm.getEmail());
        userEntity.setAdmin(isAdmin(signUpForm.getRole()));
        userEntity.setRolesUser(roleUserMapper.mapRoleUserEntities(signUpForm.getRole()));
        return userEntity;
    }

    public void updateUserEntity(UserEntity user, UserDto userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setActive(userDTO.isActive());
        user.setAdmin(isAdmin(userDTO.getRolesUserDto()));
        user.setRolesUser(roleUserMapper.mapRoleUserEntities(userDTO.getRolesUserDto()));
    }

    private Boolean isAdmin(Collection<RoleUserDto> roles) {
        for (RoleUserDto r : roles) {
            if (r.getCode().equals(UserRole.ADMIN.getCode())) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }


    public List<UserDto> buildUserDTOs(Collection<UserEntity> userEntities) {
        return userEntities.stream()
                .map(this::buildUserDTO)
                .collect(Collectors.toList());
    }

    public UserDto buildUserDTO(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getUserId())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .login(userEntity.getLogin())
                .email(userEntity.getEmail())
                .rolesUserDto(roleUserMapper.buildRoleUserDTOs(userEntity.getRolesUser()))
                .active(userEntity.getActive())
                .deleted(userEntity.getDeleted())
                .admin(userEntity.getAdmin())
                .build();
    }
}
