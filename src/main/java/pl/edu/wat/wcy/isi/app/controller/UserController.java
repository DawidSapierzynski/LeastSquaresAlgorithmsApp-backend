package pl.edu.wat.wcy.isi.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.edu.wat.wcy.isi.app.configuration.exception.ForbiddenException;
import pl.edu.wat.wcy.isi.app.configuration.exception.LoginException;
import pl.edu.wat.wcy.isi.app.configuration.exception.ResourceNotFoundException;
import pl.edu.wat.wcy.isi.app.dto.UserDTO;
import pl.edu.wat.wcy.isi.app.dto.message.response.ResponseMessage;
import pl.edu.wat.wcy.isi.app.mapper.UserMapper;
import pl.edu.wat.wcy.isi.app.model.entityModels.UserEntity;
import pl.edu.wat.wcy.isi.app.service.UserService;

import java.math.BigInteger;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserEntity> userEntities = userService.getAll();
        List<UserDTO> userDTOs = userMapper.buildUserDTOs(userEntities);

        logger.info("Getting all users successfully completed. Size: {}", userDTOs.size());
        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json", value = "/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") BigInteger userId) throws ResourceNotFoundException, ForbiddenException {
        UserEntity userEntities = userService.findByUserIdAndDeleted(userId, (byte) 0)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));
        UserEntity loggedUser = userService.getLoggedUser();

        if (!(loggedUser.getUserId() == userId || loggedUser.isAdmin())) {
            throw new ForbiddenException("No permission to open this user details");
        }

        UserDTO userDTO = userMapper.buildUserDTO(userEntities);

        logger.info("Get user successfully completed. Id: {}", userDTO.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping(produces = "application/json", value = "/{userId}")
    public ResponseEntity<ResponseMessage> deletedUser(@PathVariable(value = "userId") BigInteger userId) throws ResourceNotFoundException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        this.userService.delete(user);

        logger.info("Deleted user with id: {}", userId);
        return ResponseEntity.ok(new ResponseMessage("Deleted user with id: " + userId));
    }

    @Transactional
    @PutMapping(produces = "application/json", value = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable(value = "userId") BigInteger userId, @RequestBody UserDTO userDTO) throws ResourceNotFoundException, LoginException {
        UserEntity user = userService.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id: " + userId));

        if (userService.findByEmailAndLoginNot(userDTO.getEmail(), userDTO.getLogin())) {
            throw new LoginException("Fail - Email is already in use!");
        }

        user = this.userService.update(user, userDTO);

        logger.info("Updated user with id: {}", userId);
        return new ResponseEntity<>(this.userMapper.buildUserDTO(user), HttpStatus.OK);
    }
}
