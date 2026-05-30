package pl.least_squares_algorithms.core.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.least_squares_algorithms.core.dto.PolynomialDto;
import pl.least_squares_algorithms.core.function.polynomials.AlgebraicPolynomial;
import pl.least_squares_algorithms.core.function.polynomials.Polynomial;
import pl.least_squares_algorithms.core.function.polynomials.TrigonometricPolynomial;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PolynomialMapper {

    public static PolynomialDto mapToPolynomialDTO(Polynomial polynomial) {
        return PolynomialDto.builder()
                .coefficients(polynomial.getCoefficients())
                .degree(polynomial.getDegree())
                .build();
    }

    public static Polynomial mapToPolynomial(PolynomialDto polynomialDTO, boolean isTrigonometricPolynomial) {
        if (isTrigonometricPolynomial) {
            return new TrigonometricPolynomial(polynomialDTO.getCoefficients());
        } else {
            return new AlgebraicPolynomial(polynomialDTO.getCoefficients());
        }
    }
}
