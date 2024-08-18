package pl.leastsquaresalgorithms.approximationservice.core;

import java.util.Random;

public class Distribution {
    private static final Random random = new Random();

    public static long normalRound(double mean, double standardDeviation) {
        return Math.round(normal(mean, standardDeviation));
    }

    public static double normal(double mean, double standardDeviation) {
        return random.nextGaussian() * standardDeviation + mean;
    }
}
