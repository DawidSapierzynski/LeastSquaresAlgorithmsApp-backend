package pl.leastsquaresalgorithms.approximationservice.mapper;

import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.AlgebraicPolynomial;
import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.Polynomial;
import pl.leastsquaresalgorithms.approximationservice.core.function.polynomials.TrigonometricPolynomial;
import pl.leastsquaresalgorithms.approximationservice.dto.PolynomialDto;


@Service
public class PolynomialMapper {

    public PolynomialDto mapToPolynomialDTO(Polynomial polynomial) {
        return PolynomialDto.builder()
                .coefficients(polynomial.getCoefficients())
                .degree(polynomial.getDegree())
                .build();
    }

    public Polynomial mapToPolynomial(PolynomialDto polynomialDTO, boolean isTrigonometricPolynomial) {
        if (isTrigonometricPolynomial) {
            return new TrigonometricPolynomial(polynomialDTO.getCoefficients());
        } else {
            return new AlgebraicPolynomial(polynomialDTO.getCoefficients());
        }
    }
}
