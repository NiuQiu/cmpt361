package line;

import geometry.Vertex3D;
import line.helper.Supplier;
import windowing.drawable.Drawable;
import static java.lang.Math.abs;

/**
 * @author Administrator on
 * @date 10/3/2017.
 */
public class AntialiasingLineRenderer implements LineRenderer {
    private final static double RADIUS = Math.sqrt(2);
    private final static double RADIUSSQUARED = Math.pow(Math.sqrt(2), 2);
    private final static double CIRCLEAREA = Math.PI * RADIUSSQUARED;
    private final static int MAXDISTANCE = 1;
    private final static int ANTIALIAS_RANGE = 3;

    // If this value is 1 then there is no change
    // If < 1 then the coverage is increased (Aliasing starts to show around 0.5)
    // If > 1 then coverage is decreased (Image is nearly invisible at 1.6)
    // Does not increase coverage of 0
    private static final double COVERAGE_CHANGE = 1;

    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
        double slope = Supplier.calculateSlope(p1, p2);
        double y = p1.getY();
        int color = p1.getColor().asARGB();
        double coverage;

        for (int x = p1.getIntX(); x <= p2.getIntX(); x++){
            for (int pixelY = (int) (Math.round(y) - ANTIALIAS_RANGE); pixelY <= Math.round(y) + ANTIALIAS_RANGE; pixelY++){
                coverage = calculatePixelCoverage(p1, p2, x, pixelY);
                if (coverage != 0){ coverage = 1 - COVERAGE_CHANGE*(1-coverage);}
                panel.setPixelWithCoverage(x, pixelY, 0.0, color, coverage);
            }
            y += slope;
        }

    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new AntialiasingLineRenderer());
    }

    /**
     * This function use dot products to calculate the distance
     * for {v1[p1, (x,y)], v2[p1, p2]} and {v1, v3}
     * Detail:
     *
     * -- Step 1 --
     * v1*v2 = cos(theta) * |v1| * |v2| (dot product)
     * cos(theta) = v3/v1 (adjacent/hypotenuse)
     * -> v3/v1 = (v1*v2) / (|v1|*|v2|)
     * -> v3 = v2 / |v1|*|v2|
     * -> v3/v2 = v2 / (|v1|*|v2|)^2 (because deltaX^2 + deltaY^2 = v2^2)
     * -> ratio = v3/v2
     *
     * -- Step 2 --
     * orthogonalX = p1.getX + ratio*deltaX (scale down x value)
     * orthogonalY = p1.getY + ratio*deltaY (same reason above)
     * Then use pythagorean to calculate distance, distance must
     * within the range of p1 and p2
     *
     * @param x x is the current x value
     * @param y y is the current y value
     * @return the distance from pixel center point to (orthogonalX, orthogonalY) on vector(p1, p2)
     */
    private double getDistance(Vertex3D p1, Vertex3D p2, double x, double y){
        double orthogonalX, orthogonalY;

        // hypotenuse and opposite for [p1, (x,y)]
        double A = x - p1.getX();
        double B = y - p1.getY();
        // hypotenuse and opposite for [p1, p2]
        double C = Supplier.calculateDeltaX(p1, p2);
        double D = Supplier.calculateDeltaY(p1, p2);

        // See step 1
        double dotProduct = A*C + B*D;
        double hypotenuse = C*C + D*D;
        double ratio = -1;

        if (hypotenuse != 0){
            ratio = dotProduct / hypotenuse;
        }

        // see step 2
        if (ratio < 0){
            orthogonalX = p1.getX();
            orthogonalY = p1.getY();
        } else if (ratio > 1){
            orthogonalX = p2.getX();
            orthogonalY = p2.getY();
        } else {
            orthogonalX = p1.getX() + ratio *C;
            orthogonalY = p1.getY() + ratio *D;
        }

        return Math.sqrt(Math.pow(abs(x - orthogonalX), 2) + Math.pow(abs(y - orthogonalY), 2));
    }

    private double getAngle(double distance){
        return Math.acos(distance / RADIUS);
    }

    private double calculatePixelCoverage(Vertex3D p1, Vertex3D p2, int x, int y){
        double distance = getDistance(p1, p2, x, y);
        if (distance > MAXDISTANCE) {return 0;}
        double angle = getAngle(distance);
        double numerator =  calculatePieWedge(angle) + calculateTriangle(distance);
        return 1 - (numerator / CIRCLEAREA);
    }

    private double calculateTriangle(double distance){
        return distance*Math.sqrt(RADIUSSQUARED - Math.pow(distance, 2));
    }

    private double calculatePieWedge(double angle){
        double radian = angle/Math.PI;
        return (1 - radian)* CIRCLEAREA;
    }




}
