package org.kd;

public class LineAG {

    //equation y = Ax + B
    public float A;
    public float B;
    //vertical line like x=3 has no coefficients A and B
    public float verticalX;
    public final boolean vertical;

    public LineAG(float A, float B) {
        this.A = A;
        this.B = B;
        this.vertical = false;
    }

    public LineAG(float verticalX) {
        this.verticalX = verticalX;
        this.vertical = true;
    }

    public LineAG(PointAG p1, PointAG p2) {
        if (p1.x == p2.x) {
            this.vertical = true;
            this.verticalX = p1.x;

        } else {
            this.vertical = false;
            this.A = (p2.y - p1.y) / (p2.x - p1.x);
            this.B = -this.A * p1.x + p1.y;
        }
    }

    public LineAG(PointAG p1, float angle) {
        if (Numbers.roundToFloat(angle) == Numbers.roundToFloat(Math.PI / 2)
                 || (Numbers.roundToFloat(angle) == Numbers.roundToFloat(-Math.PI / 2))) {
            this.vertical = true;
            this.verticalX = p1.x;
        } else {
            this.vertical = false;
            this.A = Numbers.roundToFloat(Math.tan(angle));
            this.B = p1.y - this.A * p1.x;
        }
    }

    public float computeY(float x) {
        if (this.vertical) {
            throw new RuntimeException("Method computeY not available for vertical lines.");
        } else {
            return this.A * x + this.B;
        }
    }

    public float computeX(float y) {
        if (this.vertical) {
            return this.verticalX;
        } else if (this.A == 0) {
            throw new RuntimeException("Can't compute x for flat line.");
        } else {
            return (y - this.B) / A;
        }
    }

    public PointAG findIntersection(LineAG line) {
        float x;
        float y;
        if (!this.vertical && !line.vertical) {
            if (this.A == line.A) {
                return null;
            }

            x = (line.B - this.B) / (this.A - line.A);
            y = this.A * x + this.B;

        } else if (this.vertical) {
            x = this.verticalX;
            y = line.A * this.verticalX + line.B;
        } else { //line.vertical = true
            x = line.verticalX;
            y = this.A * x + this.B;
        }

        return new PointAG(x, y);
    }

    public LineAG computePerpendicularLine(PointAG pointOfNewLine) {
        if ((this.A == 0) && (!this.vertical)) {
            return new LineAG(pointOfNewLine.x);
        } else if (this.vertical) {
            return new LineAG(0, pointOfNewLine.y);
        } else {
            float perpendicularA = -1 / A;
            float perpendicularB = pointOfNewLine.y - perpendicularA * pointOfNewLine.x;
            return new LineAG(perpendicularA, perpendicularB);
        }
    }

    public PointAG givenXMovePointAlongLine(float XofPointToMove, float distance) {
        if (!this.vertical) {
            float YofPointToMove = this.computeY(XofPointToMove);

            return movePoint(XofPointToMove, YofPointToMove, distance);
        } else {
            throw new RuntimeException("Cannot move on vertical line. Try using givenYMovePointAlongLine instead.");
        }
    }

    public PointAG givenYMovePointAlongLine(float YofPointToMove, float distance) {
        if (this.vertical) {
            return new PointAG(this.verticalX, YofPointToMove + distance);
        } else {
            float XofPointToMove = this.computeX(YofPointToMove);
            return movePoint(XofPointToMove, YofPointToMove, distance);
        }
    }
    
    private PointAG movePoint(float XofPointToMove, float YofPointToMove, float distance) {
        PointAG pointToMove = new PointAG(XofPointToMove, YofPointToMove);
        double angle = Math.atan(this.A);
        pointToMove.moveByVector(distance, angle);
        return pointToMove;
    }    
}
