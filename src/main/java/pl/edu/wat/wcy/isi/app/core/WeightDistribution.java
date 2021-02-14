package pl.edu.wat.wcy.isi.app.core;

import java.util.function.BiFunction;

public enum WeightDistribution {
    NONE("none", (a, b) -> 1L),
    NORMAL("normal", (a, b) -> Math.abs(Distribution.normal(a, b)));

    private final String distributionName;
    private final BiFunction<Double, Double, Long> function;

    WeightDistribution(String distributionName, BiFunction<Double, Double, Long> function) {
        this.distributionName = distributionName;
        this.function = function;
    }

    public String getDistributionName() {
        return distributionName;
    }

    public double applyValue(double a, double b) {
        return this.function.apply(a, b);
    }
}
