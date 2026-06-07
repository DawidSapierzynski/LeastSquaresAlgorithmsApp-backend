package pl.least_squares_algorithms.approximation_service.core.function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.least_squares_algorithms.core.PointXY;
import pl.least_squares_algorithms.core.function.LinearDomainMapping;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

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

        assertEquals(LinearDomainMapping.MIN, pointsResult.getFirst().getX(), DELTA);
        for (int i = 1; i < pointsResult.size() - 1; i++) {
            assertTrue("X is not in the range.", (LinearDomainMapping.MIN < pointsResult.get(i).getX()) && (LinearDomainMapping.MAX > pointsResult.get(i).getX()));
        }
        assertEquals(LinearDomainMapping.MAX, pointsResult.getLast().getX(), DELTA);
    }
}