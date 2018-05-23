import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;


public class BoardController extends gameController {
	// public static final int  VERT=0;
	// public static final int HORIZ=1;
	protected static final int  GOALCAR =5;
    protected static final String[] GAME_OVER_MSGS = {"GAME OVER", "TRY AGAIN", "Your Moves: ", "Optimal Moves: "};
    protected static final String[] GAME_WON_MSGS = {"YOU WON", "Time used: ", "Your Moves: ", "Optimal Moves: ", "Your grade: "};
    protected static final int animTime = 500;

    protected GameEngine engine;

	@FXML
	protected Pane boardPane;
    @FXML
    protected Label totalTime;
    @FXML
    protected Label movesMade;
    @FXML
    protected Button buttonPause;
    @FXML
    protected Button buttonRestart;
    
    @FXML
    protected Button buttonNewGame;
    @FXML
    protected Button buttonHint;
    @FXML
    protected Button buttonMenu;

    protected Rectangle curtain;
    protected Label message;
    protected double squareWidth;
    protected int nSquares;
    protected Timeline countDown;
    protected Timeline winCountDown;
    protected int totalSeconds;  // The duration of game, should not changed *TOBY but needs to be reset for each new board
    protected int currSeconds;
    protected boolean running;
    protected ArrayList<Car> workload;
    protected boolean GameWon = false;
    protected boolean animating = false;
    protected Car goalCar;
    protected Main main;

    protected final Color boardColor = Color.ORANGE;

    public BoardController(Stage s, GameEngine engine, Main main) {
    	super(s,engine,main);
        this.engine = engine;
        this.main = main;
        totalSeconds = currSeconds = engine.getTime();
        workload = new ArrayList<>();

        running = true; // this is to check whether the game is paused. Initially, it's running.
    }

	@FXML
	void NewGame() {
    	if(animating==false) {
    		GetNewBoard();
    	}
	}
	
	@FXML
	void MainMenu() {
		main.ShowMenuScreen();
	}
    
