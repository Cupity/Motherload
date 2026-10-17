package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class Miner {

    private enum Direction { LEFT, RIGHT, UP, DOWN }
    private static final int CELL_SIZE = 70;

    private static final int[] SIDE_FRAME = {0, 0};  // drill - left
    private static final int[] DOWN_FRAME = {0, 5};  // drill - down
    private static final int[] UP_FRAME = {0, 3};    // propeller - top

    // correction for drill length
    private static final double BODY_X = 22;
    private static final double BODY_Y = 15;
    private static final double BODY_WIDTH = 47;
    private static final double BODY_HEIGHT = 48;

    // miner scaling
    private static final double SPRITE_SCALE = 0.8;

    private Point2D position;
    private Point2D speed;

    private final Image sprite;
    private Direction direction = Direction.LEFT;

    // the hitbox is only the body, not the drill
    private final double width = BODY_WIDTH * SPRITE_SCALE;
    private final double height = BODY_HEIGHT * SPRITE_SCALE;
    private final double moveSpeed = 100;

    private final double drillTime = 0.5;
    private Tile drillingTile = null;
    private double drillTimer = 0;

    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    public Miner(double startX, double startY) {
        this.position = new Point2D(startX, startY);
        this.speed = Point2D.ZERO;
        this.sprite = new Image(getClass().getResourceAsStream("miner_textures.png"));
    }

    public void handleKeyPressed(KeyCode code) {
        switch (code) {
            case W -> isUpPressed = true;
            case S -> isDownPressed = true;
            case A -> isLeftPressed = true;
            case D -> isRightPressed = true;
            default -> {}
        }
        updateSpeed();
    }

    public void handleKeyReleased(KeyCode code) {
        switch (code) {
            case W -> isUpPressed = false;
            case S -> isDownPressed = false;
            case A -> isLeftPressed = false;
            case D -> isRightPressed = false;
            default -> {}
        }
        updateSpeed();
    }

    private void updateSpeed() {
        double vx = 0;
        double vy = 0;

        if (isLeftPressed) vx -= moveSpeed;
        if (isRightPressed) vx += moveSpeed;
        if (isUpPressed) vy -= moveSpeed;
        if (isDownPressed) vy += moveSpeed;

        if (vx < 0) {
            direction = Direction.LEFT;
        } else if (vx > 0) {
            direction = Direction.RIGHT;
        } else if (vy > 0) {
            direction = Direction.DOWN;
        } else if (vy < 0) {
            direction = Direction.UP;
        }

        this.speed = new Point2D(vx, vy);
    }

    public void simulate(double deltaT, double screenWidth, double screenHeight, Tile[][] world) {
        position = new Point2D(position.getX() + speed.getX() * deltaT, position.getY());
        Tile hitX = collideX(world);

        position = new Point2D(position.getX(), position.getY() + speed.getY() * deltaT);
        Tile hitY = collideY(world);

        double minX = 0;
        double maxX = screenWidth - width;
        double minY = 0;
        double maxY = screenHeight - height;

        double clampedX = Math.max(minX, Math.min(position.getX(), maxX));
        double clampedY = Math.max(minY, Math.min(position.getY(), maxY));

        position = new Point2D(clampedX, clampedY);

        // drill the tile we are pushing into
        Tile target = (hitX != null) ? hitX : hitY;
        drill(target, deltaT);
    }

    private boolean overlaps(Tile tile) {
        double x = position.getX();
        double y = position.getY();

        return x < tile.getX() + tile.getSize()
            && x + width > tile.getX()
            && y < tile.getY() + tile.getSize()
            && y + height > tile.getY();
    }

    // returns the tile we hit while moving left or right (or null)
    private Tile collideX(Tile[][] world) {
        Tile hit = null;

        for (Tile[] row : world) {
            for (Tile tile : row) {
                if (tile.isSolid() && overlaps(tile)) {
                    if (speed.getX() > 0) {
                        position = new Point2D(tile.getX() - width, position.getY());
                        hit = tile;
                    } else if (speed.getX() < 0) {
                        position = new Point2D(tile.getX() + tile.getSize(), position.getY());
                        hit = tile;
                    }
                }
            }
        }
        return hit;
    }

    // returns the tile we hit while moving down (or null), going up only blocks
    private Tile collideY(Tile[][] world) {
        Tile hit = null;

        for (Tile[] row : world) {
            for (Tile tile : row) {
                if (tile.isSolid() && overlaps(tile)) {
                    if (speed.getY() > 0) {
                        position = new Point2D(position.getX(), tile.getY() - height);
                        hit = tile;
                    } else if (speed.getY() < 0) {
                        position = new Point2D(position.getX(), tile.getY() + tile.getSize());
                    }
                }
            }
        }
        return hit;
    }

    private void drill(Tile target, double deltaT) {
        if (target == null) {
            drillingTile = null;
            drillTimer = 0;
            return;
        }

        if (target != drillingTile) {
            drillingTile = target;
            drillTimer = 0;
        }

        drillTimer += deltaT;
        if (drillTimer >= drillTime) {
            target.dig();
            drillingTile = null;
            drillTimer = 0;
        }
    }

    public void draw(GraphicsContext gc) {
        int[] frame = switch (direction) {
            case LEFT, RIGHT -> SIDE_FRAME;
            case DOWN -> DOWN_FRAME;
            case UP -> UP_FRAME;
        };

        double sx = frame[0] * CELL_SIZE;
        double sy = frame[1] * CELL_SIZE;
        double bodyCenterX = BODY_X + BODY_WIDTH / 2;

        gc.save();
        gc.translate(position.getX() + width / 2, position.getY());
        if (direction == Direction.RIGHT) {
            gc.scale(-1, 1);
        }
        gc.drawImage(sprite, sx, sy, CELL_SIZE, CELL_SIZE,
            -bodyCenterX * SPRITE_SCALE, -BODY_Y * SPRITE_SCALE,
            CELL_SIZE * SPRITE_SCALE, CELL_SIZE * SPRITE_SCALE);
        gc.restore();
    }
}
