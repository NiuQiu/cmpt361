package line;

import geometry.Vertex3D;
import line.helper.Supplier;
import windowing.drawable.Drawable;

/**
 * Created by Andrew on 2017-10-03.
 */
public class DDALineRenderer implements LineRenderer{
    private static final int MIDANGLE = 1;

    /**
     * implement DDA algorithm to calculate the points
     * @param p1
     * @param p2
     * @param panel
     */
    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
        double slope = Supplier.calculateSlope(p1, p2);
        double finalValue = Supplier.calculateAbsValue(slope);

        double intercept = p2.getIntY() - slope * p2.getIntX();

        if(finalValue <= MIDANGLE){
            for (int x = p1.getIntX(); x <= p2.getIntX(); x++){
                int y = Supplier.calculateY(slope, x, intercept);
                panel.setPixel(x, y, p1.getZ(), p1.getColor().asARGB());
            }
        }
        else{
            for (int y = p1.getIntY(); y <= p2.getIntY(); y++){
                int x = Supplier.calculateX(slope, y, intercept);
                panel.setPixel(x, y, p1.getZ(), p1.getColor().asARGB());
            }
        }
    }

    /**
     * singleton instance
     * @return
     */
    public static LineRenderer make(){
        return new AnyOctantLineRenderer(new DDALineRenderer());
    }




}
