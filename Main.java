import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setResizable(false);
        GameEngine engine = new GameEngine();
        Screen screen = new BoardScreen(primaryStage,engine);
        screen.show();

    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0); //Close all threads when window is closed

    }
}
