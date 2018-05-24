
import java.util.ArrayList;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NetworkController extends gameController {
    protected static final int networkUpdateTime = 2000;
    protected static final int oppAnimTime = 100;


	@FXML
	private Pane boardPane;
	@FXML
	private Pane NboardPane;
	@FXML
	private Label NmovesMade;
	
    private ArrayList<Car> nworkload = new ArrayList<>();
    private Car theirgoalCar;
    private Timeline timeline;
    private Rectangle Ncurtain;
	private Timeline scounter;
	private boolean pregame =true;
	private int Timecounter = 4;
	private Label message;
	public NetworkController(Stage s, GameEngine engine, Main main) {
		super(s,engine,main);
    }

	
    public void initialize() {
    	super.initialize();
    	message = new Label("");
        message.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 40));
        message.setTextFill(Color.WHITESMOKE);
        setCenterX(message);
    	Ncurtain = new Rectangle(NboardPane.getPrefWidth(), NboardPane.getPrefHeight(), boardColor);
        Ncurtain.setX(0);
        Ncurtain.setY(0);

        
    	timeline = new Timeline(new KeyFrame(Duration.millis(networkUpdateTime), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Checking Network");
            	if(engine.NetworkMoveWaiting()) {
            		int[] arr = engine.NetworkGetMove();
            		MakeOpponentMove(arr);
            		NmovesMade.setText(Integer.toString(engine.getOppMoves()));

            		if(engine.NetworkOpponentWon()) {
            			theirgoalCar.CarMakeAnimatingMove(theirgoalCar.getR(), engine.getBoardSize()-2, animTime);
            			GameWon();
            		}
            	}
    	}}));
    	
    	
    	timeline.setCycleCount(Animation.INDEFINITE);
    	
    	
    	scounter = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(pregame) {
            		Timecounter--;

            		message.setText(Integer.toString(Timecounter));
            		if(Timecounter<=0) {
            			NetgameStart();
            			scounter.stop();
            		}
            	}
            	
    	}}));
    	scounter.setCycleCount(Animation.INDEFINITE);


    	//engine.getNewPuzzle();
    	//nworkload = drawBoard(NboardPane, nworkload, false);
    	
        //nSquares = engine.getBoardSize(); //this will be replaced dynamically.

        //this.squareWidth = boardPane.getPrefWidth()/nSquares;

    }
    	
    protected void NetgameStart() {
    	timeline.play();
    	curtainHide();
    	NcurtainHide();
    	message.setVisible(false);
    	message.toBack();
		
	}


	private void MakeOpponentMove(int [] arr) {
		Car car =findOpponentCar(arr[0], arr[1]);
		car.makeSetAnimateMove(arr[2], arr[3], oppAnimTime);

    }
    
   /* AnimatingFin(){}
    
    @Override
    public void MakeMove(int oldR, int oldC, int r, int c) {
    	if(!isGameWon) {
			if(engine.MakeMove(oldR, oldC, r,c)) {
	    		animating=true;	    		
				goalCar.CarMakeAnimatingMove(goalCar.getR(), engine.getBoardSize()-2, animTime);
	            isGameWon=true;
				GameWon();
			}

	}*/
    @Override
    public void GetNewBoard() {
    	super.GetNewBoard();
        boardPane.getChildren().add(message);
        
    	NmovesMade.setText("0");
        nworkload = drawBoard(NboardPane,nworkload,false);
        curtainShow();

        for(Car c: nworkload) {
        	if(c.getCarType()==GOALCAR) {
        		theirgoalCar=c;
        	}
        }
        message.setVisible(true);
        message.toFront();
        NboardPane.getChildren().add(Ncurtain);

        NcurtainShow();

        scounter.playFromStart();
    }
    
    private void NcurtainShow() {
    	Ncurtain.setVisible(true);
    	Ncurtain.toFront();
    }
    
    private void NcurtainHide() {
    	Ncurtain.setVisible(false);
    	Ncurtain.toBack();
    }
    

    
    @FXML
    private void RequestNewGame() {
    	
    }
    
    protected Car findOpponentCar(int r,int c){
    	for(Car car: nworkload) {
    		if(r==car.getR() && c==car.getC()) {
    			return car;
    		}
    	}
    	return null;
    }
}
