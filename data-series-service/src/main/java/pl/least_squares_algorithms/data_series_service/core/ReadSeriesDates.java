package pl.least_squares_algorithms.data_series_service.core;

import org.slf4j.Logger;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.data_series_service.model.DataSeriesFileEntity;


import java.util.List;

public abstract class ReadSeriesDates implements Runnable {
    protected static final String REGEX_SPLIT = "[;,]";

    protected final DataSeriesFileEntity dataSeriesFile;

    public ReadSeriesDates(DataSeriesFileEntity dataSeriesFile) {
        this.dataSeriesFile = dataSeriesFile;
    }

    protected static void parseLine(List<PointXY> points, String line, String[] split, Logger log) {
        line = line.trim();
        if (line.startsWith("//"))
            return;

        if (split.length == 2) {
            double x, y;

            x = Double.parseDouble(split[0].trim());
            y = Double.parseDouble(split[1].trim());

            PointXY pointXY = new PointXY(x, y);
            points.add(pointXY);
            log.trace("Add point: {}", pointXY);
        } else if (split.length == 3) {
            double x, y, weight;

            x = Double.parseDouble(split[0].trim());
            y = Double.parseDouble(split[1].trim());
            weight = Double.parseDouble(split[2].trim());

            PointXY pointXY = new PointXY(x, y, weight);
            points.add(pointXY);
            log.trace("Add point: {}", pointXY);
        } else {
            log.error("Not parse line: {}", line);
        }
    }

    public static boolean checkPoints(List<PointXY> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i + 1).getX() - points.get(i).getX() == 0d) {
                return false;
            }
        }
        return true;
    }
}
