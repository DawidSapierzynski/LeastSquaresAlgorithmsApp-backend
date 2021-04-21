package pl.edu.wat.wcy.isi.app.core.decomposition;

import Jama.Matrix;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.lang.Math.*;

@Getter
@AllArgsConstructor
public class GivensRotation {
    private final double c;
    private final double s;
    private final double r;

    public static GivensRotation givens(double a, double b) {
        double c, s, r, t, u;
        if (b == 0.0) {
            c = signum(a);
            s = 0;
            r = abs(a);
        } else if (a == 0.0) {
            c = 0;
            s = signum(b);
            r = abs(b);
        } else if (abs(a) > abs(b)) {
            t = b / a;
            u = signum(a) * sqrt(1 + t * t);
            c = 1 / u;
            s = c * t;
            r = a * u;
        } else {
            t = a / b;
            u = signum(b) * sqrt(1 + t * t);
            s = 1 / u;
            c = s * t;
            r = b * u;
        }
        return new GivensRotation(c, s, r);
    }

    public Matrix getGivensRotationMatrix(int i, int j, int size) {
        Matrix g = Matrix.identity(size, size);
        g.set(i, i, this.getC());
        g.set(i, j, -this.getS());
        g.set(j, i, this.getS());
        g.set(j, j, this.getC());
        return g;
    }
}
