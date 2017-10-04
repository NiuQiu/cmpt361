package line;

import geometry.Vertex3D;
import windowing.drawable.Drawable;

/**
 * Created by Administrator on 10/3/2017.
 */
public class AntialiasingLineRenderer implements LineRenderer{
    @Override
    public void drawLine(Vertex3D p1, Vertex3D p2, Drawable panel) {
        // do nothing
    }

    public static LineRenderer make(){
        return new AnyOctantLineRenderer(new AntialiasingLineRenderer());
    }
}
