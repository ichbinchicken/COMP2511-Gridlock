import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public Main() {
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Screen screen = new BoardScreen(primaryStage);
        screen.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
