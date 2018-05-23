
import java.util.ArrayList;
import java.util.Timer;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class NetworkController extends gameController {
    protected static final int networkUpdateTime = 500;
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
    
	
	public NetworkController(Stage s, GameEngine engine, Main main) {
		super(s,engine,main);
    }

	
    public void initialize() {
    	super.initialize();
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
    	//engine.getNewPuzzle();
    	//nworkload = drawBoard(NboardPane, nworkload, false);
    	
        //nSquares = engine.getBoardSize(); //this will be replaced dynamically.

        //this.squareWidth = boardPane.getPrefWidth()/nSquares;

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

    public void GetNewBoard() {
    	super.GetNewBoard();
    	timeline.play();
    	NmovesMade.setText("0");
        nworkload = drawBoard(NboardPane,nworkload,false);
        for(Car c: nworkload) {
        	if(c.getCarType()==GOALCAR) {
        		theirgoalCar=c;
        	}
        }
        
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
