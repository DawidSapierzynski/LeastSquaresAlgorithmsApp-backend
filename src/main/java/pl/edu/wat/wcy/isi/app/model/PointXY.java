package pl.edu.wat.wcy.isi.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
