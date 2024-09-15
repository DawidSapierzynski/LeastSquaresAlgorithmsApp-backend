package pl.leastsquaresalgorithms.user.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.user.dto.RoleUserDto;
import pl.leastsquaresalgorithms.user.model.RoleUserEntity;
import pl.leastsquaresalgorithms.user.service.RoleUserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleUserMapper {
    private final RoleUserService roleUserService;

    public List<RoleUserDto> buildRoleUserDTOs(Collection<RoleUserEntity> roleUserEntities) {
        return roleUserEntities.stream()
                .map(this::buildRoleUserDTOs)
                .collect(Collectors.toList());
    }

    public RoleUserDto buildRoleUserDTOs(RoleUserEntity roleUserEntity) {
        return RoleUserDto.builder()
                .id(roleUserEntity.getRoleUserId())
                .code(roleUserEntity.getCode())
                .name(roleUserEntity.getName())
                .build();
    }

    public List<RoleUserEntity> mapRoleUserEntities(Collection<RoleUserDto> roleUserDtos) {
        List<RoleUserEntity> roleUserEntities = new ArrayList<>();
        for (RoleUserDto role : roleUserDtos) {
            Optional<RoleUserEntity> roleUserEntity = findRoleUserEntity(role);
            roleUserEntity.ifPresent(roleUserEntities::add);
        }
        return roleUserEntities;
    }

    public Optional<RoleUserEntity> findRoleUserEntity(RoleUserDto roleUserDTO) {
        return roleUserService.findById(roleUserDTO.getId());
    }
}
