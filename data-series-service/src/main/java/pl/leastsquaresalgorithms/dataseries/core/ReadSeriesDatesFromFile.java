package pl.leastsquaresalgorithms.dataseries.core;

import lombok.extern.slf4j.Slf4j;
import pl.leastsquaresalgorithms.dataseries.configuration.FileStorageProperties;
import pl.leastsquaresalgorithms.dataseries.dto.PointXY;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class ReadSeriesDatesFromFile extends ReadSeriesDates {
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

        log.debug("The file was read correctly: {}", seriesDatesName);
    }

    private List<PointXY> getPoints(String seriesDatesPath) {
        List<PointXY> points = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(seriesDatesPath));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                parseLine(points, line, line.split(REGEX_SPLIT), log);
            }

            Collections.sort(points);
        } catch (FileNotFoundException | NumberFormatException e) {
            log.error("{}", e.getMessage());
        }

        log.info("Points have been loaded.");
        return points;
    }
}
