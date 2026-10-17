package lab;

import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class DrawingThread extends AnimationTimer {

    private static final double NANOSECONDS_IN_SECOND = 1_000_000_000.0;

    // the texture sheet is a 7x7 grid
    private static final int ATLAS_COLUMNS = 7;
    private static final int ATLAS_ROWS = 7;

    private static final double TILE_SIZE = 60;
    private static final double SKY_HEIGHT = 80;

    private static final int DIRT_COL = 0;
    private static final int DIRT_ROW = 0;

    private static final double MAX_DELTA_T = 0.05;

    private final Canvas canvas;
    private final GraphicsContext gc;

    private final Miner miner;
    private final TextureAtlas atlas;
    private final Random random = new Random();

    private final int columns;
    private final int rows;
    private final Tile[][] world;

    private long lastTime;

    public DrawingThread(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        Image sheet = new Image(getClass().getResourceAsStream("tile_textures.png"));
        this.atlas = new TextureAtlas(sheet, ATLAS_COLUMNS, ATLAS_ROWS);

        this.miner = new Miner(385, 30);

        // how many tiles fit
        columns = (int) Math.ceil(canvas.getWidth() / TILE_SIZE);
        rows = (int) Math.ceil((canvas.getHeight() - SKY_HEIGHT) / TILE_SIZE);

        world = new Tile[rows][columns];
        generateWorld();

        this.lastTime = System.nanoTime();
    }

    public Miner getMiner() {
        return miner;
    }

    private void generateWorld() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                world[row][col] = createTile(row, col);
            }
        }
    }

    private Tile createTile(int row, int col) {
        double x = col * TILE_SIZE;
        double y = SKY_HEIGHT + row * TILE_SIZE;

        // 0.0 at the surface, 1.0 at the bottom
        double depth = (double) row / (rows - 1);

        if (row > 0) {
            if (depth > 0.7 && random.nextDouble() < 0.04) {
                return new Tile(x, y, TILE_SIZE, 3, 2);   // lava
            }
            if (depth > 0.6 && random.nextDouble() < 0.02) {
                return new Tile(x, y, TILE_SIZE, 4, 1);   // emerald
            }
            if (depth > 0.5 && random.nextDouble() < 0.03) {
                return new Tile(x, y, TILE_SIZE, 5, 1);   // gold
            }
            if (depth > 0.25 && random.nextDouble() < 0.05) {
                return new Tile(x, y, TILE_SIZE, 1, 1);   // silver
            }
            if (random.nextDouble() < 0.08) {
                return new Tile(x, y, TILE_SIZE, 0, 1);   // copper
            }
        }

        return new Tile(x, y, TILE_SIZE, DIRT_COL, DIRT_ROW);
    }

    @Override
    public void handle(long now) {
        double deltaT = (now - lastTime) / NANOSECONDS_IN_SECOND;
        lastTime = now;

        if (deltaT > MAX_DELTA_T) {
            deltaT = MAX_DELTA_T;
        }

        double screenWidth = canvas.getWidth();
        double screenHeight = canvas.getHeight();

        gc.setFill(Color.SKYBLUE);
        gc.fillRect(0, 0, screenWidth, SKY_HEIGHT);

        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, SKY_HEIGHT, screenWidth, screenHeight - SKY_HEIGHT);

        miner.simulate(deltaT, screenWidth, screenHeight, world);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                world[row][col].draw(gc, atlas);
            }
        }
        miner.draw(gc);
    }
}
