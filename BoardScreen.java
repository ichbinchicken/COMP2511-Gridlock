import javafx.stage.Stage;
/**
 * GradLock Project
 * @author Ziming Zheng and Michael Hamilton
 * This class will show the screen for single player board
 */
public class BoardScreen extends Screen {
    private Controller controller;
    private Stage stage;

    /**
     * Constructor of BoardScreen
     * @param s the stage required by javafx
     * @param engine the game engine, "interface" to connect with backend
     * @param main game launcher to switch the screens
     * @pre s != null && engine != null && main != null
     * @post controller != null
     */
    public BoardScreen(Stage s, GameEngine engine, Main main) {
        super("view/BoardScreen.fxml", "Gradlock", s);
    	//this.engine = engine;
        controller = new BoardController(s,engine, main);
        super.setController(controller);
        stage = s;
    }

    /**
     * Show the stage required by javafx
     * @pre none
     * @post none
     */
    @Override
    public void StageShow() {
    	BoardController bcontroller = (BoardController) controller;
    	bcontroller.GetNewBoard();
    	stage.setScene(scene);
    	stage.show();
    }


}