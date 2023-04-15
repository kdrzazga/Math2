package org.kd;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LineAgTest {

    @Test
    public void testComputePerpendicularLine() {
        LineAG line, perpLine;
        final float x = 100;
        final float y = 200;
        final PointAG intersection;
        final PointAG expectedIntersection;
        final float delta = 0.01f;

        line = TestLinesLineSectionsBuilder.getDiagonalLine();
        expectedIntersection = new PointAG(150f, 150f);

        perpLine = line.computePerpendicularLine(new PointAG(x, y));
        intersection = perpLine.findIntersection(line);

        assertEquals(perpLine.A, -1 / line.A, delta);
        assertEquals(perpLine.computeY(x), y, delta);
        assertEquals(intersection.x, expectedIntersection.x, delta);
        assertEquals(intersection.y, expectedIntersection.y, delta);
    }

    @Test
    public void testComputeX() {
        LineAG diagonalLine;
        float x, y;
        final float delta = 0.00001f;

        diagonalLine = TestLinesLineSectionsBuilder.getDiagonalLine();
        for (y = -150; y < 20 * Math.pow(10, 12); y += Math.pow(10, 7) * Math.random()) {
            x = diagonalLine.computeX(y);
            assertEquals(x, y, delta);
        }
    }

    @Test
    public void testFindIntersection() {
        PointAG commonPoint, intersectionPoint;
        float A, B;
        List<LineAG> lines;
        LineAG line, lineToCompare;
        int lineIndex;

        commonPoint = new PointAG(10.2012f, 20.1891f);
        lines = new ArrayList<>();
        lines.add(new LineAG(new PointAG(0, 0), commonPoint));
        
        for (float angle = -3.141f; angle < 3.141f; angle += 0.05) {
            A = Numbers.roundToFloat(Math.tan(angle));
            B = commonPoint.y - A * commonPoint.x;
            line = new LineAG(A, B);

            Random rand = new Random();
            lineIndex = rand.nextInt(lines.size());
            lineToCompare = lines.get(lineIndex);

            intersectionPoint = line.findIntersection(lineToCompare);
            lines.add(line);
            
            assertTrue(Math.abs(commonPoint.x - intersectionPoint.x) < 0.001f);
            assertTrue(Math.abs(commonPoint.y - intersectionPoint.y) < 0.001f);
        }
    }
}
