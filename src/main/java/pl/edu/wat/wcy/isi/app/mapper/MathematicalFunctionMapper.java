package pl.edu.wat.wcy.isi.app.mapper;

import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.core.function.MathematicalFunction;
import pl.edu.wat.wcy.isi.app.dto.MathematicalFunctionDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MathematicalFunctionMapper {

    private final PolynomialMapper polynomialMapper;

    public MathematicalFunctionMapper(PolynomialMapper polynomialMapper) {
        this.polynomialMapper = polynomialMapper;
    }

    public List<MathematicalFunctionDTO> mapToMathematicalFunctionDTOs(List<MathematicalFunction> mathematicalFunctions) {
        return mathematicalFunctions.stream()
                .map(this::mapToMathematicalFunctionDTO)
                .collect(Collectors.toList());
    }

    public MathematicalFunctionDTO mapToMathematicalFunctionDTO(MathematicalFunction mathematicalFunction) {
        return MathematicalFunctionDTO.builder()
                .polynomialDTO(polynomialMapper.mapToPolynomialDTO(mathematicalFunction.polynomial()))
                .domainFunction(mathematicalFunction.domainFunction())
                .build();
    }
}
