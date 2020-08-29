package pl.edu.wat.wcy.isi.app.core.calculate;

import org.slf4j.Logger;
import pl.edu.wat.wcy.isi.app.model.PointXY;
import pl.edu.wat.wcy.isi.app.model.entityModels.DataSeriesFileEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class ReadSeriesDates implements Runnable {
    protected static final double MAX_MULTIPLE_DEFAULT = 60;
    protected static final int CYCLE_SIZE = 10;
    protected static final double MIN_Y = 0.01;
    protected static final String REGEX_SPLIT = "[;,]";
    protected double maxMultiple;

    protected final DataSeriesFileEntity dataSeriesFile;

    public ReadSeriesDates(DataSeriesFileEntity dataSeriesFile) {
        this.dataSeriesFile = dataSeriesFile;
    }

    protected static void parseLine(List<PointXY> points, String line, String[] split, Logger logger) {
        line = line.trim();
        if (line.startsWith("//"))
            return;

        if (split.length == 2) {
            double x, y;

            x = Double.parseDouble(split[0].trim());
            y = Double.parseDouble(split[1].trim());

            PointXY pointXY = new PointXY(x, y);
            points.add(pointXY);
            logger.debug("Add point: {}", pointXY);
        } else {
            logger.error("Not prase line: {}", line);
        }
    }

    protected List<PointXY> getArtefact(List<PointXY> points, Logger logger) {
        List<PointXY> artefacts = new ArrayList<>();
        List<Double> quotientsList = new ArrayList<>();

        for (int i = 0; i < points.size() - 1; i++) {
            quotientsList.add(Math.abs(points.get(i + 1).getY() / points.get(i).getY()));
        }

        double quotientsMean = getTrimmedMean(quotientsList) * 40;
        maxMultiple = Double.isFinite(quotientsMean) ? quotientsMean : MAX_MULTIPLE_DEFAULT;
        logger.info("maxMultiple = {}", maxMultiple);

        for (int i = 0; i < quotientsList.size() - 1; i++) {
            if ((checkValuesZero(quotientsList.get(i), quotientsList.get(i + 1)) || Math.abs(points.get(i + 1).getY()) < MIN_Y) && i > 0 && i < points.size() - 3) {
                double dif1 = Math.abs(points.get(i).getY() - points.get(i - 1).getY());
                double dif2 = Math.abs(points.get(i + 1).getY() - points.get(i).getY());
                double dif3 = Math.abs(points.get(i + 2).getY() - points.get(i + 1).getY());
                double dif4 = Math.abs(points.get(i + 3).getY() - points.get(i + 2).getY());

                if (dif1 * maxMultiple < dif2 && dif3 > dif4 * maxMultiple) {
                    artefacts.add(points.get(i + 1));
                }

            } else if (checkValuesQuotients(quotientsList.get(i), quotientsList.get(i + 1))) {
                artefacts.add(points.get(i + 1));
            }
        }

        points.removeAll(artefacts);

        logger.debug("Artefacts: {}", artefacts);

        return artefacts;
    }

    public static boolean checkPoints(List<PointXY> points) {
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i + 1).getX() - points.get(i).getX() == 0d) {
                return false;
            }
        }
        return true;
    }

    private boolean checkValuesQuotients(double value1, double value2) {
        return ((value1 >= maxMultiple && value2 <= 1.0 / maxMultiple)
                || (value2 >= maxMultiple && value1 <= 1.0 / maxMultiple))
                && value1 != 0
                && value2 != 0;
    }

    private boolean checkValuesZero(double value1, double value2) {
        return ((value1 == 0 && Double.isInfinite(value2))
                || (value2 == 0 && Double.isInfinite(value1)));
    }

    private double getTrimmedMean(List<Double> valuesList) {
        List<Double> list = new ArrayList<>(List.copyOf(valuesList));

        list.sort(Double::compareTo);

        for (int i = 0; i < valuesList.size(); i += CYCLE_SIZE) {
            list.remove(0);
            list.remove(list.size() - 1);
        }

        return list.stream()
                .mapToDouble((v) -> v)
                .average()
                .orElse(0d);
    }
}
