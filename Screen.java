import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public abstract class Screen {
    private String fxmlFile;
    private String screenTile;
    private Stage stage;
    private FXMLLoader fxmlLoader;

    public Screen(String fxmlFile, String screenTile, Stage stage) {
        this.fxmlFile = fxmlFile;
        this.screenTile = screenTile;
        this.stage = stage;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
    }

    public void show() {
        stage.setTitle(screenTile);

    }
}
