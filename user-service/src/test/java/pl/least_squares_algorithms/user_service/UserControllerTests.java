package pl.least_squares_algorithms.user_service;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.google.common.collect.ImmutableSet;
import pl.least_squares_algorithms.user_service.dto.RoleUserDto;
import pl.least_squares_algorithms.user_service.dto.SignUpForm;
import pl.least_squares_algorithms.user_service.dto.UserDto;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
class UserControllerTests {
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
    void shouldGetAllUsers() {
        RestAssured.when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id[0]", equalTo(1),
                        "login[0]", equalTo("admin"),
                        "id[1]", equalTo(2),
                        "login[1]", equalTo("user"));
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
        SignUpForm signUpForm = buildTestSignUpForm();
        RoleUserDto roleUserDto = signUpForm.getRole().stream().findFirst().orElseThrow();
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
                .body("id", equalTo(response.getId().intValue()))
                .body("firstName", equalTo(signUpForm.getFirstName()))
                .body("lastName", equalTo(signUpForm.getLastName()))
                .body("email", equalTo(signUpForm.getEmail()))
                .body("login", equalTo(signUpForm.getLogin()))
                .body("rolesUserDto[0].id", equalTo(roleUserDto.getId().intValue()))
                .body("rolesUserDto[0].code", equalTo(roleUserDto.getCode()))
                .body("rolesUserDto[0].name", equalTo(roleUserDto.getName()));
    }

    @Test
    void shouldRegisterUserWithSameLogin() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLogin("admin");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooShortLogin() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLogin("ts");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooLongLogin() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLogin("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBlankLogin() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLogin("");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooShortFirstName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setFirstName("te");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooLongFirstName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setFirstName("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBlankFirstName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setFirstName("");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooShortLastName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLastName("te");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooLongLastName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLastName("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBlankLastName() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setLastName("");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithSameEmail() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setEmail("admin@lsaa.local");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooLongEmail() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setEmail("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest@lsaa.local");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBlankEmail() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setEmail("");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBadPatternEmail() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setEmail("test");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooShortPassword() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setPassword("test");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithTooLongPassword() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setPassword("testtesttesttesttesttesttesttesttesttesttesttesttesttest1234");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldRegisterUserWithBlankPassword() {
        SignUpForm signUpForm = buildTestSignUpForm();
        signUpForm.setPassword("");
        shouldRegisterUserWithBadRequest(signUpForm);
    }

    @Test
    void shouldUpdateUser() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("user-uzytkownik@lsaa.local");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(userDto.getEmail()));

        RestAssured.when()
                .get("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(userDto.getId().intValue()))
                .body("firstName", equalTo(userDto.getFirstName()))
                .body("lastName", equalTo(userDto.getLastName()))
                .body("email", equalTo(userDto.getEmail()))
                .body("login", equalTo(userDto.getLogin()))
                .body("rolesUserDto[0].id", equalTo(roleUserDto.getId().intValue()))
                .body("rolesUserDto[0].code", equalTo(roleUserDto.getCode()))
                .body("rolesUserDto[0].name", equalTo(roleUserDto.getName())
                );
    }

    @Test
    void shouldUpdateUserWithTooShortLogin() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("us");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooLongLogin() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithBlankLogin() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooShortFirstName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setFirstName("us");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooLongFirstName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setFirstName("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithBlankFirstName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setFirstName("");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooShortLastName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setLastName("us");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooLongLastName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setLastName("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithBlankLastName() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setLastName("");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithTooLongEmail() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("testtesttesttesttesttesttesttesttesttesttesttesttesttesttest@lsaa.local");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithBlankEmail() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldUpdateUserWithBadPatternEmail() {
        RoleUserDto roleUserDto = buildUserRole();
        UserDto userDto = buildUpdateUserDto(roleUserDto);
        userDto.setEmail("user@");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put("/{id}", userDto.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldDeleteUser() {
        int userId = 2;
        RestAssured.given()
                .when()
                .delete("/{id}", userId)
                .then()
                .statusCode(HttpStatus.OK.value());

        RestAssured.when()
                .get("/{id}", userId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private static void shouldRegisterUserWithBadRequest(SignUpForm signUpForm) {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signUpForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private static RoleUserDto buildUserRole() {
        return RoleUserDto.builder().id(2L).code("USER").name("User").build();
    }

    private static SignUpForm buildTestSignUpForm() {
        return SignUpForm.builder()
                .firstName("test")
                .lastName("test")
                .login("test")
                .email("test@test.local")
                .role(ImmutableSet.of(buildUserRole()))
                .password("test1234")
                .build();
    }

    private static UserDto buildUpdateUserDto(RoleUserDto roleUserDto) {
        return UserDto.builder()
                .id(2L)
                .login("user")
                .firstName("User")
                .lastName("Użytkownik")
                .email("user@lsaa.local")
                .rolesUserDto(ImmutableSet.of(roleUserDto))
                .deleted(false)
                .admin(false)
                .active(true)
                .build();
    }
}
