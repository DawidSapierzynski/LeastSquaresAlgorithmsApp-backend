package pl.leastsquaresalgorithms.approximationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.dto.ApproximationDto;
import pl.least_squares_algorithms.core.dto.ChosenMethodDto;
import pl.least_squares_algorithms.core.dto.MathematicalFunctionDto;
import pl.leastsquaresalgorithms.approximationservice.calculate.ApproximationCalculate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ApproximationService {
    private final ExecutorService threadPool;

    public ApproximationService(@Value("${number.threads}") int nThreads) {
        this.threadPool = Executors.newFixedThreadPool(nThreads);
    }

    public ApproximationDto doApproximations(ChosenMethodDto chosenMethodDTO, List<PointXY> points) {
        ApproximationDto approximationDTO = new ApproximationDto();
        List<Callable<Object>> callables = Collections.singletonList(Executors.callable(new ApproximationCalculate(chosenMethodDTO, points, approximationDTO)));
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            log.debug("ApproximationCalculate - isDone: {}", futures.getFirst().isDone());
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }
        return approximationDTO;
    }

    public byte[] getApproximationResult(List<MathematicalFunctionDto> mathematicalFunctionDtos) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Double> coefficients;

        for (MathematicalFunctionDto m : mathematicalFunctionDtos) {
            stringBuilder.append(m.getDomainFunction()).append("\n");
            coefficients = m.getPolynomialDto().getCoefficients();
            for (int i = 0; i < coefficients.size(); i++) {
                stringBuilder.append("a")
                        .append(i)
                        .append("=")
                        .append(coefficients.get(i))
                        .append("\n");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
