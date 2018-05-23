
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class NetworkController extends gameController {

	@FXML
	private Pane boardPane;
	@FXML
	private Pane NboardPane;

	
	public NetworkController(Stage s, GameEngine engine, Main main) {
		super(s,engine,main);
    }

	
    public void initialize() {
    	engine.getNewPuzzle();
    	drawBoard(NboardPane);
    	
        //nSquares = engine.getBoardSize(); //this will be replaced dynamically.

        //this.squareWidth = boardPane.getPrefWidth()/nSquares;

    }
}
