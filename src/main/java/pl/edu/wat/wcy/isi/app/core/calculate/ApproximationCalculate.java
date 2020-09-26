package pl.edu.wat.wcy.isi.app.core.calculate;

import pl.edu.wat.wcy.isi.app.core.approximation.Approximation;
import pl.edu.wat.wcy.isi.app.core.approximation.PolynomialApproximation;
import pl.edu.wat.wcy.isi.app.dto.ApproximationDTO;
import pl.edu.wat.wcy.isi.app.dto.ChosenMethodDTO;
import pl.edu.wat.wcy.isi.app.mapper.MathematicalFunctionMapper;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.List;

public class ApproximationCalculate implements Runnable {
    private static final int NUMBER_SAMPLES = 20;

    private final ChosenMethodDTO chosenMethodDTO;
    private final List<PointXY> points;
    private final ApproximationDTO approximationDTO;
    private final MathematicalFunctionMapper mathematicalFunctionMapper;

    public ApproximationCalculate(ChosenMethodDTO chosenMethodDTO, List<PointXY> points, ApproximationDTO approximationDTO, MathematicalFunctionMapper mathematicalFunctionMapper) {
        this.chosenMethodDTO = chosenMethodDTO;
        this.points = points;
        this.approximationDTO = approximationDTO;
        this.mathematicalFunctionMapper = mathematicalFunctionMapper;
    }

    @Override
    public void run() {
        int degree = chosenMethodDTO.getDegree();
        Approximation approximation = new PolynomialApproximation(points, degree);

        approximationDTO.setMathematicalFunctionDTOs(mathematicalFunctionMapper.mapToMathematicalFunctionDTOs(approximation.doApproximations(chosenMethodDTO.getLeastSquaresMethod())));
        approximationDTO.setPoints(approximation.getApproximationsPoints(NUMBER_SAMPLES * points.size()));
        approximationDTO.setAbsoluteError(approximation.getAbsoluteError());
    }
}