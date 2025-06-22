package pl.leastsquaresalgorithms.dataseries.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import pl.leastsquaresalgorithms.dataseries.dto.PointXY;
import pl.leastsquaresalgorithms.dataseries.model.DataSeriesFileEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ReadSeriesDatesFromMultipartFile extends ReadSeriesDates {
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
        log.debug("The file was read correctly: {}", this.dataSeriesMultipartFile.getOriginalFilename());
    }

    private List<PointXY> getPoints(MultipartFile dataSeriesMultipartFile) {
        List<PointXY> points = new ArrayList<>();
        try {
            String line;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataSeriesMultipartFile.getInputStream()));
            while ((line = bufferedReader.readLine()) != null) {
                parseLine(points, line, line.split(REGEX_SPLIT), log);
            }
            Collections.sort(points);
        } catch (IOException e) {
            log.error("{}", e.getMessage(), e);
        }
        log.info("Points have been loaded.");
        return points;
    }
}
