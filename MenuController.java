
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

/**
 * @author Michael Hamilton & Ziming Zheng
 * Controller for the main menu
 *
 */
public class MenuController extends Controller {
	private Main main;
	private GameEngine engine;
	private Stage stage;
	private boolean showHelp;
	private int port;
	public MenuController(Main main, GameEngine engine, Stage s) {
		this.main = main;
		this.engine = engine;
		this.stage = s;
		showHelp = false;
	}
	
	
    /**
     * Start button pressed
     * Go to solo mode screen
     * Select mode and difficulty
     * @param event
     * 
     */
    @FXML
    void StartButtonAction(ActionEvent event) {
		engine.NetworkSetMode(false);

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
	
    
    
    /**
     * Multiplayer button pressed
     * Get new puzzle and go to multiplayer screen
     * @param event
     */
    @FXML
    void MultiplayerButtonAction(ActionEvent event) {
    		engine.getNewPuzzle();
    		engine.NetworkSetMode(true);
    		//main.ShowNetworkScreen();

    }
    
    /**
     * Exit button pressed
     * Quit game
     * @param event
     */
    @FXML
    void ExitButton(ActionEvent event) {
    	stage.close();
    }
    
	/* (non-Javadoc)
	 * @see Controller#initialize()
	 * Initialize toggle buttons mode select
	 * Initialize difficulty updating on slider movement
	 */
	@FXML
    public void initialize() {
		HostIPAddr.setVisible(false);
		HostIDTextInput.setVisible(false);
    	IPEnterBut.setVisible(false);

		ModeGroup = new ToggleGroup();
		labelDifficulty.setText("School Certificate");
		toggleTimed.setToggleGroup(ModeGroup);
		toggleFreePlay.setToggleGroup(ModeGroup);
		toggleStory.setToggleGroup(ModeGroup);
		initHelp();
		//PortTextField = new TextField();
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
	
	/**
	 * Help button pressed
	 * Show or hide help screen
	 */
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
	
	
	
	/**
	 * Initialize help screen
	 */
	private void initHelp() {
		//helpMsg = new Popup();
		//helpMsg.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_TOP_RIGHT);
		Rectangle rect = new Rectangle();
		int helpWidth = 600;
		int helpHeight = 240;
		rect.setWidth(helpWidth);
		rect.setHeight(helpHeight);
		//Color mycolour = new Color(46/255,139/255,87/255,240/255);
		rect.setFill(Color.LEMONCHIFFON);
		rect.setX(menuPane.getPrefWidth()-helpWidth-buttonHelp.getPrefWidth()-30);
		rect.yProperty().bind(buttonHelp.layoutYProperty());
		rect.setVisible(false);
		//helpMsg.setX(menuPane.getPrefWidth()+50);
		//helpMsg.setY(-30);
		helpMsgText.setTextAlignment(TextAlignment.CENTER);
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
	
	/**
	 * Slider change detected
	 * Update label
	 */
	public void sliderChanged(){
		double difficulty = sliderDifficulty.getValue();
		String text = getDiffString(difficulty);
		labelDifficulty.setText(text);

		engine.SetDifficulty((int)difficulty);
	}
		
	/**
	 * Toggle button changed
	 * @param newToggle
	 */
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

    /**
     * Convert difficulty level from slider to string difficulty name
     * @param difficulty
     * @return Difficulty name
     */
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
    void HostButton(ActionEvent event) {
    	engine.setUpNetworking();
    	String portStr = PortTextField.getText();
    	if(portStr!=null) {
    		int port = Integer.parseInt(portStr);
    		engine.hostGame(port);
	    	System.out.println("HOST" + port);
	    	System.out.println("IP" + engine.getIP());
	    	HostIPAddr.setText(engine.getIP());
	    	HostIPAddr.setVisible(true);
	    	HostIPAddr.toFront();
	    	HostIDTextInput.toBack();
	    	HostIDTextInput.setVisible(false);
	    	IPEnterBut.setVisible(false);
	    	engine.waitForJoin();
	    	
    		engine.getNewPuzzle();
    		engine.NetworkSetMode(true);
    		main.ShowNetworkScreen();

	    	
    	}
    	
    }

    @FXML
    void JoinButton(ActionEvent event) {
    	engine.setUpNetworking();

    	String portStr = PortTextField.getText();
    	//System.out.println(port);
    	if(portStr!=null) {
    		port = Integer.parseInt(portStr);
	    	HostIPAddr.setVisible(false);
	    	HostIPAddr.toBack();
	    	HostIDTextInput.toBack();
	    	HostIDTextInput.setVisible(true);
	    	IPEnterBut.setVisible(true);
	    	
	    	

    	}

    }
    
    @FXML
    void IPEnter(ActionEvent event) {
    	String IP = HostIDTextInput.getText();
    	System.out.println(IP);
		engine.joinGame(IP, port);
		
		engine.NetworkSetMode(true);
		main.ShowNetworkScreen();



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
    private Label HostIPAddr;
    @FXML
    private Button JoinButton;

    @FXML
    private Button HostButton;

    @FXML
    private TextField PortTextField;
    
    @FXML
    private TextField HostIDTextInput;
    
    @FXML
    private Button IPEnterBut;




    @FXML
	private Pane menuPane;
	private Node[] helpMsg;
	private final Text helpMsgText = new Text
			("You're a busy student who's trying to Graduate!\n "
					+ "But before you can Graduate you have to complete all your assignments.\n"
					+ "To complete them, and move them away, simply click and drag.\n"
					+ "But they can only move on one axis and they can't move through other assignments.\n"
					+ "Once all your assignments have been moved, you will automatically run to the edge and graduate!\n"
					+ "With each level you complete you will be graded\n"
					+ "Relax with free play, Race against the clock with timed or Challenge yourself with story mode and see just how far you can go!");

}
