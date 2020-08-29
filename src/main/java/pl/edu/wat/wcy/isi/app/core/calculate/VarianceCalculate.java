package pl.edu.wat.wcy.isi.app.core.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.wat.wcy.isi.app.model.PointXY;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;

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
