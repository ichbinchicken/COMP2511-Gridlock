import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.application.Application;

public class BoardScreen extends Screen {

    @FXML
    private TableView table;

    private Controller controller;
    private Stage stage;

    public BoardScreen(Stage s) {
        super("BoardScreen.fxml", "Uni Hell", s);
        controller = new BoardController();
        super.setController(controller);
        stage = s;
    }


}