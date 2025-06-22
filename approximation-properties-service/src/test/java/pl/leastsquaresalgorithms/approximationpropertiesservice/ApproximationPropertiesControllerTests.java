package pl.leastsquaresalgorithms.approximationpropertiesservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.leastsquaresalgorithms.approximationpropertiesservice.client.DataSeriesFileClient;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.ApproximationPropertiesDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.DataSeriesFileDto;
import pl.leastsquaresalgorithms.approximationpropertiesservice.dto.PointXY;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Transactional
class ApproximationPropertiesControllerTests {

    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:9.2.0");

    @LocalServerPort
    private Integer port;

    @MockitoBean
    private DataSeriesFileClient dataSeriesFileClient;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost/api/approximation-properties";
        RestAssured.port = port;
    }

    @Test
    void shouldGetAllForUser() {
        RestAssured.given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", empty());
    }

    @Test
    void shouldGetAll() {
        RestAssured.given()
                .when()
                .get("/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", empty());
    }

    @Test
    void shouldCreateApproximationProperties() {
        List<PointXY> mockedPoints = new ArrayList<>();
        for (double i = 1.0; i <= 10.0; i++) {
            mockedPoints.add(new PointXY(i, i));
        }
        DataSeriesFileDto mockedDataSeriesFileDto = DataSeriesFileDto.builder()
                .id(BigInteger.ONE)
                .userId(BigInteger.ONE)
                .name("mocked_file.csv")
                .hashName(UUID.randomUUID().toString())
                .dateSent(new Timestamp(System.currentTimeMillis()))
                .size(10)
                .variance(8.25)
                .standardDeviation(2.8722813232690)
                .deleted(Boolean.FALSE)
                .points(mockedPoints)
                .build();
        when(dataSeriesFileClient.getDataSeriesFile(BigInteger.ONE)).thenReturn(mockedDataSeriesFileDto);

        ApproximationPropertiesDto newApproximationPropertiesDto = RestAssured.given()
                .queryParam("dataSeriesFileId", BigInteger.ONE)
                .queryParam("degree", BigInteger.ONE)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ApproximationPropertiesDto.class);

        assertThat(newApproximationPropertiesDto.getId(), notNullValue());
        assertThat(newApproximationPropertiesDto.getUserId(), notNullValue());
        assertThat(newApproximationPropertiesDto.getDegree(), equalTo(1));
        assertThat(newApproximationPropertiesDto.getDateCreate(), notNullValue());
        assertThat(newApproximationPropertiesDto.isDeleted(), equalTo(Boolean.FALSE));

        DataSeriesFileDto dataSeriesFileDto = newApproximationPropertiesDto.getDataSeriesFile();
        assertThat(dataSeriesFileDto, notNullValue());
        assertThat(dataSeriesFileDto.getId(), equalTo(mockedDataSeriesFileDto.getId()));
        assertThat(dataSeriesFileDto.getUserId(), equalTo(mockedDataSeriesFileDto.getUserId()));
        assertThat(dataSeriesFileDto.getName(), equalTo(mockedDataSeriesFileDto.getName()));
        assertThat(dataSeriesFileDto.getHashName(), equalTo(mockedDataSeriesFileDto.getHashName()));
        assertThat(dataSeriesFileDto.getDateSent(), equalTo(mockedDataSeriesFileDto.getDateSent()));
        assertThat(dataSeriesFileDto.getSize(), equalTo(mockedDataSeriesFileDto.getSize()));
        assertThat(dataSeriesFileDto.getVariance(), equalTo(mockedDataSeriesFileDto.getVariance()));
        assertThat(dataSeriesFileDto.getStandardDeviation(), equalTo(mockedDataSeriesFileDto.getStandardDeviation()));
        assertThat(dataSeriesFileDto.isDeleted(), equalTo(mockedDataSeriesFileDto.isDeleted()));
        assertThat(dataSeriesFileDto.getPoints(), hasSize(mockedDataSeriesFileDto.getPoints().size()));
        for (int i = 0; i < mockedDataSeriesFileDto.getPoints().size(); i++) {
            assertThat(dataSeriesFileDto.getPoints().get(i), equalTo(mockedDataSeriesFileDto.getPoints().get(i)));
        }
    }
}
