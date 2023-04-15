package org.kd;

import org.junit.Test;

import static org.junit.Assert.*;

public class PolygonAgTest {

    @Test
    public void testGetLineSectionCrossedBy() {
        PolygonAG triangle;
        LineSection crossingLineSection = new LineSection(1200, 1200, 1300, 1000);
        final PointAG[] TRIANGLE_PTS = {new PointAG(1100, 1100), new PointAG(2100, 1100), new PointAG(1100, 2100)};

        triangle = new PolygonAG(TRIANGLE_PTS);
        crossingLineSection = triangle.getLineSectionCrossedBy(crossingLineSection);
        assertEquals(crossingLineSection.p1.x, 1100.0, 0.1);
        assertEquals(crossingLineSection.p1.y, 1100.0, 0.1);
        assertEquals(crossingLineSection.p2.x, 2100, 0.1);
        assertEquals(crossingLineSection.p2.y, 1100, 0.1);
    }


    @Test
    public void testGetLineSectionCrossingVerticalSection() {
        final PointAG[] RECTANGLE_VERTICES = {new PointAG(10, 10), new PointAG(500, 15), new PointAG(490, 900), new PointAG(10, 890)};
        LineSection verticalLineSection, crossingSection;

        PolygonAG rectangle;
        rectangle = new PolygonAG(RECTANGLE_VERTICES);
        verticalLineSection = computeVerticalSectionDownFromCenter(rectangle);

        crossingSection = rectangle.getLineSectionCrossingVerticalSection(verticalLineSection);

        assertEquals(10, crossingSection.p2.x, 0.0);
        assertEquals(890, crossingSection.p2.y, 0.0);
        assertEquals(490, crossingSection.p1.x, 0.0);
        assertEquals(900, crossingSection.p1.y, 0.0);
    }

    @Test
    public void testIsConvex() {
        final PointAG[] CONCAVE_POLYGON1_VERTICES = {new PointAG(0, 900), new PointAG(123, 500), new PointAG(2500, 900),
                new PointAG(124, 900), new PointAG(123, 899), new PointAG(122, 900)};
        final PointAG[] CONCAVE_POLYGON2_VERTICES = {new PointAG(10.53f, 10), new PointAG(12.33f, 30), new PointAG(10, 50),
                new PointAG(40, 50), new PointAG(40, 10)};
        final PointAG[] CONVEX_POLYGON1_VERTICES = {new PointAG(1000.3f, 1000.12f), new PointAG(3000.3f, 2000.12f),
                new PointAG(5000.3f, 3000.12f), new PointAG(1000.3f, 3000.12f)};
        PolygonAG concavePolygon1UnderTest, convexPolygon1UnderTest, concavePolygon2UnderTest;

        concavePolygon1UnderTest = new PolygonAG(CONCAVE_POLYGON1_VERTICES);
        concavePolygon2UnderTest = new PolygonAG(CONCAVE_POLYGON2_VERTICES);
        convexPolygon1UnderTest = new PolygonAG(CONVEX_POLYGON1_VERTICES);

        assertFalse(concavePolygon1UnderTest.isConvex());
        assertFalse(concavePolygon2UnderTest.isConvex());
    }

    private static LineSection computeVerticalSectionDownFromCenter(PolygonAG rectangle) {
        PointAG verticalSectionP1 = rectangle.computeCenter();
        PointAG verticalSectionP2 = new PointAG(verticalSectionP1.x, 50000);
        return new LineSection(verticalSectionP1, verticalSectionP2);
    }
}
