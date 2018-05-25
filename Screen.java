import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * @author Michael Hamilton & Ziming Zheng
 * This is the base class for a screen loader.
 * All screen loaders for each specific screen needs to extend this class.
 * It instantiates a controller for the screen dynamically and loads the fxml.
 */
public abstract class Screen {
    private String screenTile;
    private Stage stage;
    private FXMLLoader fxmlLoader;
    private Controller controller;
    protected Scene scene;

    /**
     * Constructor for the base screen loader
     * @param fxmlFile the FXML file name
     * @param screenTile the title of the screen
     * @param stage the current stage
     */
    public Screen(String fxmlFile, String screenTile, Stage stage) {
       // this.fxmlFile = fxmlFile;
        this.screenTile = screenTile;
        this.stage = stage;
        this.controller = null;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
    }

    /**
     * Init the screen, connects controller with view, loads FXML file and show on stage
     */
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

    /**
     * Init the screen without show it on stage
     */
    public void loadShowSettings() {
        stage.setTitle(screenTile);
        fxmlLoader.setController(controller);
        try {
            Parent root = fxmlLoader.load();
            scene = new Scene(root, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a screen that is already init'd
     */
    public void StageShow() {
    	stage.setScene(scene);
    	stage.show();
    }

    /**
     * Setter for the controller of the screen
     * @param controller the controller of the screen
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Getter for the scene of the screen
     * @return the scene of the screen
     */
    public Scene getScene() {
    	return scene;
    }
}
