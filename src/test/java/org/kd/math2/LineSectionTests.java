package org.kd.math2;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class LineSectionTests {

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
        ls1Length = Numbers.roundToFloat(LineSection.computeLength.apply(lineSection1));
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

    @Test
    public void testSectionDividing(){
        List<LineSection> expectedLineSections0033 = Arrays.asList(
                new LineSection(2,2, 3,3),
                new LineSection(1,1, 2,2),
                new LineSection(0,0, 1,1)
        );

        List<LineSection> expectedLineSections121212212 = Arrays.asList(
                new LineSection(12,212, 12,211),
                new LineSection(12,211, 12,210),
                new LineSection(12,210, 12,209)
        );

        List<LineSection> expectedLineSections1154 = Arrays.asList(
                new LineSection(5,4, 3,2.5f),
                new LineSection(3,2.5f, 1,1)
        );

        List<LineSection> expectedLineSections5411 = Arrays.asList(
                new LineSection(3,2.5f, 1,1),
                new LineSection(5,4, 3,2.5f)
        );

        List<LineSection> expectedLineSections14301010 = Arrays.asList(
                new LineSection(10,10, 11,15),
                new LineSection(11,15, 12,20)
        );

        testSectionDividing(new LineSection(0,0, 3,3), expectedLineSections0033, 3);
        testSectionDividing(new LineSection(12,12,12, 212), expectedLineSections121212212, 200);
        testSectionDividing(new LineSection(1,1,5, 4), expectedLineSections1154, 2);
        testSectionDividing(new LineSection(5,4,1, 1), expectedLineSections5411, 2);
        testSectionDividing(new LineSection(14,30,10, 10), expectedLineSections14301010, 4);
    }

    private void testSectionDividing(LineSection ls, List<LineSection> expectedLineSections0033, int divisionCount) {
        List<LineSection> dividedLineSections= ls.divideToLineSections(divisionCount);
        assertEquals(divisionCount, dividedLineSections.size());
        for (int i = 0; i < expectedLineSections0033.size(); i++)
            assertTrue("act " + dividedLineSections.get(i) + " exp " + expectedLineSections0033.get(i)
                    , dividedLineSections.get(i).equalsNoMatterPointOrder(expectedLineSections0033.get(i)));
    }

    @Test
    public void testBrokenLineCreation(){
        PointAG[] inputPoints = {
                new PointAG(1, 1), new PointAG(2, 2),new PointAG(3, 1)
        };

        LineSection[] expectedSections = { new LineSection(1,1, 2, 2),
                new LineSection(2,2, 3, 1)};

        List<LineSection> brokenLine = LineSection.createBrokenLine(Arrays.asList(inputPoints));

        int i = 0;
        for (LineSection ls : brokenLine){
            assertTrue(ls.equalsNoMatterPointOrder(expectedSections[i]));
            i++;
        }
    }
/*
    @Test
    public void testSectionDivision(){
        PointAG[] pointsArray = {new PointAG(1f, 1f), new PointAG(2f, 2f), new PointAG(3f, 1f)
                , new PointAG(4f, 2f), new PointAG(5f, 1f)};
        ArrayList<PointAG> inputPoints = Arrays.asList(pointsArray);

        List<PointAG> outputPoints1 = LineSection.divideSectionSetToEvenSections(inputPoints, 5);
        List<PointAG> outputPoints2 = LineSection.divideSectionSetToEvenSections(inputPoints, 3);



    }*/
}
