package pl.leastsquaresalgorithms.approximationservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import pl.leastsquaresalgorithms.approximationservice.core.LeastSquaresMethod;
import pl.leastsquaresalgorithms.approximationservice.core.function.DomainFunction;
import pl.leastsquaresalgorithms.approximationservice.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApproximationControllerTests {
    public static final double DELTA = 1.0e-12;
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
    void shouldDoApproximationForLinearFunctionWithNormalizationMethod() {
        shouldDoApproximationForLinearFunction(LeastSquaresMethod.NORMALIZATION);
    }

    @Test
    void shouldDoApproximationForThirdDegreeFunctionWithNormalizationMethod() {
        shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod.NORMALIZATION);
    }

    @Test
    void shouldDoApproximationForLinearFunctionWithQRHouseholderTransformationMethod() {
        shouldDoApproximationForLinearFunction(LeastSquaresMethod.HOUSEHOLDER_TRANSFORMATION);
    }

    @Test
    void shouldDoApproximationForThirdDegreeFunctionWithQRHouseholderTransformationMethod() {
        shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod.HOUSEHOLDER_TRANSFORMATION);
    }

    @Test
    void shouldDoApproximationForLinearFunctionWithQRGivensRotationMethod() {
        shouldDoApproximationForLinearFunction(LeastSquaresMethod.GIVENS_ROTATION);
    }

    @Test
    void shouldDoApproximationForThirdDegreeFunctionWithQRGivensRotationMethod() {
        shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod.GIVENS_ROTATION);
    }

    @Test
    void shouldDoApproximationForLinearFunctionWithSingularValueDecompositionMethod() {
        shouldDoApproximationForLinearFunction(LeastSquaresMethod.SINGULAR_VALUE_DECOMPOSITION);
    }

    @Test
    void shouldDoApproximationForThirdDegreeFunctionWithSingularValueDecompositionMethod() {
        shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod.SINGULAR_VALUE_DECOMPOSITION);
    }

    private void shouldDoApproximationForLinearFunction(LeastSquaresMethod normalization) {
        int degree = 1;
        List<PointXY> points = preparePoints(x -> -x);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(normalization, degree))
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

        PolynomialDto polynomialDto = mathematicalFunctionDtos.getFirst().getPolynomialDTO();
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

    private void shouldDoApproximationForThirdDegreeFunction(LeastSquaresMethod householderTransformation) {
        int degree = 3;
        List<PointXY> points = preparePoints(x -> 3 * x * x * x + 2 * x * x + -10 * x + 7);
        ApproximationForm approximationForm = ApproximationForm.builder()
                .chosenMethod(new ChosenMethodDto(householderTransformation, degree))
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

        PolynomialDto polynomialDto = mathematicalFunctionDtos.getFirst().getPolynomialDTO();
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

    private List<PointXY> preparePoints(Function<Double, Double> function) {
        List<PointXY> points = new ArrayList<>();
        for (double x = -5; x <= 5; x++) {
            points.add(new PointXY(x, function.apply(x)));
        }
        return points;
    }
}
