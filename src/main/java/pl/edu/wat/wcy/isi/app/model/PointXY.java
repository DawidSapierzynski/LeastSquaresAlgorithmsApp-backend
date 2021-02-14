package pl.edu.wat.wcy.isi.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointXY implements Comparable<PointXY> {
    private double x;
    private double y;
    private double weight;

    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
        this.weight = 1;
    }

    @Override
    public String toString() {
        return "[" + x + "; " + y + "]";
    }

    @Override
    public int compareTo(PointXY otherPoint) {
        double difference = this.getX() - otherPoint.getX();
        return difference > 0 ? 1 : (difference < 0 ? -1 : 0);
    }
}
