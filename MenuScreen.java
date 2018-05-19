import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.TableView;
import javafx.application.Application;

public class MenuScreen extends Screen {
	//private GameEngine engine;
    @FXML
    private TableView table;

    private Controller controller;
    //private Stage stage;
    //private Main main;

    public MenuScreen(Stage s, GameEngine engine, Main main) {
        super("view/MainMenu2.fxml", "Gradlock", s);
    	//this.engine = engine;
    	//this.main=main;

        controller = new MenuController(main, engine);
        super.setController(controller);
        //stage = s;
    }


}