package pl.least_squares_algorithms.core.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;
import pl.least_squares_algorithms.core.function.MathematicalFunction;


import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MathematicalFunctionMapper {

    public static List<MathematicalFunctionDto> mapToMathematicalFunctionDTOs(List<MathematicalFunction> mathematicalFunctions) {
        return mathematicalFunctions.stream()
                .map(MathematicalFunctionMapper::mapToMathematicalFunctionDTO)
                .collect(Collectors.toList());
    }

    public static MathematicalFunctionDto mapToMathematicalFunctionDTO(MathematicalFunction mathematicalFunction) {
        return MathematicalFunctionDto.builder()
                .polynomialDto(PolynomialMapper.mapToPolynomialDTO(mathematicalFunction.polynomial()))
                .domainFunction(mathematicalFunction.domainFunction())
                .build();
    }
}
