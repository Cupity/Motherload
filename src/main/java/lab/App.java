package lab;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 *  Class <b>App</b> - extends class Application and it is an entry point of the program
 * @author     Java I
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Canvas canvas;
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) {
        try {
            Rectangle2D screen = Screen.getPrimary().getBounds();
            final double gameWidth = screen.getWidth();
            final double gameHeight = screen.getHeight();

            canvas = new Canvas(gameWidth, gameHeight);
            StackPane root = new StackPane(canvas);

            Scene scene = new Scene(root, gameWidth, gameHeight);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            canvas.scaleXProperty().bind(scene.widthProperty().divide(gameWidth));
            canvas.scaleYProperty().bind(scene.heightProperty().divide(gameHeight));

            DrawingThread drawingThread = new DrawingThread(canvas);
            timer = drawingThread;

            scene.setOnKeyPressed((KeyEvent event) -> {
                drawingThread.getMiner().handleKeyPressed(event.getCode());
            });
            scene.setOnKeyReleased((KeyEvent event) -> {
                drawingThread.getMiner().handleKeyReleased(event.getCode());
            });

            primaryStage.setScene(scene);
            primaryStage.setMaximized(true);
            primaryStage.setTitle("Motherload");
            primaryStage.show();
            primaryStage.setOnCloseRequest(this::exitProgram);

            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void stop() throws Exception {
        timer.stop();
        super.stop();
    }

    private void exitProgram(WindowEvent evt) {
        System.exit(0);
    }
}
