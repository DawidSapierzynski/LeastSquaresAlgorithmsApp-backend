package pl.leastsquaresalgorithms.approximationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.leastsquaresalgorithms.approximationservice.core.calculate.ApproximationCalculate;
import pl.leastsquaresalgorithms.approximationservice.dto.ApproximationDto;
import pl.leastsquaresalgorithms.approximationservice.dto.ChosenMethodDto;
import pl.leastsquaresalgorithms.approximationservice.dto.PointXY;
import pl.leastsquaresalgorithms.approximationservice.mapper.MathematicalFunctionMapper;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ApproximationService {
    private static final Logger logger = LoggerFactory.getLogger(ApproximationService.class);

    private final ExecutorService threadPool;
    private final MathematicalFunctionMapper mathematicalFunctionMapper;

    public ApproximationService(@Value("${number.threads}") int nThreads, MathematicalFunctionMapper mathematicalFunctionMapper) {
        this.threadPool = Executors.newFixedThreadPool(nThreads);
        this.mathematicalFunctionMapper = mathematicalFunctionMapper;
    }

    public ApproximationDto doApproximations(ChosenMethodDto chosenMethodDTO, List<PointXY> points) {
        ApproximationDto approximationDTO = new ApproximationDto();
        List<Callable<Object>> callables = Collections.singletonList(Executors.callable(new ApproximationCalculate(chosenMethodDTO, points, approximationDTO, mathematicalFunctionMapper)));
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            logger.debug("ApproximationCalculate - isDone: {}", futures.getFirst().isDone());
        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage(), e);
        }
        return approximationDTO;
    }
}
