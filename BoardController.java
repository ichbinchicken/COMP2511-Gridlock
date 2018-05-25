import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * GradLock Project
 * @author Ziming Zheng and Michael Hamilton
 * This class is the controller to connect front end and back end
 * for single player board screen.
 */
public class BoardController extends GameController {
	private static final String[] GAME_OVER_MSGS = {"GAME OVER", "TRY AGAIN", "Your Moves: ", "Optimal Moves: "};
    private static final String[] GAME_WON_MSGS = {"YOU WON", "Time used: ", "Your Moves: ", "Optimal Moves: ", "Your grade: "};

    @FXML
    private Label totalTime;
    @FXML
    private Label movesMade;
    @FXML
    private Button buttonPause;
    @FXML
    private Button buttonRestart;
    @FXML
    private Button buttonHint;

    private Timeline countDown;
    private int totalSeconds;  // The duration of game, should not changed *TOBY but needs to be reset for each new board
    private int currSeconds;
    private boolean running;
    private Mode mode;

    /**
     * Constructor of BoardController
     * @param s the stage of app, required by javafx
     * @param engine the backend "interface" object so that we can call backend methods
     * @param main game launcher, holding this reference for screen switching
     * @pre s != null && engine != null && main != null
     * @post none
     */
    public BoardController(Stage s, GameEngine engine, Main main) {
    	super(s,engine,main);
        totalSeconds = currSeconds = engine.getTime();
        running = true; // this is to check whether the game is paused. Initially, it's running.
    }

    /**
     * Method to handle the action when "Hint" button is clicked
     * @pre none
     * @post none
     */
	@FXML
	void HintAction() {
		if(animating==false && isGameWon == false) {
    		int[] arr =engine.getNextMove();
    		//System.out.println(arr);
    		Car car =findCar(arr[0], arr[1]);
    		animating=true;
    		car.CarMakeAnimatingMove(arr[2], arr[3], animTime);
    	}
	}
	
    /**
     * Method to handle the action when "Pause" button is clicked
     * @pre none
     * @post none
     */
	@FXML
    void PauseAction(ActionEvent event) {
		if(running) {
			running = false;
			countDown.pause();
			buttonHint.setDisable(true);
			buttonPause.setText("Resume");
			curtainShow();
		}
		else {
			if(mode!=Mode.STORY) {
				buttonHint.setDisable(false);
			}
			curtainHide();
			countDown.play();
			running=true;
			buttonPause.setText("Pause");
		}
    }

    /**
     * Method to handle the action when "Restart" button is clicked
     * @param event
     * @pre event != null
     * @post boardPane != null
     */
    @FXML
    void RestartAction(ActionEvent event) {
    	if(animating==false) {
    		engine.RestartPuzzle();
    		boardPane.getChildren().clear();
            currSeconds = totalSeconds;
            isGameWon=false;
            animating=false;
            running = true;
            workload = drawBoard(boardPane, workload, true);
        	drawBoardAdit();

            boardPane.getChildren().add(curtain);
            isGameWon=false;
            animating=false;
            running=true;
            movesMade.setText("0");
            curtainHide();
    	}
    }

