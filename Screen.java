import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Screen {
    private String screenTile;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Controller controller;
    protected Scene scene;
    

    public Screen(String fxmlFile, String screenTile, Stage stage) {
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
            scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    
    public void loadShowSettings() {
        stage.setTitle(screenTile);
        fxmlLoader.setController(controller);
        try {
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 800, 600);
            //stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void StageShow() {
    	stage.setScene(scene);
    	stage.show();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
    
    public Scene getScene() {
    	return scene;
    }
}
