package pl.leastsquaresalgorithms.approximationservice.core.function;

public record DomainFunction(boolean leftClosedInterval, double beginningInterval, double endInterval,
                             boolean rightClosedInterval) {

    @Override
    public String toString() {
        return (leftClosedInterval() ? "<" : "(") + beginningInterval() + ";" + endInterval() + (rightClosedInterval() ? ">" : ")");
    }
}
