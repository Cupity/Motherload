package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Miner {
    private Point2D position;
    private Point2D speed;

    private final double width = 30;
    private final double height = 30;
    private final double moveSpeed = 100;

    private boolean isUpPressed = false;
    private boolean isDownPressed = false;
    private boolean isLeftPressed = false;
    private boolean isRightPressed = false;

    public Miner(double startX, double startY) {
        this.position = new Point2D(startX, startY);
        this.speed = Point2D.ZERO;
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

        this.speed = new Point2D(vx, vy);
    }

    public void simulate(double deltaT, double screenWidth, double screenHeight) {
        position = position.add(speed.multiply(deltaT));

        double minX = 0;
        double maxX = screenWidth - width;
        double minY = 0;
        double maxY = screenHeight - height - 10;

        double clampedX = Math.max(minX, Math.min(position.getX(), maxX));
        double clampedY = Math.max(minY, Math.min(position.getY(), maxY));

        position = new Point2D(clampedX, clampedY);
    }

    public void draw(GraphicsContext gc) {
        double x = position.getX();
        double y = position.getY();

        gc.setFill(Color.GOLD);
        gc.fillRect(x, y, width, height);

        double[] drillX = { x + 5, x + width - 5, x + (width / 2) };
        double[] drillY = { y + height, y + height, y + height + 10 };
        int pointCount = 3;

        gc.setFill(Color.GRAY);
        gc.fillPolygon(drillX, drillY, pointCount);
    }
}
