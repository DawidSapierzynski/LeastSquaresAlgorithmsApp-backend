package pl.least_squares_algorithms.core.function;

public record DomainFunction(boolean leftClosedInterval, double beginningInterval, double endInterval,
                             boolean rightClosedInterval) {

    @Override
    public String toString() {
        return (leftClosedInterval() ? "<" : "(") + beginningInterval() + ";" + endInterval() + (rightClosedInterval() ? ">" : ")");
    }
}
