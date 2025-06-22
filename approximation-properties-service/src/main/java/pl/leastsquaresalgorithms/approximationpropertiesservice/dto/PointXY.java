package pl.leastsquaresalgorithms.approximationpropertiesservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class PointXY implements Comparable<PointXY> {
    private double x;
    private double y;
    private double beforeY;
    private double weight;

    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
        this.beforeY = Double.NEGATIVE_INFINITY;
        this.weight = 1;
    }

    public PointXY(double x, double y, double weight) {
        this.x = x;
        this.y = y;
        this.beforeY = 0.0;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "[" + x + "; " + y + "]";
    }

    public String toStringWithBeforeY() {
        if (beforeY != Double.NEGATIVE_INFINITY) {
            return "//" + x + ";" + beforeY + "\n" + x + ";" + y;
        } else {
            return x + ";" + y;
        }
    }

    public String toStringWithWeight() {
        if (beforeY != Double.NEGATIVE_INFINITY) {
            return "//" + x + ";" + beforeY + "\n" + x + ";" + y + ";" + weight;
        } else {
            return x + ";" + y + ";" + weight;
        }
    }

    @Override
    public int compareTo(PointXY otherPoint) {
        double difference = this.getX() - otherPoint.getX();
        return difference > 0 ? 1 : (difference < 0 ? -1 : 0);
    }

    public void addY(double y) {
        this.beforeY = this.y;
        this.y += y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PointXY pointXY = (PointXY) o;
        return Double.compare(x, pointXY.x) == 0
                && Double.compare(y, pointXY.y) == 0
                && Double.compare(beforeY, pointXY.beforeY) == 0
                && Double.compare(weight, pointXY.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, beforeY, weight);
    }
}
