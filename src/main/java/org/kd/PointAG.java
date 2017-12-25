package org.kd;

import java.awt.*;

public class PointAG {

    public float x;
    public float y;

    public PointAG(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public PointAG(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public PointAG(PointAG p) {
        this.x = p.x;
        this.y = p.y;
    }

    public Point convertToPoint() {
        return new Point(Math.round(this.x), Math.round(this.y));
    }

    public double distanceToAntherPointAG(PointAG anotherPoint) {
        return Math.sqrt((this.x - anotherPoint.x) * (this.x - anotherPoint.x)
                + (this.y - anotherPoint.y) * (this.y - anotherPoint.y));
    }

    public PointAG createPointByMovingByVector(LineSection vector) {
        PointAG movedPoint = new PointAG(this);
        movedPoint.moveByVector(vector);
        return movedPoint;
    }

    public PointAG createPointByMovingByVector(float length, float angle) {
        PointAG movedPoint = new PointAG(this);
        movedPoint.moveByVector(length, angle);
        return movedPoint;
    }

    public void moveByVector(double length, double angle) {
        this.x += Numbers.roundToFloat(length * Math.sin(angle));
        this.y += Numbers.roundToFloat(length * Math.cos(angle));
    }

    public void moveByVector(LineSection vector) {
        this.moveByVector(LineSection.computeLength.apply(vector), vector.computeInclinationAngle());
    }

    public boolean equals(PointAG point) {
        return (this.x == point.x && this.y == this.y);
    }

    @Override
    public String toString() {
        return "(" + this.x + " ," + this.y + ")";
    }
}
