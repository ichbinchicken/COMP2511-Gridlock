import javafx.application.Application;
import javafx.stage.Stage;
/**
 * @author Michael Hamilton & Ziming Zheng
 * Main class to launch the app
 *
 */
public class Main extends Application {
	Screen gameScreen;
	Screen menuScreen;
	Screen networkScreen;
	GameEngine engine;
	public Main() {
		
	}

    /**
     * Start the app and load the screens
     * @param primaryStage main stage of the app
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

    	//Parent root = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));
    	//primaryStage.setTitle("Gridlock");
    	//primaryStage.setScene(new Scene(root, 400, 500));
    	//primaryStage.show();
    	
        primaryStage.setResizable(false);
        engine = new GameEngine();
        gameScreen = new BoardScreen(primaryStage,engine, this);
        menuScreen = new MenuScreen(primaryStage,engine, this);
        networkScreen = new NetworkScreen(primaryStage,engine,this);
        networkScreen.loadShowSettings();
        gameScreen.loadShowSettings();
        menuScreen.show();


    }


    public static void main(String[] args) {
        launch(args);
        System.exit(0); //Close all threads when window is closed

    }

    /**
     * Show a single-player game screen
     */
    public void ShowGameScreen() {
    	gameScreen.StageShow();
    }

    /**
     * Show a menu screen
     */
    public void ShowMenuScreen() {
    	menuScreen.StageShow();
    }

    /**
     * Show Beat-AI game screen
     */
    public void ShowNetworkScreen() {
    	networkScreen.StageShow();
    }
}


