import javafx.stage.Stage;

/**
 * @author Ziming Zheng
 * This class represents a menu screen loader.
 */
public class MenuScreen extends Screen {

    private Controller controller;

    /**
     * Constructor of the menu screen loader
     * @param s current stage
     * @param engine backend interface
     * @param main main application
     */
    public MenuScreen(Stage s, GameEngine engine, Main main) {
        super("view/MainMenu2.fxml", "Gradlock", s);

        controller = new MenuController(main, engine, s);
        super.setController(controller);
    }

}