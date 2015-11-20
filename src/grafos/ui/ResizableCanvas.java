package grafos.ui;

import javafx.scene.canvas.Canvas;

/**
 * Created by dfcarvalho on 8/21/15.
 */
public class ResizableCanvas extends Canvas {

    public ResizableCanvas(double width, double height) {
        super(width, height);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth() - 100;
    }

    @Override
    public double prefHeight(double width) {
        return getHeight() - 100;
    }
}
