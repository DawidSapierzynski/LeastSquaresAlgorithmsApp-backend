package pl.leastsquaresalgorithms.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.leastsquaresalgorithms.user.dto.RoleUserDto;
import pl.leastsquaresalgorithms.user.mapper.RoleUserMapper;
import pl.leastsquaresalgorithms.user.model.RoleUserEntity;
import pl.leastsquaresalgorithms.user.service.RoleUserService;

import java.util.List;

@RestController
@RequestMapping(value = "/roleUser")
public class RoleUserController {
    private final RoleUserService roleUserService;
    private final RoleUserMapper roleUserMapper;

    public RoleUserController(RoleUserService roleUserService, RoleUserMapper roleUserMapper) {
        this.roleUserService = roleUserService;
        this.roleUserMapper = roleUserMapper;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<RoleUserDto>> getAllUserRoles() {
        List<RoleUserEntity> roleUserEntities = roleUserService.getAll();
        List<RoleUserDto> roleUserDtos = roleUserMapper.buildRoleUserDTOs(roleUserEntities);
        return new ResponseEntity<>(roleUserDtos, HttpStatus.OK);
    }
}
