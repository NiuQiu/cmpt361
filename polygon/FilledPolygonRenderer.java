package polygon;

import windowing.drawable.Drawable;

/**
 * Created by Administrator on 10/3/2017.
 */
public class FilledPolygonRenderer implements PolygonRenderer{
    @Override
    public void drawPolygon(Polygon polygon, Drawable drawable, Shader vertexShader) {

    }

    @Override
    public void drawPolygon(Polygon polygon, Drawable panel) {

    }

    public static FilledPolygonRenderer make(){
        return  new FilledPolygonRenderer();
    }
}
