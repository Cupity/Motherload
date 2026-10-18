package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Tile implements Drawable {

    private final Point2D position;
    private final double size;

    private final int col;
    private final int row;

    private final TextureAtlas atlas;

    private boolean solid = true;

    public Tile(double x, double y, double size, int col, int row, TextureAtlas atlas) {
        this.position = new Point2D(x, y);
        this.size = size;
        this.col = col;
        this.row = row;
        this.atlas = atlas;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public double getSize() {
        return size;
    }

    public boolean isSolid() {
        return solid;
    }

    public void dig() {
        solid = false;
    }

    // Draws tunnel if mined
    @Override
    public void draw(GraphicsContext gc) {
        if (!solid) {
            gc.setFill(Color.rgb(70, 45, 30));
            gc.fillRect(position.getX(), position.getY(), size, size);
            return;
        }
        atlas.draw(gc, col, row, position.getX(), position.getY(), size, size);
    }
}
