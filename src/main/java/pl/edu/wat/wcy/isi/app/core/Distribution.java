package pl.edu.wat.wcy.isi.app.core;

import java.util.Random;

public class Distribution {
    private static final Random random = new Random();

    public static long normal(double mean, double standardDeviation) {
        return Math.round(random.nextGaussian() * standardDeviation + mean);
    }
}
