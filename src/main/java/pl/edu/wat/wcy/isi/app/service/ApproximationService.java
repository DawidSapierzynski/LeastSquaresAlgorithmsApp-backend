package pl.edu.wat.wcy.isi.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.wat.wcy.isi.app.core.calculate.ApproximationCalculate;
import pl.edu.wat.wcy.isi.app.dto.ApproximationDTO;
import pl.edu.wat.wcy.isi.app.dto.ChosenMethodDTO;
import pl.edu.wat.wcy.isi.app.mapper.MathematicalFunctionMapper;
import pl.edu.wat.wcy.isi.app.model.PointXY;

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

    public void doApproximations(ChosenMethodDTO chosenMethodDTO, List<PointXY> points, ApproximationDTO approximationDTO) {
        List<Callable<Object>> callables = Collections.singletonList(Executors.callable(new ApproximationCalculate(chosenMethodDTO, points, approximationDTO, mathematicalFunctionMapper)));
        try {
            List<Future<Object>> futures = this.threadPool.invokeAll(callables);
            logger.debug("ApproximationCalculate - isDone: {}", futures.get(0).isDone());

        } catch (InterruptedException e) {
            logger.error("{}", e.getMessage());
        }
    }
}
