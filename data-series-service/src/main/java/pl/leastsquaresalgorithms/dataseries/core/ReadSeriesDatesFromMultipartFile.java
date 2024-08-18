package pl.leastsquaresalgorithms.dataseries.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import pl.leastsquaresalgorithms.dataseries.dto.PointXY;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadSeriesDatesFromMultipartFile extends ReadSeriesDates {
    private static final Logger logger = LoggerFactory.getLogger(ReadSeriesDatesFromMultipartFile.class);

    private final MultipartFile dataSeriesMultipartFile;

    public ReadSeriesDatesFromMultipartFile(DataSeriesFileEntity dataSeriesFile, MultipartFile dataSeriesMultipartFile) {
        super(dataSeriesFile);
        this.dataSeriesMultipartFile = dataSeriesMultipartFile;
    }

    @Override
    public void run() {
        List<PointXY> points = getPoints(dataSeriesMultipartFile);

        this.dataSeriesFile.setPoints(points);
        this.dataSeriesFile.setSize(points.size());

        logger.debug("The file was read correctly: {}", this.dataSeriesMultipartFile.getOriginalFilename());
    }

    private List<PointXY> getPoints(MultipartFile dataSeriesMultipartFile) {
        List<PointXY> points = new ArrayList<>();
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataSeriesMultipartFile.getInputStream()));

            while ((line = bufferedReader.readLine()) != null) {
                parseLine(points, line, line.split(REGEX_SPLIT), logger);
            }

            Collections.sort(points);
        } catch (IOException e) {
            logger.error("{}", e.getMessage(), e);
        }

        logger.info("Points have been loaded.");
        return points;
    }
}
