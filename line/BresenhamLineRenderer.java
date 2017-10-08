package line;

import geometry.Point3DH;
import geometry.Vertex3D;
import line.helper.Supplier;
import windowing.drawable.Drawable;

/**
 * Created by Andrew on 2017-10-03.
 */
public class BresenhamLineRenderer implements LineRenderer{
    private final static int MIDANGLE = 1;
    private double threshold = 0.5;
    private double offset = 0;
    private int adjust = 1;

    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
        double slope = Supplier.calculateSlope(p1, p2);
        double finalValue = Supplier.calculateAbsValue(slope);
        double intercept = p2.getIntY() - slope * p2.getIntX();


        if(finalValue <= MIDANGLE){
            double y = p1.getY();
            for (int x = p1.getIntX(); x <= p2.getIntX(); x++){
                panel.setPixel(x, (int)y, p1.getZ(), p1.getColor().asARGB());
                offset += finalValue;
                if(offset >= threshold){
                    y += adjust;
                    threshold += 1;
                }
            }
        }
        else{
            int x  = p1.getIntX();
            for (int y = p1.getIntY(); y <= p2.getIntY(); y++){
                panel.setPixel(x, y, p1.getZ(), p1.getColor().asARGB());
                offset += finalValue;
                if(offset >= threshold){
                    x += adjust;
                    threshold += 1;
                }
            }
        }
    }

    public static LineRenderer make(){
        return new AnyOctantLineRenderer(new BresenhamLineRenderer());
    }


}