    /**
     * Method for initializing the setting of single player board screen
     * @pre none
     * @post countDown != null
     */
    @FXML
    public void initialize() {
    	super.initialize();

        countDown = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currSeconds--;
                totalTime.setText(convertTime(currSeconds));
                if (currSeconds <= 0 && !isGameWon) {
    				engine.GameLoss();
                	if(mode==Mode.STORY) {
                        buttonNewGame.setDisable(true);
                	}
                	stopGame(GAME_OVER_MSGS[0]);
                }
            }
        }));
        countDown.setCycleCount(Animation.INDEFINITE);
        countDown.playFromStart(); // initialise the timer at the first time

    }

    /**
     * Method for displaying the "You Won" screen and stop the game for each round
     * @pre none
     * @post none
     */
    @Override
    public void DisplayWinScreen() {
    	stopGame(GAME_WON_MSGS[0]);
    }

    /**
     * Method for getting a single player board screen with settings
     * @pre none
     * @post none
     */
    @Override
    public void GetNewBoard(){
    	super.GetNewBoard();
    	drawBoardAdit();
    }

    /**
     * Method for drawing or setting additional things for a single player board screen
     * @pre none
     * @post none
     */
    private void drawBoardAdit() {
        totalSeconds = currSeconds = engine.getTime();
        running=true;
		countDown.play();
    	buttonPause.setDisable(false);
    	buttonPause.setText("Pause");
    	buttonNewGame.setDisable(false);
    	totalTime.setText(convertTime(totalSeconds));
    	mode = engine.getMode();
    	totalSeconds = engine.getTime();
    	currSeconds = totalSeconds;
    	countDown.playFromStart();
    	if(mode==Mode.STORY) {
    		buttonHint.setDisable(true);
            buttonRestart.setDisable(true);
            buttonNewGame.setDisable(true);
            buttonNewGame.setText("Next");
    	}
    	else {
    		buttonHint.setDisable(false);
            buttonRestart.setDisable(false);
            buttonNewGame.setDisable(false);
            buttonNewGame.setText("New Game");
    	}
    }

    /**
     * Method for stopping the game for each level/round instead of the entire mode
     * @param msg the message to be displayed on the screen when game stops,
     *            defined in final type in this class
     * @pre msg != none
     * @post none
     */
    private void stopGame(String msg) {
        //System.out.println("msg="+msg);
        double boardHeight = boardPane.getPrefHeight();
        //double boardWidth = boardPane.getWidth();

        countDown.pause();
        buttonPause.setDisable(true);
        buttonHint.setDisable(true);
        buttonNewGame.setDisable(false);
        curtain.setVisible(true);
        curtain.toFront();
        Label message = new Label();
        message.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 40));
        message.setTextFill(primaryTextColor);
        message.setTextAlignment(TextAlignment.CENTER);
        setCenterX(boardPane, message);
        boardPane.getChildren().add(message);
        message.setText(msg);
        if (engine.StoryModeEnd()==true) {
        	StoryModeEndScreen();
        }
        else if (isGameWon) {
            message.setLayoutY(boardHeight/4);
            String timeElapsed = convertTime(totalSeconds - currSeconds);
            Label[] details = new Label[4];
            details[0] = new Label(GAME_WON_MSGS[1]+timeElapsed);
            details[0].setLayoutY(boardHeight*5/12);
            details[1] = new Label(GAME_WON_MSGS[2]+engine.getMoves());
            details[1].setLayoutY(boardHeight/2);
            int minMoves = engine.getMinMoves();
            details[2] = new Label(GAME_WON_MSGS[3]+minMoves);
            details[2].setLayoutY(boardHeight*7/12);
            details[3] = new Label(GAME_WON_MSGS[4]+engine.CalculateGrade(currSeconds).getString());  // needs to be replaced by actual grade
            details[3].setLayoutY(boardHeight*2/3);

            boardPane.getChildren().addAll(details);
            for(int i = 0; i < GAME_WON_MSGS.length-1; i++) {
                setCenterX(boardPane, details[i]);
                details[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
                details[i].setTextFill(secondaryTextColor);
                details[i].setTextAlignment(TextAlignment.CENTER);
                details[i].toFront();
            }
            message.setVisible(true);
            message.toFront();

            if (mode == Mode.STORY) {
                String gradLevel = engine.StoryGetGradLevel();
                if (gradLevel.equals("FAILED")) {
                    gradLevel = "Starting School";
                }
                Label storyLevel = new Label("Progress: "+engine.getStoryLevel()+"/10  "+gradLevel);
                storyLevel.setLayoutY(boardHeight*3/4);
                setCenterX(boardPane, storyLevel);
                storyLevel.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 16));
                storyLevel.setTextFill(secondaryTextColor);
                storyLevel.setTextAlignment(TextAlignment.CENTER);
                storyLevel.toFront();
                boardPane.getChildren().add(storyLevel);
            }


        } else {
            message.setLayoutY(boardHeight*3/8);
            Label prompt = new Label(GAME_OVER_MSGS[1]);
            Label[] details = new Label[2];
            prompt.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 30));
            prompt.setTextFill(secondaryTextColor);
            prompt.setTextAlignment(TextAlignment.CENTER);
            prompt.setLayoutY(boardHeight*16/24);
            details[0] = new Label(GAME_OVER_MSGS[2]+engine.getMoves());
            details[0].setLayoutY(boardHeight/2);
            int minMoves = engine.getMinMoves();
            details[1] = new Label(GAME_OVER_MSGS[3]+minMoves);
            details[1].setLayoutY(boardHeight*14/24);
            boardPane.getChildren().addAll(details);

            for(int i = 0; i < 2; i++) {
                setCenterX(boardPane, details[i]);
                details[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
                details[i].setTextFill(secondaryTextColor);
                details[i].setTextAlignment(TextAlignment.CENTER);
                details[i].toFront();
            }
            setCenterX(boardPane, prompt);
            boardPane.getChildren().add(prompt);
            prompt.toFront();
            message.setVisible(true);
            message.toFront();


        }
    }

    /**
     * Method for getting "game won" state from backend via GameEngine
     * @pre none
     * @post none
     */
    @Override
    protected void GameWon() {
        engine.GameWon(currSeconds);
        countDown.stop();
        
    }

     /**
      * Method for showing the ending screen in story mode
      * @pre none
      * @post boardPane != null
      */
    protected void StoryModeEndScreen() {
        buttonNewGame.setDisable(true);
        double boardHeight = boardPane.getPrefHeight();
        double boardWidth = boardPane.getPrefWidth();

    	ArrayList<GradeLvl> gradeList = engine.StoryGetAllGrades();
    	String gradLevel = engine.StoryGetGradLevel();
    	GradeLvl finalGrade = engine.GetFinalGrade();
        Label[] gradeLabel = new Label[gradeList.size()];
        Label[] GradLabel = new Label[3];
        Label[] levelLabel = new Label[gradeList.size()];
        if(!gradLevel.equals("FAILED")){
        	GradLabel[0] = new Label("Congratulations!");
        	GradLabel[1] = new Label("You Graduated from: "+ gradLevel);
        	GradLabel[2] = new Label ("Overall Grade: " + finalGrade.getString());
        	
        }
        else {
        	GradLabel[0] = new Label("You Failed to Graduate");
        	GradLabel[1] = new Label("Maybe try again");
        	GradLabel[2] = new Label("");
        }

        for(int i=0;i<3;i++) {
        	GradLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 14));
        	GradLabel[i].setTextFill(primaryTextColor);
        	GradLabel[i].setTextAlignment(TextAlignment.CENTER);
        	setCenterX(boardPane, GradLabel[i]);
        	GradLabel[i].setLayoutY(boardHeight*(2*i+4)/24);

        }
        for(int i=0;i<gradeList.size();i++) {
        	levelLabel[i] = new Label("Level: "+(i+1));
        	gradeLabel[i] = new Label("Grade: "+gradeList.get(i).getString());
        	//levelLabel[i].setLayoutX(value);
        	gradeLabel[i].setLayoutY((boardHeight*(i+10))/24);
        	levelLabel[i].setLayoutY((boardHeight*(i+10))/24);

        	levelLabel[i].layoutXProperty().bind(boardPane.widthProperty().divide(6));
        	gradeLabel[i].layoutXProperty().bind(boardPane.widthProperty().multiply(5).divide(12));            //setCenterX(gradeLabel[i]);
            gradeLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
            
            levelLabel[i].layoutXProperty().bind(boardPane.widthProperty().divide(6));
            gradeLabel[i].layoutXProperty().bind(boardPane.widthProperty().multiply(5).divide(12));
            gradeLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 16));
            gradeLabel[i].setTextFill(secondaryTextColor);
            gradeLabel[i].setTextAlignment(TextAlignment.LEFT);
            gradeLabel[i].toFront();
            levelLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 16));
            levelLabel[i].setTextFill(secondaryTextColor);
            levelLabel[i].setTextAlignment(TextAlignment.LEFT);
            levelLabel[i].toFront();
        }
        boardPane.getChildren().addAll(gradeLabel);
        boardPane.getChildren().addAll(GradLabel);
        boardPane.getChildren().addAll(levelLabel);
    }


    /**
     * Method for converting total seconds into strings
     * @param secondDelta the difference between start time and end time in seconds
     * @return the time difference showing in a string format of "HH:MM:SS"
     * @pre none
     * @post returned string is not null
     */
    protected String convertTime(long secondDelta) {
        // this snippet taken from https://stackoverflow.com/questions/43892644
        long seconds = 1;
        long minutes = seconds * 60;
        long hours = minutes * 60;

        long elapsedHours = secondDelta / hours;
        secondDelta = secondDelta % hours;

        long elapsedMinutes = secondDelta / minutes;
        secondDelta = secondDelta % minutes;

        long elapsedSeconds = secondDelta;
        return String.format("%02d", elapsedHours) + ":" +
                String.format("%02d", elapsedMinutes) + ":" +
                String.format("%02d", elapsedSeconds);
    }
}
