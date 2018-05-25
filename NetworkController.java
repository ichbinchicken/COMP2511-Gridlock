
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

public class NetworkController extends GameController {
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
	private int Timecounter = 3;
	private Label message;
	private Label Nmessage;
	private boolean GameOver = false; //=True when someone wins

	public NetworkController(Stage s, GameEngine engine, Main main) {
		super(s,engine,main);
    }

	
    public void initialize() {
    	super.initialize();
    	message = new Label("");
        message.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 60));
        message.setTextFill(Color.BLACK);
        setCenterX(boardPane, message);
        setCenterY(boardPane, message);
    	Ncurtain = new Rectangle(NboardPane.getPrefWidth(), NboardPane.getPrefHeight(), boardColor);
        Ncurtain.setX(0);
        Ncurtain.setY(0);
		Nmessage = new Label("");
		Nmessage.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 60*NboardPane.getPrefWidth()/boardPane.getPrefWidth()));
		Nmessage.setTextFill(Color.BLACK);
		setCenterX(NboardPane, Nmessage);
		setCenterY(NboardPane, Nmessage);

        
    	timeline = new Timeline(new KeyFrame(Duration.millis(networkUpdateTime), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	System.out.println("Checking Network");
            	if(engine.NetworkMoveWaiting()) {
            		int[] arr = engine.NetworkGetMove();
            		MakeOpponentMove(arr);
            		NmovesMade.setText(Integer.toString(engine.getOppMoves()));

            		if(engine.NetworkOpponentWon()) {  // opponent won
            			theirgoalCar.makeSetAnimateMove(theirgoalCar.getR(), engine.getBoardSize()-2, animTime);
            			GameWon();
            			GameOver=true;
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
					Nmessage.setText(Integer.toString(Timecounter));
            		if(Timecounter<=0) {
            			NetgameStart();
            			scounter.stop();
            		}
            	}
            	
    	}}));
    	scounter.setCycleCount(Animation.INDEFINITE);


    }
    
    
    @Override
    protected void GameWon(){
    	timeline.stop();
    	for(Car c: workload) {
    		c.setMoveable(false);
    	}
    	//curtainShow();
    	//NcurtainShow();
    	if (isGameWon) {
    		message.setText("YOU WON");
    		Nmessage.setText("AI LOST");
		} else {
    		message.setText("YOU LOST");
    		Nmessage.setText("AI WON");
		}
		message.setVisible(true);
		message.toFront();
		Nmessage.setVisible(true);
		Nmessage.toFront();

    }
    	
    protected void NetgameStart() {
    	timeline.play();
    	curtainHide();
    	NcurtainHide();
    	message.setVisible(false);
    	message.toBack();
		Nmessage.setVisible(false);
		Nmessage.toBack();

	}


	private void MakeOpponentMove(int [] arr) {
		Car car =findOpponentCar(arr[0], arr[1]);
		car.makeSetAnimateMove(arr[2], arr[3], oppAnimTime);

    }
    
    //AnimatingFin(){}
    
    @Override
    public void MakeMove(int oldR, int oldC, int r, int c) {
    	super.MakeMove(oldR, oldC, r, c);
    	if(isGameWon) {
    		// you won

    		GameOver=true;
    		GameWon();
    	}
	}
    @Override
    public void GetNewBoard() {
    	super.GetNewBoard();
    	Timecounter = 3;
    	
        boardPane.getChildren().add(message);
		message.setText(Integer.toString(Timecounter));
    	//workload = drawBoard(boardPane, workload, true);
        nworkload = drawBoard(NboardPane,nworkload,false);
        curtainShow();

        for(Car c: nworkload) {
        	if(c.getCarType()==GOALCAR) {
        		theirgoalCar=c;
        	}
        }
        message.setVisible(true);
        message.toFront();

		Nmessage.setText(Integer.toString(Timecounter));
		NmovesMade.setText("0");
        NboardPane.getChildren().add(Ncurtain);
		NboardPane.getChildren().add(Nmessage);
        NcurtainShow();
		Nmessage.setVisible(true);
		Nmessage.toFront();

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
    	GetNewBoard();
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
