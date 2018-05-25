import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	Screen gameScreen;
	Screen menuScreen;
	Screen networkScreen;
	GameEngine engine;
	public Main() {
		
	}
	
    @Override
    public void start(Stage primaryStage) throws Exception {
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
        System.exit(0);

    }
    public void ShowGameScreen() {
    	gameScreen.StageShow();
    }
    public void ShowMenuScreen() {
    	menuScreen.StageShow();
    }
    
    public void ShowNetworkScreen() {
    	networkScreen.StageShow();
    }
}


