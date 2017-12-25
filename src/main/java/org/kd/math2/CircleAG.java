package org.kd.math2;

    public class CircleAG {

        private final PolygonAG points;
        private final PointAG center;
        private final float radius;

        public CircleAG(PointAG center, float radius, int numberOfPoints) {
            this.center = center;
            this.points = new PolygonAG();
            this.radius = radius;
            
            for (double angle = Math.PI; angle > -Math.PI; angle -= 2 * Math.PI / numberOfPoints) {                
                float x  = center.x + radius * Numbers.roundToFloat(Math.cos(angle));                        
                float y =  center.y + radius * Numbers.roundToFloat(Math.sin(angle));
                
                points.addPointAG(x, y);
            }
        }


        public PolygonAG getPoints() {
            return points;
        }

        public PointAG getCenter() {
            return center;
        }

        public double getRadius() {
            return radius;
        }
    }