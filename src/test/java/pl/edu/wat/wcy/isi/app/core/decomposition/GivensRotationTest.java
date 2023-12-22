package pl.edu.wat.wcy.isi.app.core.decomposition;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class GivensRotationTest {

    public static final double DELTA = 1e-4;

    @Test
    void givens() {
        GivensRotation givensRotation = GivensRotation.givens(12, -4);

        assertEquals(0.94868, givensRotation.c(), DELTA);
        assertEquals(-0.31622, givensRotation.s(), DELTA);

        log.info("c={}", givensRotation.c());
        log.info("s={}", givensRotation.s());
        log.info("r={}", givensRotation.r());
    }

    @Test
    void g() {
        GivensRotation givensRotation = GivensRotation.givens(12, -4);
        double[][] m = new double[][]{
                {0.94868, 0.0, -0.31622},
                {0.0, 1.0, 0.0},
                {0.31622, 0.0, 0.94868}
        };
        double[][] g = givensRotation.getGivensRotationMatrix(2, 0, 3).getArray();

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                assertEquals(m[i][j], g[i][j], DELTA);
            }
        }

        log.info(Arrays.deepToString(g));
    }
}