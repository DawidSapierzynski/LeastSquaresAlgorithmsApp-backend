package pl.edu.wat.wcy.isi.app.controller;

import jakarta.validation.Valid;
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
import pl.edu.wat.wcy.isi.app.configuration.security.jwt.JwtServiceImpl;
import pl.edu.wat.wcy.isi.app.dto.message.request.LoginForm;
import pl.edu.wat.wcy.isi.app.dto.message.request.SignUpForm;
import pl.edu.wat.wcy.isi.app.dto.message.response.JwtResponse;
import pl.edu.wat.wcy.isi.app.dto.message.response.ResponseMessage;
import pl.edu.wat.wcy.isi.app.mapper.RoleUserMapper;
import pl.edu.wat.wcy.isi.app.mapper.UserMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.service.RoleUserToUserService;
import pl.edu.wat.wcy.isi.app.service.UserService;

import static pl.edu.wat.wcy.isi.app.dto.message.response.JwtResponse.BEARER_TOKEN_TYPE;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtServiceImpl jwtServiceImpl;
    private final UserMapper userMapper;
    private final RoleUserMapper roleUserMapper;
    private final RoleUserToUserService roleUserToUserService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtServiceImpl jwtServiceImpl,
                          UserMapper userMapper, RoleUserMapper roleUserMapper, RoleUserToUserService roleUserToUserService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtServiceImpl = jwtServiceImpl;
        this.userMapper = userMapper;
        this.roleUserMapper = roleUserMapper;
        this.roleUserToUserService = roleUserToUserService;
    }

    @PostMapping(produces = "application/json", value = "/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrinciple userDetails = (UserPrinciple) authentication.getPrincipal();
        String jwt = jwtServiceImpl.generateToken(userDetails);

        logger.debug("User logged in with id: {}", userDetails.getId());
        JwtResponse jwtResponse = JwtResponse.builder()
                .tokenType(BEARER_TOKEN_TYPE)
                .accessToken(jwt)
                .username(userDetails.getUsername())
                .userId(String.valueOf(userDetails.getId()))
                .authorities(userDetails.getAuthorities())
                .build();
        return ResponseEntity.ok(jwtResponse);
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
