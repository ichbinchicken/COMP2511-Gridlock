
import javafx.stage.Stage;
public class BoardScreen extends Screen {

    private Controller controller;
    private Stage stage;

    public BoardScreen(Stage s, GameEngine engine, Main main) {
        super("view/BoardScreen.fxml", "Gradlock", s);
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
