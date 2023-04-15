package org.kd;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.kd.LineSection.computeLength;

public class LineSectionTest {

    private final static LineSection[] LINE_SECTION = new LineSection[4];

    @Test
    public void testMoveP1MultiplyingBy() {
        final float delta = 0.00001f;

        LINE_SECTION[0] = new LineSection(0, 0, 10, 10); //length 10*sqrt(2)
        LINE_SECTION[1] = new LineSection(10, 10, 0, 0);
        LINE_SECTION[2] = new LineSection(0, 5, 0, 15);
        LINE_SECTION[3] = new LineSection(10, 20.333f, 10, 41.333f);

        LINE_SECTION[0].moveP2MultiplyingBy(2);
        LINE_SECTION[1].moveP2MultiplyingBy(2);
        LINE_SECTION[2].moveP2MultiplyingBy(7.5f);
        LINE_SECTION[3].moveP2MultiplyingBy(0.333f);

        assertEquals(LINE_SECTION[0].p2.x, 20f, delta);
        assertEquals(LINE_SECTION[0].p2.y, 20f, delta);
        assertEquals(LINE_SECTION[1].p2.x, -10f, delta);
        assertEquals(LINE_SECTION[1].p2.y, -10f, delta);
        assertEquals(LINE_SECTION[2].p2.x, 0f, delta);
        assertEquals(LINE_SECTION[2].p2.y, 80f, delta);
        assertEquals(LINE_SECTION[3].p2.x, 10f, delta);
        assertEquals(LINE_SECTION[3].p2.y, 27.326f, delta);
    }

    @Test
    public void testComputeIntersection() {
        LineSection lineSection2;
        LineSection lineSection3;
        LineSection parallelLineSection1;
        LineSection parallelLineSection2;
        final float delta = 0.00001f;

        lineSection1 = new LineSection(0, 0, 10, 10);
        lineSection2 = new LineSection(0, 10, 10, 0);
        lineSection3 = new LineSection(2, 0, 10, 7);
        verticalLineSection = new LineSection(5, 0, 10, 5);
        parallelLineSection1 = new LineSection(0, 2, 1, 0);
        parallelLineSection2 = new LineSection(0, 3, 6, 0);
        PointAG intersection1_2;
        PointAG nullIntersection1_3;
        PointAG nullIntersection1;
        PointAG intersection2_3;

        intersection1_2 = lineSection1.computeIntersection.apply(lineSection2);
        nullIntersection1_3 = lineSection1.computeIntersection.apply(lineSection3);
        intersection2_3 = lineSection3.computeIntersection.apply(lineSection2);
        nullIntersection1 = parallelLineSection1.computeIntersection.apply(parallelLineSection2);

        assertEquals(intersection1_2.x, 5f, delta);
        assertEquals(intersection1_2.y, 5f, delta);
        assertNull(nullIntersection1_3);//intersection at point (-14, -14)- outside both sections
        assertEquals(intersection2_3.x, 6.266667f, delta);
        assertEquals(intersection2_3.y, 3.7333336f, delta);
        assertNull(nullIntersection1);//parallel lines - no intersection
    }

    private static LineSection verticalLineSection;
    private static PointAG p1, p2, p3;

    @Test
    public void testXBelongsToLineSection() {
        p1 = new PointAG(10.3f, 100);
        p2 = new PointAG(10.3f, 10.3f);
        p3 = new PointAG(-10.3f, 10.3f);

        verticalLineSection = new LineSection(10.3f, 33.33f, 10.3f, Numbers.roundToFloat(Math.PI));

        assertTrue(verticalLineSection.xBelongsToLineSection.apply(p1.x));
        assertTrue(verticalLineSection.xBelongsToLineSection.apply(p2.x));
        assertFalse(verticalLineSection.xBelongsToLineSection.apply(p3.x));
    }

    @Test
    public void testYBelongsToLineSection() {
        p1 = new PointAG(10.3f, 100);
        p2 = new PointAG(10.3f, 10.3f);
        p3 = new PointAG(-10.3f, 20.3f);

        verticalLineSection = new LineSection(10.3f, 33.33f, 10.3f
                , Numbers.roundToFloat(Math.PI));

        assertFalse(verticalLineSection.yBelongsToLineSection.apply(p1.y));
        assertTrue(verticalLineSection.yBelongsToLineSection.apply(p2.y));
        assertTrue(verticalLineSection.yBelongsToLineSection.apply(p3.y));
    }

    private static LineSection lineSection1;

    @Test
    public void testCreateParallelSection() {
        float ls1Length;

        lineSection1 = new LineSection(new PointAG(20, 20), new PointAG(40, 40));
        ls1Length = Numbers.roundToFloat(computeLength.apply(lineSection1));
        LineSection sameLineSection;

        sameLineSection = lineSection1.createParalelSection.apply(0f);
        LineSection lineSectionAbove = lineSection1.createParalelSection.apply(-ls1Length / 4);
        LineSection lineSectionBelow = lineSection1.createParalelSection.apply(ls1Length / 4);

        Assert.assertTrue(lineSection1.equals(sameLineSection));

        float minXlsAbove = Math.min(lineSectionAbove.p1.x, lineSectionAbove.p2.x);
        float minXls1 = Math.min(lineSection1.p1.x, lineSection1.p2.x);
        float minXlsBelow = Math.min(lineSectionBelow.p1.x, lineSectionBelow.p2.x);
        float maxXlsAbove = Math.max(lineSectionAbove.p1.x, lineSectionAbove.p2.x);
        float maxXls1 = Math.max(lineSection1.p1.x, lineSection1.p2.x);
        float maxXlsBelow = Math.max(lineSectionBelow.p1.x, lineSectionBelow.p2.x);

        float[] leftXs = new float[]{minXlsAbove, minXls1, minXlsBelow};
        float[] rightXs = new float[]{maxXlsAbove, maxXls1, maxXlsBelow};

        float minCommonX = Numbers.getMax(leftXs);
        float maxCommonX = Numbers.getMin(rightXs);

        for (float x = minCommonX; x < maxCommonX; x += Math.abs(maxCommonX - minCommonX) / 10) {
            assertTrue(lineSectionAbove.computeY(x) < lineSection1.computeY(x));
            assertTrue(lineSectionBelow.computeY(x) > lineSection1.computeY(x));
        }
    }
}
