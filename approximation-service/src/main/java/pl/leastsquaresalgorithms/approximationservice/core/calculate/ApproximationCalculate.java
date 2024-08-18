package pl.leastsquaresalgorithms.approximationservice.core.calculate;

import pl.leastsquaresalgorithms.approximationservice.core.approximation.Approximation;
import pl.leastsquaresalgorithms.approximationservice.core.approximation.PolynomialApproximation;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationDto;
import pl.leastsquaresalgorithms.approximationservice.dto.ChosenMethodDto;
import pl.leastsquaresalgorithms.approximationservice.dto.PointXY;
import pl.leastsquaresalgorithms.approximationservice.mapper.MathematicalFunctionMapper;


import java.util.List;

public class ApproximationCalculate implements Runnable {
    private static final int NUMBER_SAMPLES = 20;

    private final ChosenMethodDto chosenMethodDTO;
    private final List<PointXY> points;
    private final ApproximationDto approximationDTO;
    private final MathematicalFunctionMapper mathematicalFunctionMapper;

    public ApproximationCalculate(ChosenMethodDto chosenMethodDTO, List<PointXY> points, ApproximationDto approximationDTO, MathematicalFunctionMapper mathematicalFunctionMapper) {
        this.chosenMethodDTO = chosenMethodDTO;
        this.points = points;
        this.approximationDTO = approximationDTO;
        this.mathematicalFunctionMapper = mathematicalFunctionMapper;
    }

    @Override
    public void run() {
        int degree = chosenMethodDTO.getDegree();
        Approximation approximation = new PolynomialApproximation(points, degree);
        approximationDTO.setMathematicalFunctionDtos(mathematicalFunctionMapper.mapToMathematicalFunctionDTOs(approximation.doApproximations(chosenMethodDTO.getLeastSquaresMethod())));
        approximationDTO.setPoints(approximation.getApproximationsPoints(NUMBER_SAMPLES * points.size()));
        approximationDTO.setAbsoluteError(approximation.getAbsoluteError());
        approximationDTO.setConvergenceCoefficient(approximation.getConvergenceCoefficient());
    }
}