package line;

import geometry.Vertex3D;
import line.helper.Supplier;
import windowing.drawable.Drawable;
import windowing.graphics.Color;

import static java.lang.Math.round;

/**
 * Created by Administrator on 10/3/2017.
 */
public class AntialiasingLineRenderer implements LineRenderer {
    private static final int MIDANGLE = 1;


    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
        double slope = Supplier.calculateSlope(p1, p2);
        double finalSlope = Supplier.calculateAbsValue(slope);
        Color originalColor = p1.getColor();
        double currentYValue = p1.getY();
        double currentXValue = p1.getX();

        if (Supplier.calculateDeltaX(p1, p2) == 0.0) {
            finalSlope = 1.0;
        }


        double initX = round(currentXValue);
        double initY = integerPart(currentYValue + slope * (initX - currentXValue));
        double fractional = fractionPart(initY);
        double xGap = remainFractionPart(p1.getX() + 0.5);


        double rFractional = remainFractionPart(currentYValue * xGap);

        // handle start point
        if (finalSlope <= MIDANGLE) {
            panel.setPixel((int) initX, integerPart(initY), 0.0, adjustOpacity(originalColor, rFractional).asARGB());
            panel.setPixel((int) initX, integerPart(initY) - 1, 0.0, adjustOpacity(originalColor, fractional).asARGB());
        } else {
            panel.setPixel((int) initX, (int) initY, 0.0, adjustOpacity(originalColor, rFractional).asARGB());
            panel.setPixel((int) initX - 1, (int) initY, 0.0, adjustOpacity(originalColor, fractional).asARGB());
        }
        currentYValue += finalSlope;

        // handle end point
        initX = round(p2.getX());
        initY = integerPart(p2.getY() + slope * (initX - p2.getX()));
        fractional = fractionPart(initY);
        xGap = remainFractionPart(p2.getX() + 0.5);
        rFractional = remainFractionPart(p2.getY() * xGap);
        if (finalSlope <= MIDANGLE) {
            panel.setPixel((int) initX, integerPart(initY), 0.0, adjustOpacity(originalColor, rFractional).asARGB());
            panel.setPixel((int) initX, integerPart(initY) - 1, 0.0, adjustOpacity(originalColor, rFractional).asARGB());
        } else {
            panel.setPixel((int) initX, (int) initY, 0.0, adjustOpacity(originalColor, rFractional).asARGB());
            panel.setPixel((int) initX - 1, (int) initY, 0.0, adjustOpacity(originalColor, rFractional).asARGB());
        }

        // handle midpoints
        if (finalSlope <= MIDANGLE) {
            for (int x = p1.getIntX() + 1; x <= p2.getIntX()-1; x++) {
                fractional = fractionPart(currentYValue);
                rFractional = remainFractionPart(currentYValue);
                panel.setPixel(x, integerPart(currentYValue), 0.0, adjustOpacity(originalColor, rFractional).asARGB());
                panel.setPixel(x, integerPart(currentYValue) - 1, 0.0, adjustOpacity(originalColor, fractional).asARGB());
                currentYValue += finalSlope;
            }

        } else {
            for (int y = p1.getIntY() + 1; y <= p2.getIntY()-1; y++) {
                fractional = fractionPart(currentXValue);
                rFractional = remainFractionPart(currentXValue);

                panel.setPixel(integerPart(currentXValue), y, 0.0, adjustOpacity(originalColor, rFractional).asARGB());
                panel.setPixel(integerPart(currentXValue) - 1, y, 0.0, adjustOpacity(originalColor, fractional).asARGB());
                currentXValue += finalSlope;
            }

        }

    }

    public static LineRenderer make() {
        return new AnyOctantLineRenderer(new AntialiasingLineRenderer());
    }

    private int integerPart(double x) {
        return (int) x;
    }

    private double fractionPart(double x) {
        return x - integerPart(x);
    }

    private double remainFractionPart(double x) {
        return 1.0 - fractionPart(x);
    }

    private Color adjustOpacity(Color color, double fractional) {
        double r = color.getR() * (1 - fractional);
        double g = color.getG() * (1 - fractional);
        double b = color.getB() * (1 - fractional);

        return new Color(r, g, b);
    }
}