    @FXML
    public void initialize() {
    	engine.getNewPuzzle();
        nSquares = engine.getBoardSize(); //this will be replaced dynamically.


        this.squareWidth = boardPane.getPrefWidth()/nSquares;

        // init curtain
        curtain = new Rectangle(boardPane.getPrefWidth(), boardPane.getPrefHeight(), boardColor);
        curtain.setX(0);
        curtain.setY(0);

        // init game over message
        message = new Label("");
        message.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 40));
        message.setTextFill(Color.WHITESMOKE);
        message.setTextAlignment(TextAlignment.CENTER);
        // place the label in the centre
        setCenterX(message);
        //message.layoutXProperty().bind(boardPane.widthProperty().subtract(message.widthProperty()).divide(2));
        //message.layoutYProperty().bind(boardPane.heightProperty().subtract(message.heightProperty()).divide(2));

        //GenNewPuzzle();

        // must call drawBoard after curtain and message are init'd, and after GenNewPuzzle
        drawBoard(boardPane);

        // init buttons
        buttonPause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (running) {
                    countDown.pause();
                    buttonHint.setDisable(true);
                    running = false;
                    buttonPause.setText("Resume");
                    curtain.setVisible(true);
                    curtain.toFront();
                } else {
                	if(engine.getMode()!=Mode.STORY)
                			buttonHint.setDisable(false);
                    countDown.play();
                    running = true;
                    buttonPause.setText("Pause");
                    curtain.setVisible(false);
                    //curtain.toFront();
                }
            }
        });
        


        buttonRestart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	//workload = engine.GetCarList();
            	if(animating==false) {
	            	engine.RestartPuzzle();
	                boardPane.getChildren().clear();
	                currSeconds = totalSeconds;
	                GameWon=false;
	                animating=false;
	                drawBoard(boardPane);
	                countDown.playFromStart();
	                movesMade.setText("0");
            	}

            }
        });
        

        buttonHint.setOnMouseClicked(new EventHandler <MouseEvent>() {
        	@Override
        	public void handle (MouseEvent event) {
        		if(animating==false && GameWon == false) {
	        		int[] arr =engine.getNextMove();
	        		//System.out.println(arr);
	        		Car car =findCar(arr[0], arr[1]);
	        		if(car!=null) {
	        			animating=true;
	        			car.CarMakeAnimatingMove(arr[2], arr[3], 2000);
	        		}
	        		else {
	        		}
        		}
        	}
        });
        




        // init timer
        countDown = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currSeconds--;
                totalTime.setText(convertTime(currSeconds));
                if (currSeconds <= 0) {
    				engine.GameLoss();
                	if(engine.getMode()==Mode.STORY) {
                        buttonNewGame.setDisable(true);
                	}
                	stopGame(GAME_OVER_MSGS[0]);
				

                }
            }
        }));
        countDown.setCycleCount(Animation.INDEFINITE);
        countDown.playFromStart(); // initialise the timer at the first time

        winCountDown = new Timeline(new KeyFrame(Duration.millis(animTime-43), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stopGame(GAME_WON_MSGS[0]);
            }
        }));
        winCountDown.setCycleCount(1);

    }



    // draw border for the board


    public boolean checkIntersection(Car car) {
        Bounds bounds = car.getCar().getBoundsInParent();
        //Bounds bounds = new BoundingBox(tmpBounds.getMinX()-0.5, tmpBounds.getMinY()-0.5,
        //        tmpBounds.getWidth()+1, tmpBounds.getHeight()+1);
        for (Car c: workload) {
            if (c == car) {
                continue;
            }
            Bounds cbounds = c.getCar().getBoundsInParent();
            if(cbounds.intersects(bounds)) {
                return true;
            }
        }
        return false;
    }

    protected void stopGame(String msg) {
        double boardHeight = boardPane.getPrefHeight();
        //double boardWidth = boardPane.getWidth();

        countDown.pause();
        buttonPause.setDisable(true);
        buttonHint.setDisable(true);
        buttonNewGame.setDisable(false);
        curtain.setVisible(true);
        curtain.toFront();
        message.setText(msg);
        if (engine.StoryModeEnd()==true) {
        	StoryModeEndScreen();
        }
        else if (GameWon) {
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
                setCenterX(details[i]);
                details[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
                details[i].setTextFill(Color.WHITESMOKE);
                details[i].setTextAlignment(TextAlignment.CENTER);
                details[i].toFront();
            }
            message.setVisible(true);
            message.toFront();


        } else {
            message.setLayoutY(boardHeight*3/8);
            Label prompt = new Label("TRY AGAIN");
            Label[] details = new Label[2];

            prompt.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 30));
            prompt.setTextFill(Color.WHITESMOKE);
            prompt.setTextAlignment(TextAlignment.CENTER);
            prompt.setLayoutY(boardHeight*16/24);
            details[0] = new Label(GAME_OVER_MSGS[2]+engine.getMoves());
            details[0].setLayoutY(boardHeight/2);
            int minMoves = engine.getMinMoves();
            details[1] = new Label(GAME_OVER_MSGS[3]+minMoves);
            details[1].setLayoutY(boardHeight*14/24);
            boardPane.getChildren().addAll(details);

            for(int i = 0; i < 2; i++) {
                setCenterX(details[i]);
                details[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
                details[i].setTextFill(Color.WHITESMOKE);
                details[i].setTextAlignment(TextAlignment.CENTER);
                details[i].toFront();
            }
            setCenterX(prompt);
            boardPane.getChildren().add(prompt);
            prompt.toFront();
            message.setVisible(true);
            message.toFront();


        }
    }
    
    
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
        if(gradLevel!="FAILED") {
        	GradLabel[0] = new Label("Congratulations!");
        	GradLabel[1] = new Label("You Graduated from: "+ gradLevel);
        	GradLabel[2] = new Label ("With an Overall Grade of: " + finalGrade.getString());
        	
        }
        else {
        	GradLabel[0] = new Label("You Failed to Graduate");
        	GradLabel[1] = new Label("Maybe he should try again");
        	GradLabel[2] = new Label("");
        }

        for(int i=0;i<3;i++) {
        	GradLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 25));
        	GradLabel[i].setTextFill(Color.WHITESMOKE);
        	GradLabel[i].setTextAlignment(TextAlignment.CENTER);
        	setCenterX(GradLabel[i]);
        	GradLabel[i].setLayoutY(boardHeight*(2*i)/24);

        }
        for(int i=0;i<gradeList.size();i++) {
        	levelLabel[i] = new Label("Level: "+(i+1));
        	gradeLabel[i] = new Label("Grade: "+gradeList.get(i).getString());
        	//levelLabel[i].setLayoutX(value);
        	gradeLabel[i].setLayoutY((boardHeight*(i+6))/24);
        	levelLabel[i].setLayoutY((boardHeight*(i+6))/24);

        	levelLabel[i].setLayoutX(boardWidth/4);
        	gradeLabel[i].setLayoutX(boardWidth/2);

            //setCenterX(gradeLabel[i]);
            gradeLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
            gradeLabel[i].setTextFill(Color.WHITESMOKE);
            gradeLabel[i].setTextAlignment(TextAlignment.LEFT);
            gradeLabel[i].toFront();
            levelLabel[i].setFont(new Font("DejaVu Sans Mono for Powerline Bold", 20));
            levelLabel[i].setTextFill(Color.WHITESMOKE);
            levelLabel[i].setTextAlignment(TextAlignment.LEFT);
            levelLabel[i].toFront();


        }
        boardPane.getChildren().addAll(gradeLabel);
        boardPane.getChildren().addAll(GradLabel);
        boardPane.getChildren().addAll(levelLabel);
        message.setVisible(false);
        //message.toFront();


        //details[0] = new Label(GAME_WON_MSGS[1]+timeElapsed);


    }
    
    

    /*
    public Bounds getFreeZone(Car car) {
        Bounds bounds = car.getCar().getBoundsInParent();
        double minX = bounds.getMinX();
        double minY = bounds.getMinY();
        double maxX = bounds.getMaxX();
        double maxY = bounds.getMaxY();
        for (Car c: workload) {
            if (c == car) {
                continue;
            }
            Bounds cbounds = c.getCar().getBoundsInParent();
            if (cbounds.getMaxX() >= minX &&) {
                minX = cbounds.getMaxX();
            }

            if(cbounds.intersects(bounds)) {
            }
        }

    }
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

    
    

    

    /*
    protected Car findGoalCar() {
    	for(Car car:workload) {
    		if(car.getCarType()==GOALCAR) {
    			return car;
    		}
    	}
    	return null;
    }
    */
    

	
	
    
    protected void setCenterX(Label label) {
        label.layoutXProperty().bind(boardPane.widthProperty().subtract(label.widthProperty()).divide(2));
    }

    
   /* private void GenNewPuzzle(){
    	//puzzle = engine.getNewPuzzle();
        //puzzle = new Puzzle(6,6);
        //puzzle.printBoard();
    }*/
}
