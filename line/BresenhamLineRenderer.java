package line;

import geometry.Vertex3D;
import windowing.drawable.Drawable;

/**
 * Created by Andrew on 2017-10-03.
 */
public class BresenhamLineRenderer implements LineRenderer{
    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {

    }

    public static LineRenderer make(){
        return new AnyOctantLineRenderer(new BresenhamLineRenderer());
    }
}
