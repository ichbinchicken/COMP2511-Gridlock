import javafx.stage.Stage;

/**
 * @author Michael Hamilton
 * This class represents a Beat-AI game screen loader.
 */
public class NetworkScreen extends Screen {

    private Controller controller;
    private Stage stage;

    /**
     * Constructor for a Beat-AI game screen loader
     * @param s current stage
     * @param engine backend interface
     * @param main main app
     */
    public NetworkScreen(Stage s, GameEngine engine, Main main) {
        super("view/NetBoardScreen.fxml", "Gradlock", s);
        controller = new NetworkController(s,engine, main);
        super.setController(controller);
        stage = s;
    }

    /**
     * Show the screen on stage
     */
    @Override
    public void StageShow() {
    	NetworkController ncontroller = (NetworkController) controller;
    	ncontroller.GetNewBoard();
    	stage.setScene(scene);
    	stage.show();
    }


}