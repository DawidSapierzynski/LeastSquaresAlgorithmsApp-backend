package pl.leastsquaresalgorithms.user;

import io.restassured.RestAssured;
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

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
class RoleControllerTests {
    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.2.0");
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost/api/roleUser";
        RestAssured.port = port;
    }

    @Test
    void shouldGetAllRoles() {
        RestAssured.when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id[0]", equalTo(1),
                        "code[0]", equalTo("ADMIN"),
                        "name[0]", equalTo("Admin"),
                        "id[1]", equalTo(2),
                        "code[1]", equalTo("USER"),
                        "name[1]", equalTo("User"));
    }
}
