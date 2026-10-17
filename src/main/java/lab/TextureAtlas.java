package lab;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class TextureAtlas {

    private Image sheet;
    private double cellWidth;
    private double cellHeight;

    public TextureAtlas(Image sheet, int columns, int rows) {
        this.sheet = sheet;
        cellWidth = sheet.getWidth() / columns;
        cellHeight = sheet.getHeight() / rows;
    }

    public void draw(GraphicsContext gc, int col, int row, double x, double y, double width, double height) {
        double sx = col * cellWidth;
        double sy = row * cellHeight;

        gc.drawImage(sheet, sx, sy, cellWidth, cellHeight, x, y, width, height);
    }
}
