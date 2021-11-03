package pl.edu.wat.wcy.isi.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.isi.app.configuration.exception.LoginException;
import pl.edu.wat.wcy.isi.app.configuration.security.UserPrinciple;
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtProvider;
import pl.edu.wat.wcy.isi.app.dto.message.request.LoginForm;
import pl.edu.wat.wcy.isi.app.dto.message.request.SignUpForm;
import pl.edu.wat.wcy.isi.app.dto.message.response.JwtResponse;
import pl.edu.wat.wcy.isi.app.dto.message.response.ResponseMessage;
import pl.edu.wat.wcy.isi.app.mapper.RoleUserMapper;
import pl.edu.wat.wcy.isi.app.mapper.UserMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.service.RoleUserToUserService;
import pl.edu.wat.wcy.isi.app.service.UserService;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final RoleUserMapper roleUserMapper;
    private final RoleUserToUserService roleUserToUserService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtProvider jwtProvider,
                          UserMapper userMapper, RoleUserMapper roleUserMapper, RoleUserToUserService roleUserToUserService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.userMapper = userMapper;
        this.roleUserMapper = roleUserMapper;
        this.roleUserToUserService = roleUserToUserService;
    }

    @PostMapping(produces = "application/json", value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();

        logger.info("User logged in with id: {}", userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), String.valueOf(userDetails.getId()), userDetails.getAuthorities()));
    }

    @Transactional
    @PostMapping(produces = "application/json", value = "/signup")
    public ResponseEntity<ResponseMessage> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws LoginException {
        if (userService.existsByLogin(signUpRequest.getLogin())) {
            throw new LoginException("Fail - Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new LoginException("Fail - Email is already in use!");
        }

        UserEntity user = userMapper.buildUserEntity(signUpRequest);

        user = userService.save(user);
        roleUserToUserService.addRoleToUser(user, roleUserMapper.mapRoleUserEntities(signUpRequest.getRole()));

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}
