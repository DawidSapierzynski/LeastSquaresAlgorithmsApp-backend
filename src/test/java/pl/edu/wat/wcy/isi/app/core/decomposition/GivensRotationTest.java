package pl.edu.wat.wcy.isi.app.core.decomposition;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class GivensRotationTest {

    public static final double DELTA = 0.0001;

    @Test
    void givens() {
        GivensRotation givensRotation = GivensRotation.givens(0.9134, 0.6324);

        assertEquals(0.8222, givensRotation.getC(), DELTA);
        assertEquals(0.5692, givensRotation.getS(), DELTA);

        log.info("c={}", givensRotation.getC());
        log.info("s={}", givensRotation.getS());
        log.info("r={}", givensRotation.getR());
    }

    @Test
    void g() {
        GivensRotation givensRotation = GivensRotation.givens(0.9134, 0.6324);
        double[][] m = new double[][]{
                {1.0, 0.0, 0.0, 0.0, 0.0},
                {0.0, 1.0, 0.0, 0.0, 0.0},
                {0.0, 0.0, 1.0, 0.0, 0.0},
                {0.0, 0.0, 0.0, 0.8222, -0.5692},
                {0.0, 0.0, 0.0, 0.5692, 0.8222}
        };
        double[][] g = GivensRotation.G(3, 4, givensRotation, 5).getArray();

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                assertEquals(m[i][j], g[i][j], DELTA);
            }
        }

        log.info(Arrays.deepToString(g));
    }
}