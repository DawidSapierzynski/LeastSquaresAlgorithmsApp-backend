package pl.leastsquaresalgorithms.dataseries.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.leastsquaresalgorithms.dataseries.dto.PointXY;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.util.List;

public class VarianceCalculate implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(VarianceCalculate.class);
    private final DataSeriesFileEntity dataSeriesFile;

    public VarianceCalculate(DataSeriesFileEntity dataSeriesFile) {
        this.dataSeriesFile = dataSeriesFile;
    }

    @Override
    public void run() {
        List<PointXY> points = dataSeriesFile.getPoints();
        double average, variance;

        average = points.stream().mapToDouble(PointXY::getY).sum() / points.size();
        logger.debug("Calculated average: {}", average);

        variance = points.stream().mapToDouble((p) -> Math.pow(p.getY() - average, 2)).sum() / points.size();
        logger.debug("Calculated variance: {}", average);

        dataSeriesFile.setVariance(variance);
        logger.info("Set variance: {}", dataSeriesFile.getVariance());

        dataSeriesFile.setStandardDeviation(Math.sqrt(variance));
        logger.info("Set standard deviation: {}", dataSeriesFile.getStandardDeviation());
    }
}
