package lab;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class StartMenuController {

    @FXML
    private AnchorPane root;

    @FXML
    private ImageView background;

    private Runnable onStart;

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    @FXML
    private void initialize() {
        background.fitWidthProperty().bind(root.widthProperty());
        background.fitHeightProperty().bind(root.heightProperty());
    }

    @FXML
    private void onStartClicked() {
        if (onStart != null) {
            onStart.run();
        }
    }
}
