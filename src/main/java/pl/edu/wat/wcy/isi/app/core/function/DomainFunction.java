package pl.edu.wat.wcy.isi.app.core.function;

public class DomainFunction {
    private final boolean leftClosedInterval;
    private final boolean rightClosedInterval;
    private final double beginningInterval;
    private final double endInterval;

    public DomainFunction(boolean leftClosedInterval, double beginningInterval, double endInterval, boolean rightClosedInterval) {
        this.leftClosedInterval = leftClosedInterval;
        this.rightClosedInterval = rightClosedInterval;
        this.beginningInterval = beginningInterval;
        this.endInterval = endInterval;
    }

    public boolean isLeftClosedInterval() {
        return leftClosedInterval;
    }

    public boolean isRightClosedInterval() {
        return rightClosedInterval;
    }

    public double getBeginningInterval() {
        return beginningInterval;
    }

    public double getEndInterval() {
        return endInterval;
    }

    @Override
    public String toString() {
        return (isLeftClosedInterval() ? "<" : "(") + getBeginningInterval() + ";" + getEndInterval() + (isRightClosedInterval() ? ">" : ")");
    }
}
