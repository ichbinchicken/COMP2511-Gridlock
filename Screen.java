import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Screen {
    private String fxmlFile;
    private String screenTile;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Controller controller;

    public Screen(String fxmlFile, String screenTile, Stage stage) {
        this.fxmlFile = fxmlFile;
        this.screenTile = screenTile;
        this.stage = stage;
        this.controller = null;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
    }

    public void show() {
        stage.setTitle(screenTile);
        fxmlLoader.setController(controller);
        try {
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
