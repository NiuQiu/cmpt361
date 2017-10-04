package windowing.drawable;

/**
 * Created by Administrator on 10/3/2017.
 */
public class ColoredDrawable extends DrawableDecorator {

    private int argbColor;

    public ColoredDrawable(Drawable delegate, int argbColor) {
        super(delegate);
        this.argbColor = argbColor;
    }

    /*@Override
    public void setPixel(int x, int y, double z, int argbColor) {
        delegate.setPixel(x,  y,  z, this.argbColor);
    }*/
}
