import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class MenuController extends Controller{
	private Main main;
	private GameEngine engine;


	public MenuController(Main main, GameEngine engine) {
		this.main = main;
		this.engine = engine;
	}
	@FXML
    public void initialize() {
		ModeGroup = new ToggleGroup();
		labelDifficulty.setText("School Certificate");
		toggleTimed.setToggleGroup(ModeGroup);
		toggleFreePlay.setToggleGroup(ModeGroup);
		toggleStory.setToggleGroup(ModeGroup);

        buttonStartGame.setOnAction(new EventHandler<ActionEvent>() {
        	@Override public void handle(ActionEvent e) {
        		Toggle t = ModeGroup.getSelectedToggle();
        		Mode mode = Mode.TIMED;
        		if(t==toggleFreePlay) {
        			mode = Mode.FREEPLAY;
        		}
        		else if(t==toggleTimed) {
        			mode = Mode.TIMED;
        		}
        		else if(t==toggleStory) {
        			mode = Mode.STORY;
        		}

        		
        			
        		engine.setMode(mode);
        		engine.getNewPuzzle();
        		main.ShowGameScreen();
        	}
        });
        
        sliderDifficulty.valueProperty().addListener(new ChangeListener<Number>() {
        	public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
        		//System.out.println(new_val);
        		double difficulty = sliderDifficulty.getValue();
        		int diff = (int) Math.round(difficulty);
        		String text;
        		switch(diff) {
        		case 0:
        			text="School Certificate";
        			break;
        		case 1:
        			text="Higher School Certificate";
        			break;
        		case 2:
        			text="Bachelor Degree";
        			break;
        		case 3:
        			text="Masters Degree";
        			break;
				case 4:
					text="Doctoral Degree";
					break;
				default:
					text="SC";
				}
        		
        		labelDifficulty.setText(text);

        		engine.SetDifficulty((int)difficulty);
        	}
        });
        
        //buttonExit.setOnAction(actionEvent -> Platform.exit());
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
