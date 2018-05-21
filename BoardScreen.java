//import javafx.fxml.FXML;
import javafx.stage.Stage;
//import javafx.scene.control.TableView;

public class BoardScreen extends Screen {
	//private GameEngine engine;
    //@FXML
    //private TableView table;

    private Controller controller;
    private Stage stage;

    public BoardScreen(Stage s, GameEngine engine, Main main) {
        super("view/BoardScreen.fxml", "Gradlock", s);
    	//this.engine = engine;
        controller = new BoardController(s,engine, main);
        super.setController(controller);
        stage = s;
    }
    
    @Override
    public void StageShow() {
    	BoardController bcontroller = (BoardController) controller;
    	bcontroller.GetNewBoard();
    	stage.setScene(scene);
    	stage.show();
    }


}