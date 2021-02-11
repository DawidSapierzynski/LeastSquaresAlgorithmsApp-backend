package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.Math.hypot;

@Getter
@AllArgsConstructor
public class GivensRotation {
    private final double c;
    private final double s;
    private final double r;

    public static GivensRotation givens(double a, double b) {
        double c, s, r;
        if (a == 0.0 && b == 0.0) {
            c = 0.0;
            s = 0.0;
            r = 0.0;
        } else {
            r = hypot(a, b);
            c = a / r;
            s = b / r;
        }
        return new GivensRotation(c, s, r);
    }

    public static Matrix G(int i, int j, GivensRotation givensRotation, int size) {
        Matrix g = Matrix.identity(size, size);
        g.set(i, i, givensRotation.getC());
        g.set(i, j, -givensRotation.getS());
        g.set(j, i, givensRotation.getS());
        g.set(j, j, givensRotation.getC());
        return g;
    }
}
