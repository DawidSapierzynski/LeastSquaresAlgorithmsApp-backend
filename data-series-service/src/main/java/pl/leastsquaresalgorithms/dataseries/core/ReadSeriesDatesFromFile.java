package pl.leastsquaresalgorithms.dataseries.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.leastsquaresalgorithms.dataseries.configuration.FileStorageProperties;
import pl.leastsquaresalgorithms.dataseries.dto.PointXY;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ReadSeriesDatesFromFile extends ReadSeriesDates {
    private final Logger logger = LoggerFactory.getLogger(ReadSeriesDatesFromFile.class);

    private final String seriesDatesPath;
    private final String seriesDatesName;

    public ReadSeriesDatesFromFile(String seriesDatesName, DataSeriesFileEntity dataSeriesFile, FileStorageProperties fileStorageProperties) {
        super(dataSeriesFile);
        this.seriesDatesName = seriesDatesName;
        this.seriesDatesPath = fileStorageProperties.getUploadDir() + "\\" + seriesDatesName;
    }

    @Override
    public void run() {
        List<PointXY> points = getPoints(this.seriesDatesPath);

        this.dataSeriesFile.setPoints(points);
        this.dataSeriesFile.setSize(points.size());

        logger.debug("The file was read correctly: {}", seriesDatesName);
    }

    private List<PointXY> getPoints(String seriesDatesPath) {
        List<PointXY> points = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(seriesDatesPath));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                parseLine(points, line, line.split(REGEX_SPLIT), logger);
            }

            Collections.sort(points);
        } catch (FileNotFoundException | NumberFormatException e) {
            logger.error("{}", e.getMessage());
        }

        logger.info("Points have been loaded.");
        return points;
    }
}
