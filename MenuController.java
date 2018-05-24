import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MenuController extends Controller{
	private Main main;
	private GameEngine engine;
	private Stage stage;
	private boolean showHelp;

	public MenuController(Main main, GameEngine engine, Stage s) {
		this.main = main;
		this.engine = engine;
		this.stage = s;
		showHelp = false;
	}
	
    @FXML
    void StartButtonAction(ActionEvent event) {
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
		double difficulty = sliderDifficulty.getValue();
		engine.SetDifficulty((int) difficulty);
		engine.setMode(mode);
		engine.getNewPuzzle();
		main.ShowGameScreen();
    }
	
    
    
    @FXML
    void MultiplayerButtonAction(ActionEvent event) {
    		engine.getNewPuzzle();
    		main.ShowNetworkScreen();

    }
    
    @FXML
    void ExitButton(ActionEvent event) {
    	stage.close();
    }
    
	@FXML
    public void initialize() {
		ModeGroup = new ToggleGroup();
		labelDifficulty.setText("School Certificate");
		toggleTimed.setToggleGroup(ModeGroup);
		toggleFreePlay.setToggleGroup(ModeGroup);
		toggleStory.setToggleGroup(ModeGroup);
		initHelp();
        ModeGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
        	public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle newToggle) {
        		toggleChanged(newToggle);
        	}
        });
        
        sliderDifficulty.valueProperty().addListener(new ChangeListener<Number>() {
        	public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
        		sliderChanged();
        		}
        	});
	}
	
	public void helpButton() {
		if(showHelp) {
			helpMsg[0].setVisible(false);
			helpMsg[1].setVisible(false);
			showHelp = false;
			
		} else {
		helpMsg[0].setVisible(true);
		helpMsg[1].setVisible(true);
		showHelp = true;
		}
	}
	
	
	
	private void initHelp() {
		//helpMsg = new Popup();
		//helpMsg.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
		Rectangle rect = new Rectangle();
		int helpWidth = 300;
		int helpHeight = 300;
		rect.setWidth(helpWidth);
		rect.setHeight(helpHeight);
		rect.setFill(Color.MINTCREAM);
		rect.setX(menuPane.getPrefWidth()-helpWidth-50);
		rect.yProperty().bind(buttonHelp.layoutYProperty());
		rect.setVisible(false);
		//helpMsg.setX(menuPane.getPrefWidth()+50);
		//helpMsg.setY(-30);
		Text helpMsgText = new Text("ahahahahaha this is empty hahahahhahaha\n this\n is very looooooong hahahahaha");
		helpMsgText.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 16));
		helpMsgText.setWrappingWidth(helpWidth-30);
		helpMsgText.setFill(Color.SEAGREEN);
		helpMsgText.xProperty().bind(rect.xProperty().add(20));
		helpMsgText.yProperty().bind(rect.yProperty().add(30));
		helpMsgText.setVisible(false);
		helpMsg = new Node[2];
		helpMsg[0] = rect;
		helpMsg[1] = helpMsgText;


		menuPane.getChildren().add(rect);
		menuPane.getChildren().add(helpMsgText);
	}	
	
	public void sliderChanged(){
		double difficulty = sliderDifficulty.getValue();
		String text = getDiffString(difficulty);
		labelDifficulty.setText(text);

		engine.SetDifficulty((int)difficulty);
	}
		
	public void toggleChanged(Toggle newToggle) {
		if(newToggle!=toggleStory ) {
				sliderDifficulty.setDisable(false);
        		double difficulty = sliderDifficulty.getValue();
        		String text = getDiffString(difficulty);
        		labelDifficulty.setText(text);
				
			}
		if(newToggle==toggleStory) {
			if(newToggle.isSelected())
			sliderDifficulty.setDisable(true);
			labelDifficulty.setText("Story Mode");
		}
	}

    private String getDiffString(double difficulty) {
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
		return text;

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
    @FXML
	private Pane menuPane;
	private Node[] helpMsg;

}
