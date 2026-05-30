package pl.leastsquaresalgorithms.approximationservice.calculate;

import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.approximation.Approximation;
import pl.least_squares_algorithms.core.approximation.PolynomialApproximation;
import pl.least_squares_algorithms.core.function.MathematicalFunction;
import pl.least_squares_algorithms.core.mapper.MathematicalFunctionMapper;
import pl.least_squares_algorithms.core.dto.ApproximationDto;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;

import java.util.List;

public class ApproximationCalculate implements Runnable {
    private static final int NUMBER_SAMPLES = 20;

    private final ChosenMethodDto chosenMethodDto;
    private final List<PointXY> points;
    private final ApproximationDto approximationDto;

    public ApproximationCalculate(ChosenMethodDto chosenMethodDto, List<PointXY> points, ApproximationDto approximationDto) {
        this.chosenMethodDto = chosenMethodDto;
        this.points = points;
        this.approximationDto = approximationDto;
    }

    @Override
    public void run() {
        int degree = chosenMethodDto.getDegree();
        Approximation approximation = new PolynomialApproximation(points, degree);
        List<MathematicalFunction> mathematicalFunctions = approximation.doApproximations(chosenMethodDto.getLeastSquaresMethod());
        approximationDto.setMathematicalFunctionDtos(MathematicalFunctionMapper.mapToMathematicalFunctionDTOs(mathematicalFunctions));
        approximationDto.setPoints(approximation.getApproximationsPoints(NUMBER_SAMPLES * points.size()));
        approximationDto.setAbsoluteError(approximation.getAbsoluteError());
        approximationDto.setConvergenceCoefficient(approximation.getConvergenceCoefficient());
    }
}