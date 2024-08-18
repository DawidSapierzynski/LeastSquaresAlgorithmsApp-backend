package pl.leastsquaresalgorithms.approximationservice.mapper;

import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationservice.core.function.MathematicalFunction;
import pl.leastsquaresalgorithms.approximationservice.dto.MathematicalFunctionDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MathematicalFunctionMapper {

    private final PolynomialMapper polynomialMapper;

    public MathematicalFunctionMapper(PolynomialMapper polynomialMapper) {
        this.polynomialMapper = polynomialMapper;
    }

    public List<MathematicalFunctionDto> mapToMathematicalFunctionDTOs(List<MathematicalFunction> mathematicalFunctions) {
        return mathematicalFunctions.stream()
                .map(this::mapToMathematicalFunctionDTO)
                .collect(Collectors.toList());
    }

    public MathematicalFunctionDto mapToMathematicalFunctionDTO(MathematicalFunction mathematicalFunction) {
        return MathematicalFunctionDto.builder()
                .polynomialDTO(polynomialMapper.mapToPolynomialDTO(mathematicalFunction.polynomial()))
                .domainFunction(mathematicalFunction.domainFunction())
                .build();
    }
}
