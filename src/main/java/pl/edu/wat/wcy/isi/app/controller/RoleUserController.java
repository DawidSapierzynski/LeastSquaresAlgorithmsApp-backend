package pl.edu.wat.wcy.isi.app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.wat.wcy.isi.app.dto.RoleUserDTO;
import pl.edu.wat.wcy.isi.app.mapper.RoleUserMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.RoleUserEntity;
import pl.edu.wat.wcy.isi.app.service.RoleUserService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<List<RoleUserDTO>> getAllUserRoles() {
        List<RoleUserEntity> roleUserEntities = roleUserService.getAll();
        List<RoleUserDTO> roleUserDTOs = roleUserMapper.buildRoleUserDTOs(roleUserEntities);

        return new ResponseEntity<>(roleUserDTOs, HttpStatus.OK);
    }
}
