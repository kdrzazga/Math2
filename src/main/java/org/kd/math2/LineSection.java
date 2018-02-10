package org.kd.math2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class LineSection extends LineAG {

    public PointAG p1;
    public PointAG p2;

    public LineSection(float x1, float y1, float x2, float y2) {
        super(new PointAG(x1, y1), new PointAG(x2, y2));
        this.p1 = new PointAG(x1, y1);
        this.p2 = new PointAG(x2, y2);
    }

    public LineSection(LineSection ls){
        super(ls.p1, ls.p2);
        this.p1 = new PointAG(ls.p1);
        this.p2 = new PointAG(ls.p2);
    }

    public LineSection(Point p1, Point p2) {
        super(new PointAG(p1), new PointAG(p2));
        this.p1 = new PointAG(p1);
        this.p2 = new PointAG(p2);
    }

    public LineSection(PointAG p1, PointAG p2) {
        super(p1, p2);
        this.p1 = p1;
        this.p2 = p2;
    }
    
    public LineSection(PointAG point, float angle, float length)
    {
        super(point, angle);      
        this.p1 = point;
        this.p2 = new PointAG(point);
        p2.moveByVector(length, angle);
    }

    public final PointAG computeCenter() {
        float centerX = (p1.x + p2.x) / 2;
        float centerY = (p1.y + p2.y) / 2;
        return new PointAG(centerX, centerY);
    }
        /* positive angles:
                pi rad
                |
     3pi/2 rad--o---    pi/2 rad
                |\ pi/4 rad
                2pi rad = 0 rad     
     */
    
    public final double computePositiveInclinationAngle()
    {
        return computeInclinationAngle() +Math.PI;
    }

    /* angles:
                0 deg
                |
        90 deg--o---    -90 deg
                |\ -135 deg
                180 deg       
     */

    public final double computeInclinationAngle() {
        double ySpan = this.p1.y - this.p2.y;
        double xSpan = this.p1.x - this.p2.x;
        return Math.atan2(xSpan, ySpan);
    }

    public final Function<Float, Boolean> xBelongsToLineSection = x
            -> (x >= Math.min(this.p1.x, this.p2.x) && (x <= Math.max(this.p1.x, this.p2.x)));

    public final Function<Float, Boolean> yBelongsToLineSection = y
            -> (y >= Math.min(this.p1.y, this.p2.y) && (y <= Math.max(this.p1.y, this.p2.y)));

    @Override
    public final float computeY(float x) {
        if (xBelongsToLineSection.apply(x))
        {
            return super.computeY(x);
        }
        else
            throw new RuntimeException(x + " doesn't belong to line section");
    }

    public final Function<LineSection, PointAG> computeIntersection = lineSection2
            -> returnIntersectionIfInLineSection(super.findIntersection(lineSection2));

    private PointAG returnIntersectionIfInLineSection(PointAG intersection) {
        if (intersection != null)
        {
            if (this.xBelongsToLineSection.apply(intersection.x)
                    && this.yBelongsToLineSection.apply(intersection.y))
                return intersection;
            else return null;
        }
        else
            return null;
    }
    
    public final void moveP2MultiplyingBy(double scalar)
    {
        double length = computeLength.apply(this);
        double inclination = this.computeInclinationAngle() + Math.PI;
        
        this.p2 = new PointAG(this.p1);
        this.p2.moveByVector(length * scalar, inclination);
    }

    public final Function<Float, LineSection> createParalelSection = distance -> {
        LineAG perpLineP1 = this.computePerpendicularLine(this.p1);
        LineAG perpLineP2 = this.computePerpendicularLine(this.p2);

        PointAG parSectionP1 = perpLineP1.givenXMovePointAlongLine(this.p1.x, distance);
        PointAG parSectionP2 = perpLineP2.givenXMovePointAlongLine(this.p2.x, distance);

        return new LineSection(parSectionP1, parSectionP2);
    };

    public final static Function<LineSection, Double> computeLength = section
            -> Math.sqrt(Math.pow((section.p1.y - section.p2.y), 2)
            + Math.pow((section.p1.x - section.p2.x), 2));

    public final boolean equals(LineSection lineSectionToCmp){
        return (this.p1.equals(lineSectionToCmp.p1)
                && (this.p2.equals(lineSectionToCmp.p2)));
    }

    public final boolean equalsNoMatterPointOrder(LineSection lineSectionToCmp){
        return
                equals(lineSectionToCmp)
                || ((this.p1.equals(lineSectionToCmp.p2)
                && (this.p2.equals(lineSectionToCmp.p1))));
    }

    public final List<LineSection> divideToLineSections(int sectionsCount){
        if (sectionsCount <= 1)
            return Collections.singletonList(new LineSection(this));
        else {
            List<PointAG> pointsOfDivision = new ArrayList<>(sectionsCount);

            double dividedSectionLen = computeLength.apply(this) / sectionsCount;
            double inclination = this.computeInclinationAngle();

            for (int i = 0; i < sectionsCount; i++)
            {
                PointAG point = new PointAG(this.p2);
                point.moveByVector(dividedSectionLen * i, inclination);
                pointsOfDivision.add(point);
            }

            pointsOfDivision.add(this.p1);
            return createBrokenLine(pointsOfDivision);
        }
    }

    public final List<LineSection> divideToLineSections(double length){

        int sectionsCount = (int) (length / computeLength.apply(this));
        if (sectionsCount <= 1)
            return Collections.singletonList(new LineSection(this));
        else
        return divideToLineSections(sectionsCount);
    }

    public final static List<LineSection> createBrokenLine(List<PointAG> points){
        List<LineSection> brokenLine = new ArrayList<>(points.size() -1 );
        for (int i = 0; i < points.size() - 1; i++){
            LineSection line = new LineSection(points.get(i), points.get(i + 1));
            brokenLine.add(line);
        }
        return brokenLine;
    }

    @Override
    public final String toString() {
        return "(" + this.p1.x + ", " + this.p1.y
                + "), (" + this.p2.x + ", " + this.p2.y + ")";
    }
}
