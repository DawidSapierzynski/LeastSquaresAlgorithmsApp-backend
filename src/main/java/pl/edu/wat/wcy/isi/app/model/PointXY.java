package pl.edu.wat.wcy.isi.app.model;

public class PointXY implements Comparable<PointXY> {
    private final double x;
    private final double y;

    public PointXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
