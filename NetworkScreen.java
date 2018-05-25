//import javafx.fxml.FXML;
import javafx.stage.Stage;
//import javafx.scene.control.TableView;

public class NetworkScreen extends Screen {
	//private GameEngine engine;
    //@FXML
    //private TableView table;

    private Controller controller;
    private Stage stage;

    public NetworkScreen(Stage s, GameEngine engine, Main main) {
        super("view/NetBoardScreen.fxml", "Gradlock", s);
    	//this.engine = engine;
        controller = new NetworkController(s,engine, main);
        super.setController(controller);
        stage = s;
    }
    
    @Override
    public void StageShow() {
    	NetworkController ncontroller = (NetworkController) controller;
    	ncontroller.GetNewBoard();
    	stage.setScene(scene);
    	stage.show();
    }


}