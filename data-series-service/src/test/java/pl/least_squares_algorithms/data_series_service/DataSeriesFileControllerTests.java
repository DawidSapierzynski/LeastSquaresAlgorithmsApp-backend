package pl.least_squares_algorithms.data_series_service;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.data_series_service.dto.DataSeriesFileDto;
import pl.least_squares_algorithms.data_series_service.repository.DataSeriesFileRepository;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DataSeriesFileControllerTests {
    public static final double DELTA = 1.0e-12;
    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0");
    @LocalServerPort
    private Integer port;
    @Autowired
    private DataSeriesFileRepository dataSeriesFileRepository;

    @BeforeEach
    void setup() {
        dataSeriesFileRepository.deleteAllInBatch();
        RestAssured.baseURI = "http://localhost/api/data-series-file";
        RestAssured.port = port;
    }

    @AfterEach
    void cleanup() {
        dataSeriesFileRepository.deleteAllInBatch();
    }

    @Test
    void shouldGetAll() {
        RestAssured.given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", empty());
    }

    @Test
    void shouldGetDataSeriesFile() {
        RestAssured.given()
                .when()
                .get("/{id}", 1)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldUploadDataSeriesFile() {
        String content = "1;1\n2;2\n3;3\n4;4\n5;5\n6;6\n7;7\n8;8\n9;9\n10;10";
        MultiPartSpecification fileSpec = new MultiPartSpecBuilder(content.getBytes(StandardCharsets.UTF_8))
                .fileName("test_file.csv")
                .controlName("dataSeriesFile")
                .mimeType("text/csv")
                .build();
        DataSeriesFileDto dataSeriesFileDto = RestAssured.given()
                .multiPart(fileSpec)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(DataSeriesFileDto.class);

        assertThat(dataSeriesFileDto.getName(), equalTo("test_file.csv"));
        assertThat(dataSeriesFileDto.getSize(), equalTo(10));
        assertThat(dataSeriesFileDto.getVariance(), closeTo(8.25, DELTA));
        assertThat(dataSeriesFileDto.getStandardDeviation(), closeTo(2.8722813232690, DELTA));

        List<PointXY> points = dataSeriesFileDto.getPoints();
        assertThat(points.size(), equalTo(10));
        for (int i = 0; i < points.size(); i++) {
            PointXY point = points.get(i);
            assertThat(point.getX(), closeTo(i + 1.0, DELTA));
            assertThat(point.getY(), closeTo(i + 1.0, DELTA));
        }
    }
}
