package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class Tile {

    private final Point2D position;
    private final double size;

    private final int col;
    private final int row;

    public Tile(double x, double y, double size, int col, int row) {
        this.position = new Point2D(x, y);
        this.size = size;
        this.col = col;
        this.row = row;
    }

    public void draw(GraphicsContext gc, TextureAtlas atlas) {
        atlas.draw(gc, col, row, position.getX(), position.getY(), size, size);
    }
}
