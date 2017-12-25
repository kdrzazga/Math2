package org.kd.math2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonAG implements Cloneable {
    //AG stands for Analytical Geometry

    public List<PointAG> points;

    public PolygonAG() {
        this.points = new ArrayList<>();
    }

    public PolygonAG(PointAG[] points) {
        this.points = new ArrayList<>();
        this.points.addAll(Arrays.asList(points));
    }

    public PolygonAG(PolygonAG polygon) {
        this.points = new ArrayList<>();
        this.points.addAll(polygon.points);
    }

    public void addPoint(int x, int y) {
        this.points.add(new PointAG(x, y));
    }

    public void addPointAG(float x, float y) {
        this.points.add(new PointAG(x, y));
    }

    public LineSection getLineSectionCrossedBy(LineSection lineSection) {
        if (lineSection.vertical) {
            return getLineSectionCrossingVerticalSection(lineSection);
        } else {
            for (int i = 0; i < this.points.size() - 2; i++) {
                LineSection lineSectionToCheck = new LineSection(this.points.get(i), this.points.get(i + 1));
                PointAG intersection = lineSectionToCheck.computeIntersection.apply(lineSection);
                if (intersection != null) {
                    return lineSectionToCheck;
                }
            }
            return null;
        }
    }

    public LineSection getLineSectionCrossingVerticalSection(LineSection section) {
        if (!section.vertical) {
            throw new RuntimeException("Wrong argument - line not vertical. " + PolygonAG.class.getName());
        }

        LineSection result;
        int i;

        for (i = 0; i < this.points.size() - 1; i++) {
            PointAG p1 = this.points.get(i);
            PointAG p2 = this.points.get(i + 1);
            result = new LineSection(p1, p2);

            float minX = Math.min(p1.x, p2.x);
            float maxX = Math.max(p1.x, p2.x);

            if (minX < section.verticalX && maxX > section.verticalX) {

                PointAG intersection = section.computeIntersection.apply(result);
                if (intersection != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public Polygon convertToPolygon() {
        Polygon result = new Polygon();

        for (PointAG pointAG : this.points) {
            Point p = pointAG.convertToPoint();
            result.addPoint(p.x, p.y);
        }

        return result;
    }

    public PointAG computeCenter() {
        int sumXCoordinates = 0;
        int sumYCoordinates = 0;

        for (PointAG point : this.points) {
            sumXCoordinates += point.x;
            sumYCoordinates += point.y;
        }

        float centerX = Numbers.roundToFloat(sumXCoordinates / (float) this.points.size());
        float centerY = Numbers.roundToFloat(sumYCoordinates / (float) this.points.size());

        return new PointAG(centerX, centerY);
    }

    public void scale(float scaleFactor) {
        scaleConvexPolygon(scaleFactor);
    }

    public PointAG computeCentroid() {
        double A = this.computeSignedArea();
        if (A == 0)
            return this.points.get(0);
        else {
            double CxSum = 0;
            double CySum = 0;
            for (int i = 0; i < this.points.size() - 1; i++) {
                PointAG p = this.points.get(i);
                PointAG pPlus1 = this.points.get(i + 1);

                double factor = (p.x * pPlus1.y - pPlus1.x * p.y);
                CxSum += (p.x + pPlus1.x) * factor;
                CySum += (p.y + pPlus1.y) * factor;
            }
            double Cx = CxSum / (6 * A);
            double Cy = CySum / (6 * A);

            return new PointAG(Numbers.roundToFloat(Cx), Numbers.roundToFloat(Cy));
        }
    }

    private double computeSignedArea() {
        double factor = 0;
        for (int i = 0; i < this.points.size() - 1; i++) {
            PointAG p = this.points.get(i);
            PointAG pPlus1 = this.points.get(i + 1);

            factor += (p.x * pPlus1.y - pPlus1.x * p.y);
        }
        return factor / 2;
    }

    private void scaleConvexPolygon(float scaleFactor) {
        PointAG center = this.computeCentroid();

        List<PointAG> scaledPoints = new ArrayList<>(this.points.size());

        this.points.stream().map(point -> new LineSection(center, point)).map(ray -> {
            ray.moveP2MultiplyingBy(scaleFactor);
            return ray;
        }).forEachOrdered(ray -> scaledPoints.add(ray.p2));

        this.points = scaledPoints;
    }

    public LineSection findSideClosestToPoint(PointAG point) {
        double minDistance = Double.MAX_VALUE;
        int vertexIndex = 0;
        LineSection side;

        for (int i = 0; i < this.points.size() - 2; i++) {
            side = new LineSection(points.get(i), points.get(i + 1));
            PointAG sideCenter = side.computeCenter();

            double distance = sideCenter.distanceToAntherPointAG(point);
            if (distance < minDistance) {
                vertexIndex = i;
                minDistance = distance;
            }
        }

        return new LineSection(points.get(vertexIndex), points.get(vertexIndex + 1));
    }

    public boolean isConvex() {
        checkIfPolygonBigEnough();

        final int pointsCount = this.points.size();
        final Polygon truncatedPolygon = this.convertToPolygon();

        for (int i = 0; i < pointsCount; i++) {
            PointAG pointI = this.points.get(i);
            for (int j = i + 1; j < pointsCount; j++) {
                PointAG pointJ = this.points.get(j);
                LineSection side = new LineSection(pointI, pointJ);
                PointAG sectionCenter = side.computeCenter();

                if (truncatedPolygon.contains(sectionCenter.x, sectionCenter.y)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkIfPolygonBigEnough() {
        for (int i = 0; i < this.points.size() - 1; i++) {
            LineSection section = new LineSection(this.points.get(i), this.points.get(i + 1));
            if (LineSection.computeLength.apply(section) < 1) {
                throw new RuntimeException("Polygon too small to check if it's convex"
                        + " with this algorithm, pls google a different algorithm.");
            }
        }
    }

    private float[] getXPoints() {
        float[] xpoints = new float[this.points.size()];

        for (int i = 0; i < this.points.size(); i++) {
            xpoints[i] = this.points.get(i).x;
        }
        return xpoints;
    }

    private float[] getYPoints() {
        float[] ypoints = new float[this.points.size()];

        for (int i = 0; i < this.points.size(); i++) {
            ypoints[i] = this.points.get(i).y;
        }
        return ypoints;
    }

    public float computeMinX() {
        return Numbers.getMin(this.getXPoints());
    }

    public float computeMaxX() {
        return Numbers.getMax(this.getXPoints());
    }

    public float computeMinY() {
        return Numbers.getMin(this.getYPoints());
    }

    public float computeMaxY() {
        return Numbers.getMax(this.getYPoints());
    }
}
