package pl.leastsquaresalgorithms.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.leastsquaresalgorithms.user.configuration.exception.ForbiddenException;
import pl.leastsquaresalgorithms.user.configuration.exception.LoginException;
import pl.leastsquaresalgorithms.user.configuration.exception.ResourceNotFoundException;
import pl.leastsquaresalgorithms.user.dto.ResponseMessage;
import pl.leastsquaresalgorithms.user.dto.SignUpForm;
import pl.leastsquaresalgorithms.user.dto.UserDto;
import pl.leastsquaresalgorithms.user.mapper.UserMapper;
import pl.leastsquaresalgorithms.user.model.UserEntity;
import pl.leastsquaresalgorithms.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/user")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<UserEntity> userEntities = userService.getAll();
        List<UserDto> userDtos = userMapper.buildUserDTOs(userEntities);

        log.debug("Getting all users successfully completed. Size: {}", userDtos.size());
        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable("userId") Long userId) throws ResourceNotFoundException, ForbiddenException {
        UserEntity userEntities = userService.findByUserIdAndDeleted(userId, Boolean.FALSE)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(Objects.equals(loggedUser.getUserId(), userId) || loggedUser.isAdmin())) {
//            throw new ForbiddenException("No permission to open this user details");
//        }
        UserDto userDTO = userMapper.buildUserDTO(userEntities);
        log.debug("Get user successfully completed. Id: {}", userDTO.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws LoginException {
        if (userService.existsByLogin(signUpRequest.getLogin())) {
            throw new LoginException("Fail - Username is already taken!");
        }
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new LoginException("Fail - Email is already in use!");
        }
        UserEntity user = userService.create(signUpRequest);
        UserDto userDto = userMapper.buildUserDTO(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") Long userId, @RequestBody UserDto userDTO) throws ResourceNotFoundException, LoginException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        if (userService.findByEmailAndLoginNot(userDTO.getEmail(), userDTO.getLogin())) {
            throw new LoginException("Fail - Email is already in use!");
        }
        user = userService.update(user, userDTO);
        log.debug("Updated user with id: {}", userId);
        return new ResponseEntity<>(userMapper.buildUserDTO(user), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<ResponseMessage> deleteUser(@PathVariable(value = "userId") Long userId) throws ResourceNotFoundException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        userService.delete(user);
        log.debug("Deleted user with id: {}", userId);
        return ResponseEntity.ok(new ResponseMessage("Deleted user with id: " + userId));
    }
}
