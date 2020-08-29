package pl.edu.wat.wcy.isi.autoapproximationappbackend.core.function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.wat.wcy.isi.app.core.function.LinearDomainMapping;
import pl.edu.wat.wcy.isi.app.model.PointXY;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinearDomainMappingTest {
    private static final double DELTA = 1e-14;

    private List<PointXY> points;
    private LinearDomainMapping linearDomainMapping;

    @BeforeEach
    void setUp() {
        points = new ArrayList<>();
        points.add(new PointXY(8.0, 3.0));
        points.add(new PointXY(17.0, 21.0));
        points.add(new PointXY(24.0, 36.8));
        points.add(new PointXY(28.0, 10.0));
        linearDomainMapping = new LinearDomainMapping(points);
    }

    @AfterEach
    void tearDown() {
        points = null;
        linearDomainMapping = null;
    }

    @Test
    void convert() {
        List<PointXY> pointsResult = linearDomainMapping.convert();

        assertEquals(LinearDomainMapping.MIN, pointsResult.get(0).getX(), DELTA);
        for (int i = 1; i < pointsResult.size() - 1; i++) {
            assertTrue("X is not in the range.", (LinearDomainMapping.MIN < pointsResult.get(i).getX()) && (LinearDomainMapping.MAX > pointsResult.get(i).getX()));
        }
        assertEquals(LinearDomainMapping.MAX, pointsResult.get(pointsResult.size() - 1).getX(), DELTA);
    }
}