import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.application.Application;

public class BoardScreen extends Screen {
	private GameEngine engine;
    @FXML
    private TableView table;

    private Controller controller;
    private Stage stage;

    public BoardScreen(Stage s, GameEngine engine) {
        super("BoardScreen.fxml", "Uni Hell", s);
    	this.engine = engine;
        controller = new BoardController(engine);
        super.setController(controller);
        stage = s;
    }


}