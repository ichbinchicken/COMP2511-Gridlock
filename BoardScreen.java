import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.application.Application;

public class BoardScreen extends Screen {

    @FXML
    private TableView table;

    public BoardScreen(Stage s) {
        super("BoardScreen.fxml", "Current Board", s);

    }
}