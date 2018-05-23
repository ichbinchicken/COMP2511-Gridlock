
import javafx.stage.Stage;
public class MenuScreen extends Screen {

    private Controller controller;

    public MenuScreen(Stage s, GameEngine engine, Main main) {
        super("view/MainMenu2.fxml", "Gradlock", s);

        controller = new MenuController(main, engine);
        super.setController(controller);
      
    }


}
