package client.testPages;

import line.LineRenderer;
import windowing.drawable.Drawable;

/**
 * Created by Administrator on 10/3/2017.
 */
public class RandomLineTest {

    private final LineRenderer renderer;
    private final Drawable panel;

    public RandomLineTest(Drawable panel, LineRenderer renderer){
        this.renderer = renderer;
        this.panel = panel;
    }
}
