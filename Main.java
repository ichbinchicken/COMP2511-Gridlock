import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	Screen gameScreen;
	Screen menuScreen;
	GameEngine engine;
	public Main() {
		
	}
	
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
        gameScreen.loadShowSettings();

        menuScreen.show();


    }

    public static void main(String[] args) {
        launch(args);
        System.exit(0); //Close all threads when window is closed

    }
    
    
    public void ShowGameScreen() {
    	gameScreen.StageShow();
    }
    public void ShowMenuScreen() {
    	menuScreen.StageShow();
    }
}


