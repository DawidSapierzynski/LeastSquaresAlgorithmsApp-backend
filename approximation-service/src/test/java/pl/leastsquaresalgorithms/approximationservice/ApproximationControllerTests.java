package pl.leastsquaresalgorithms.approximationservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pl.least_squares_algorithms.core.LeastSquaresMethod;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.dto.ApproximationDto;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;
import pl.least_squares_algorithms.core.function.DomainFunction;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationForm;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApproximationControllerTests {
    public static final double DELTA = 1.0e-8;
    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost/api/approximation";
        RestAssured.port = port;
    }

    @Test
    void shouldGetMethods() {
        Integer degree = 3;
        RestAssured.given()
                .queryParam("degree", degree)
                .accept(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("degree[0]", equalTo(degree),
                        "leastSquaresMethod[0]", equalTo(LeastSquaresMethod.NORMALIZATION.name()),
                        "degree[1]", equalTo(degree),
                        "leastSquaresMethod[1]", equalTo(LeastSquaresMethod.HOUSEHOLDER_TRANSFORMATION.name()),
                        "degree[2]", equalTo(degree),
                        "leastSquaresMethod[2]", equalTo(LeastSquaresMethod.GIVENS_ROTATION.name()),
                        "degree[3]", equalTo(degree),
                        "leastSquaresMethod[3]", equalTo(LeastSquaresMethod.SINGULAR_VALUE_DECOMPOSITION.name()));
    }

    @Test
    void shouldDoApproximationWithoutChosenMethod() {
        int degree = 1;
        List<PointXY> points = preparePoints(x -> -x);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .points(points)
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(null, degree))
                .points(points)
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @EnumSource(LeastSquaresMethod.class)
    void shouldDoApproximationWithoutDegree(LeastSquaresMethod method) {
        List<PointXY> points = preparePoints(x -> -x);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(method, null))
                .points(points)
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldDoApproximationWithOnePoint() {
        List<PointXY> points = List.of(new PointXY(0.0, 0.0));
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(LeastSquaresMethod.NORMALIZATION, 1))
                .points(points)
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldDoApproximationWithoutPoints() {
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(LeastSquaresMethod.NORMALIZATION, 1))
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldDoApproximationWithMorePoints() {
        List<PointXY> points = IntStream.range(0, 10_001).mapToObj(i -> new PointXY(i, -i)).collect(Collectors.toList());
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(LeastSquaresMethod.NORMALIZATION, 1))
                .points(points)
                .build();

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @ParameterizedTest
    @EnumSource(LeastSquaresMethod.class)
    void shouldDoApproximationForLinearFunction(LeastSquaresMethod method) {
        int degree = 1;
        List<PointXY> points = preparePoints(x -> -x);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(method, degree))
                .points(points)
                .build();

        ApproximationDto approximationDto = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ApproximationDto.class);

        assertThat(approximationDto.getAbsoluteError(), notNullValue());
        assertThat(approximationDto.getConvergenceCoefficient(), notNullValue());

        assertThat(approximationDto.getPoints(), hasSize(points.size() * 20));

        List<MathematicalFunctionDto> mathematicalFunctionDtos = approximationDto.getMathematicalFunctionDtos();
        assertThat(mathematicalFunctionDtos.size(), equalTo(1));

        pl.least_squares_algorithms.core.dto.PolynomialDto polynomialDto = mathematicalFunctionDtos.getFirst().getPolynomialDto();
        assertThat(polynomialDto.getDegree(), equalTo(degree));

        List<Double> coefficients = polynomialDto.getCoefficients();
        assertThat(coefficients.size(), equalTo(degree + 1));
        assertThat(coefficients.get(0), closeTo(0.0, DELTA));
        assertThat(coefficients.get(1), closeTo(-1.0, DELTA));

        DomainFunction domainFunction = mathematicalFunctionDtos.getFirst().getDomainFunction();
        assertThat(domainFunction.beginningInterval(), equalTo(-5.0));
        assertThat(domainFunction.leftClosedInterval(), equalTo(true));
        assertThat(domainFunction.endInterval(), equalTo(5.0));
        assertThat(domainFunction.rightClosedInterval(), equalTo(true));
    }

    @ParameterizedTest
    @EnumSource(LeastSquaresMethod.class)
    void shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod method) {
        int degree = 3;
        List<PointXY> points = preparePoints(x -> 3 * x * x * x + 2 * x * x + -10 * x + 7);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(method, degree))
                .points(points)
                .build();

        ApproximationDto approximationDto = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(approximationForm)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ApproximationDto.class);

        assertThat(approximationDto.getAbsoluteError(), notNullValue());
        assertThat(approximationDto.getConvergenceCoefficient(), notNullValue());

        assertThat(approximationDto.getPoints(), hasSize(points.size() * 20));

        List<MathematicalFunctionDto> mathematicalFunctionDtos = approximationDto.getMathematicalFunctionDtos();
        assertThat(mathematicalFunctionDtos.size(), equalTo(1));

        pl.least_squares_algorithms.core.dto.PolynomialDto polynomialDto = mathematicalFunctionDtos.getFirst().getPolynomialDto();
        assertThat(polynomialDto.getDegree(), equalTo(degree));

        List<Double> coefficients = polynomialDto.getCoefficients();
        assertThat(coefficients.size(), equalTo(4));
        assertThat(coefficients.get(0), closeTo(7.0, DELTA));
        assertThat(coefficients.get(1), closeTo(-10.0, DELTA));
        assertThat(coefficients.get(2), closeTo(2.0, DELTA));
        assertThat(coefficients.get(3), closeTo(3.0, DELTA));

        DomainFunction domainFunction = mathematicalFunctionDtos.getFirst().getDomainFunction();
        assertThat(domainFunction.beginningInterval(), equalTo(-5.0));
        assertThat(domainFunction.leftClosedInterval(), equalTo(true));
        assertThat(domainFunction.endInterval(), equalTo(5.0));
        assertThat(domainFunction.rightClosedInterval(), equalTo(true));
    }

    @Test
    void shouldDownloadApproximationResult() {
        List<MathematicalFunctionDto> mathematicalFunctionDtos = List.of(
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(true, -5.0, 5.0, true))
                        .polynomialDto(
                                pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                        .degree(1)
                                        .coefficients(List.of(2.0, -1.0))
                                        .build()
                        ).build()
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mathematicalFunctionDtos)
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.OK.value())
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION, containsString("attachment"))
                .header(HttpHeaders.CONTENT_DISPOSITION, containsString("approximation-points.txt"))
                .body(notNullValue());
    }

    @Test
    void shouldDownloadNotEmptyApproximationResultFile() {
        List<MathematicalFunctionDto> mathematicalFunctionDtos = List.of(
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(true, -5.0, 5.0, true))
                        .polynomialDto(pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                .degree(1)
                                .coefficients(List.of(2.0, -1.0))
                                .build())
                        .build()
        );

        byte[] fileContent = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mathematicalFunctionDtos)
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.OK.value())
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN_VALUE))
                .extract()
                .asByteArray();

        assertThat(fileContent, notNullValue());
        assertThat(fileContent.length, greaterThan(0));
    }

    @Test
    void shouldDownloadApproximationResultWithDomainFunctionAndPolynomialCoefficients() {
        List<MathematicalFunctionDto> mathematicalFunctionDtos = List.of(
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(true, -5.0, 5.0, true))
                        .polynomialDto(pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                .degree(2)
                                .coefficients(List.of(7.0, -10.0, 2.0))
                                .build())
                        .build()
        );

        byte[] fileContent = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mathematicalFunctionDtos)
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.OK.value())
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN_VALUE))
                .extract()
                .asByteArray();

        String textContent = new String(fileContent, StandardCharsets.UTF_8);

        assertThat(textContent, containsString("<-5.0;5.0>"));
        assertThat(textContent, containsString("a0=7.0"));
        assertThat(textContent, containsString("a1=-10.0"));
        assertThat(textContent, containsString("a2=2.0"));
    }

    @Test
    void shouldDownloadApproximationResultForManyFunctions() {
        List<MathematicalFunctionDto> mathematicalFunctionDtos = List.of(
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(true, -10.0, 0.0, true))
                        .polynomialDto(pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                .degree(1)
                                .coefficients(List.of(2.0, -1.0))
                                .build())
                        .build(),
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(false, 0.0, 10.0, true))
                        .polynomialDto(pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                .degree(2)
                                .coefficients(List.of(1.0, 0.0, 3.5))
                                .build())
                        .build()
        );

        byte[] fileContent = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(mathematicalFunctionDtos)
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.OK.value())
                .header(HttpHeaders.CONTENT_TYPE, containsString(MediaType.TEXT_PLAIN_VALUE))
                .extract()
                .asByteArray();

        String textContent = new String(fileContent, StandardCharsets.UTF_8);

        assertThat(textContent, containsString("<-10.0;0.0>"));
        assertThat(textContent, containsString("a0=2.0"));
        assertThat(textContent, containsString("a1=-1.0"));

        assertThat(textContent, containsString("(0.0;10.0>"));
        assertThat(textContent, containsString("a0=1.0"));
        assertThat(textContent, containsString("a1=0.0"));
        assertThat(textContent, containsString("a2=3.5"));
    }

    @Test
    void shouldDownloadEmptyApproximationResultForEmptyFunctionList() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(List.of())
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void shouldReturnUnsupportedMediaTypeWhenDownloadingWithoutJsonContentType() {
        List<MathematicalFunctionDto> mathematicalFunctionDtos = List.of(
                MathematicalFunctionDto.builder()
                        .domainFunction(new DomainFunction(true, -5.0, 5.0, true))
                        .polynomialDto(pl.least_squares_algorithms.core.dto.PolynomialDto.builder()
                                .degree(1)
                                .coefficients(List.of(2.0, -1.0))
                                .build())
                        .build()
        );

        RestAssured.given()
                .contentType(ContentType.TEXT)
                .body(mathematicalFunctionDtos.toString())
                .when()
                .post("/download")
                .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }

    private List<PointXY> preparePoints(Function<Double, Double> function) {
        List<PointXY> points = new ArrayList<>();
        for (double x = -5; x <= 5; x++) {
            points.add(new PointXY(x, function.apply(x)));
        }
        return points;
    }
}
