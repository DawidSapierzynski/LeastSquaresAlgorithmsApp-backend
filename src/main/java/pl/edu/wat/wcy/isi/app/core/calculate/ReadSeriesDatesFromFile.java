package pl.edu.wat.wcy.isi.app.core.calculate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.wat.wcy.isi.app.configuration.FileStorageProperties;
import pl.edu.wat.wcy.isi.app.model.PointXY;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;

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
        List<PointXY> artefacts = getArtefact(points, logger);

        this.dataSeriesFile.setArtefacts(artefacts);
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
