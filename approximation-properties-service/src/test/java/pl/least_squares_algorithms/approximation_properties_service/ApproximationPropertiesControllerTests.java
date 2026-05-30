package pl.least_squares_algorithms.approximation_properties_service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.least_squares_algorithms.approximation_properties_service.client.DataSeriesFileClient;
import pl.least_squares_algorithms.approximation_properties_service.dto.ApproximationPropertiesDto;
import pl.least_squares_algorithms.approximation_properties_service.dto.DataSeriesFileDto;
import pl.least_squares_algorithms.approximation_properties_service.dto.PointXY;
import pl.least_squares_algorithms.approximation_properties_service.dto.ResponseMessage;
import pl.least_squares_algorithms.approximation_properties_service.repository.ApproximationPropertiesRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class ApproximationPropertiesControllerTests {

    @Container
    @ServiceConnection
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.4.0");
    @LocalServerPort
    private Integer port;
    @MockitoBean
    private DataSeriesFileClient dataSeriesFileClient;
    @Autowired
    private ApproximationPropertiesRepository approximationPropertiesRepository;

    @BeforeEach
    void setup() {
        approximationPropertiesRepository.deleteAllInBatch();
        RestAssured.baseURI = "http://localhost/api/approximation-properties";
        RestAssured.port = port;
    }

    @AfterEach
    void cleanup() {
        approximationPropertiesRepository.deleteAllInBatch();
    }

    /**
     * GET
     */
    @Test
    void shouldReturnEmptyListWhenGettingAllApproximationProperties() {
        RestAssured.given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("", empty());
    }

    @Test
    void shouldReturnNotFoundWhenApproximationPropertiesDoesNotExist() {
        RestAssured.given()
                .when()
                .get("/{approximationPropertiesId}", BigInteger.valueOf(999999))
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldGetApproximationPropertiesById() {
        Long dataSeriesFileId = 1L;
        DataSeriesFileDto mockedDataSeriesFileDto = buildMockedDataSeriesFileDto(dataSeriesFileId);
        when(dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId)).thenReturn(mockedDataSeriesFileDto);

        ApproximationPropertiesDto created = RestAssured.given()
                .queryParam("dataSeriesFileId", dataSeriesFileId)
                .queryParam("degree", 1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ApproximationPropertiesDto.class);

        ApproximationPropertiesDto found = RestAssured.given()
                .when()
                .get("/{approximationPropertiesId}", created.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ApproximationPropertiesDto.class);

        assertThat(found.getId(), equalTo(created.getId()));
        assertThat(found.getDegree(), equalTo(created.getDegree()));
        assertThat(found.isDeleted(), equalTo(false));
        assertThat(found.getDataSeriesFile(), notNullValue());
    }

    @Test
    void shouldReturnCreatedApproximationPropertiesWhenGettingAllForUser() {
        Long dataSeriesFileId = 1L;
        DataSeriesFileDto mockedDataSeriesFileDto = buildMockedDataSeriesFileDto(dataSeriesFileId);
        when(dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId)).thenReturn(mockedDataSeriesFileDto);

        ApproximationPropertiesDto created = RestAssured.given()
                .queryParam("dataSeriesFileId", dataSeriesFileId)
                .queryParam("degree", 1L)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ApproximationPropertiesDto.class);

        RestAssured.given()
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", hasItem(created.getId().intValue()));
    }

    /**
     * POST
     */
    @Test
    void shouldCreateApproximationProperties() {
        Long dataSeriesFileId = 1L;
        DataSeriesFileDto mockedDataSeriesFileDto = buildMockedDataSeriesFileDto(dataSeriesFileId);
        when(dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId)).thenReturn(mockedDataSeriesFileDto);

        ApproximationPropertiesDto newApproximationPropertiesDto = RestAssured.given()
                .queryParam("dataSeriesFileId", dataSeriesFileId)
                .queryParam("degree", 1L)
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
        assertDataSeriesFileDtoEquals(dataSeriesFileDto, mockedDataSeriesFileDto);
    }

    private static void assertDataSeriesFileDtoEquals(DataSeriesFileDto dataSeriesFileDto, DataSeriesFileDto mockedDataSeriesFileDto) {
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

    @Test
    void shouldReturnBadRequestWhenDegreeIsMissing() {
        RestAssured.given()
                .queryParam("dataSeriesFileId", 1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    @Test
    void shouldReturnBadRequestWhenDataSeriesFileIdIsMissing() {
        RestAssured.given()
                .queryParam("degree", 1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    @Test
    void shouldReturnBadRequestWhenDataSeriesFileIdIsInvalid() {
        RestAssured.given()
                .queryParam("dataSeriesFileId", "abc")
                .queryParam("degree", 1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    @Test
    void shouldReturnBadRequestWhenDataSeriesFileIdIsZero() {
        RestAssured.given()
                .queryParam("dataSeriesFileId", 0)
                .queryParam("degree", 1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    @Test
    void shouldReturnBadRequestWhenDegreeIsNegative() {
        RestAssured.given()
                .queryParam("dataSeriesFileId", 0)
                .queryParam("degree", -1)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    @Test
    void shouldReturnBadRequestWhenDegreeIsZero() {
        RestAssured.given()
                .queryParam("dataSeriesFileId", 0)
                .queryParam("degree", 0)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        verify(dataSeriesFileClient, never()).getDataSeriesFile(any());
    }

    /**
     * DELETE
     */
    @Test
    void shouldDeleteExistingApproximationPropertiesAndReturnSuccessMessage() {
        Long dataSeriesFileId = 1L;
        DataSeriesFileDto mockedDataSeriesFileDto = buildMockedDataSeriesFileDto(dataSeriesFileId);
        when(dataSeriesFileClient.getDataSeriesFile(dataSeriesFileId)).thenReturn(mockedDataSeriesFileDto);

        ApproximationPropertiesDto createdApproximationPropertiesDto = RestAssured.given()
                .queryParam("dataSeriesFileId", dataSeriesFileId)
                .queryParam("degree", 1L)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ApproximationPropertiesDto.class);

        ResponseMessage responseMessage = RestAssured.given()
                .when()
                .delete("/{approximationPropertiesId}", createdApproximationPropertiesDto.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ResponseMessage.class);

        assertThat(responseMessage.getMessage(), equalTo(
                "Deleted approximation properties with id: " + createdApproximationPropertiesDto.getId()
        ));

        RestAssured.given()
                .when()
                .get("/{approximationPropertiesId}", createdApproximationPropertiesDto.getId())
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNotExistingApproximationProperties() {
        RestAssured.given()
                .when()
                .delete("/{approximationPropertiesId}", 999999L)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private DataSeriesFileDto buildMockedDataSeriesFileDto(Long id) {
        List<PointXY> mockedPoints = new ArrayList<>();
        for (double i = 1.0; i <= 10.0; i++) {
            mockedPoints.add(new PointXY(i, i));
        }

        return DataSeriesFileDto.builder()
                .id(id)
                .userId(1L)
                .name("mocked_file.csv")
                .hashName(UUID.randomUUID().toString())
                .dateSent(new Timestamp(System.currentTimeMillis()))
                .size(10)
                .variance(8.25)
                .standardDeviation(2.8722813232690)
                .deleted(Boolean.FALSE)
                .points(mockedPoints)
                .build();
    }
}
