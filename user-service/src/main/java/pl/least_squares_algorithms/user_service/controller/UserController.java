package pl.least_squares_algorithms.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.least_squares_algorithms.user_service.configuration.exception.ForbiddenException;
import pl.least_squares_algorithms.user_service.configuration.exception.LoginException;
import pl.least_squares_algorithms.user_service.configuration.exception.ResourceNotFoundException;
import pl.least_squares_algorithms.user_service.dto.ResponseMessage;
import pl.least_squares_algorithms.user_service.dto.SignUpForm;
import pl.least_squares_algorithms.user_service.dto.UserDto;
import pl.least_squares_algorithms.user_service.mapper.UserMapper;
import pl.least_squares_algorithms.user_service.model.UserEntity;
import pl.least_squares_algorithms.user_service.service.UserService;

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
//        UserEntity loggedUser = userService.getLoggedUser();
//        if (!(Objects.equals(loggedUser.getUserId(), userId) || loggedUser.getAdmin())) {
//            throw new ForbiddenException("No permission to open this user details");
//        }
        UserEntity userEntities = userService.findByUserIdAndDeleted(userId, Boolean.FALSE)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        UserDto userDTO = userMapper.buildUserDTO(userEntities);
        log.debug("Get user successfully completed. Id: {}", userDTO.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpForm signUpRequest) throws LoginException {
        UserEntity user = userService.create(signUpRequest);
        UserDto userDto = userMapper.buildUserDTO(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping(value = "/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(value = "userId") Long userId, @Valid @RequestBody UserDto userDTO) throws ResourceNotFoundException, LoginException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
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
