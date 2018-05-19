import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MenuController extends Controller{
	private Main main;
	private GameEngine engine;


	public MenuController(Main main, GameEngine engine) {
		this.main = main;
		this.engine = engine;
	}
	@FXML
    public void initialize() {

        buttonStartGame.setOnAction(new EventHandler<ActionEvent>() {
        	@Override public void handle(ActionEvent e) {
        		main.ShowGameScreen();
        	}
        });
	}

        
        
        @FXML
        private Button buttonStartGame;
        @FXML
        private ToggleGroup ModeGroup;
        @FXML
        private Slider sliderDifficulty;
        @FXML
        private ToggleButton toggleTimed;
        @FXML
        private ToggleButton toggleSpeaker;
        @FXML
        private Button buttonMultiplayer;
        @FXML
        private Button buttonExit;
        @FXML
        private Button buttonHelp;
        @FXML
        private ToggleButton toggleFreePlay;
        @FXML
        private ToggleButton toggleStory;
        @FXML
        private Button buttonSize;
        @FXML
        private Label labelDifficulty;

}
