package pl.leastsquaresalgorithms.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import pl.leastsquaresalgorithms.user.dto.RoleUserDto;
import pl.leastsquaresalgorithms.user.dto.SignUpForm;
import pl.leastsquaresalgorithms.user.dto.UserDto;

import java.util.Set;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserServiceApplicationTests {
    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost/api/user";
        RestAssured.port = port;
    }

    @Test
    void shouldGetUser() {
        RestAssured.when()
                .get("/{id}", 1)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1),
                        "login", equalTo("admin"),
                        "rolesUserDto[0].code", equalTo("ADMIN")
                );
    }

    @Test
    void shouldRegisterUser() {
        RoleUserDto roleUserDto = RoleUserDto.builder().id(2L).code("USER").name("User").build();
        Set<RoleUserDto> roles = ImmutableSet.of(roleUserDto);
        SignUpForm signUpForm = new SignUpForm("test", "test", "testtest", "test@test.local", roles, "test1234");
        UserDto response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signUpForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(UserDto.class);

        RestAssured.when()
                .get("/{id}", response.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", equalTo(response.getFirstName()),
                        "lastName", equalTo(signUpForm.getLastName()),
                        "email", equalTo(signUpForm.getEmail()),
                        "login", equalTo(signUpForm.getLogin()),
                        "rolesUserDto[0].id", equalTo(roleUserDto.getId().intValue()),
                        "rolesUserDto[0].code", equalTo(roleUserDto.getCode()),
                        "rolesUserDto[0].name", equalTo(roleUserDto.getName())
                );
    }

    @Test
    void shouldUpdateUser() {
        String newMail = "user-uzytkownik@lsaa.local";
        RoleUserDto roleUserDto = RoleUserDto.builder().id(2L).code("USER").name("User").build();
        UserDto userDto = UserDto.builder()
                .id(2L)
                .login("user")
                .firstName("User")
                .lastName("Użytkownik")
                .email(newMail)
                .rolesUserDto(ImmutableSet.of(roleUserDto))
                .deleted(false)
                .admin(false)
                .active(true)
                .build();
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(newMail));

        RestAssured.when()
                .get("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("firstName", equalTo(userDto.getFirstName()),
                        "lastName", equalTo(userDto.getLastName()),
                        "email", equalTo(userDto.getEmail()),
                        "login", equalTo(userDto.getLogin()),
                        "rolesUserDto[0].id", equalTo(roleUserDto.getId().intValue()),
                        "rolesUserDto[0].code", equalTo(roleUserDto.getCode()),
                        "rolesUserDto[0].name", equalTo(roleUserDto.getName())
                );
    }
}
