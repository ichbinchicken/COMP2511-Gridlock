import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class gameController extends Controller {
	
	private static final int  GOALCAR =5;
	private static final int animTime = 0;
	private boolean isGameWon = false;
    protected GameEngine engine;
    private Main main;
    //protected double squareWidth;
    protected int nSquares;
    private final Color boardColor = Color.ORANGE;
    private ArrayList<Car> workload;
    private boolean animating=false;
    private boolean running;
    private Car goalCar;


	@FXML
	protected Pane boardPane;
    @FXML
    protected Label movesMade;

	public gameController(Stage s, GameEngine engine, Main main) {
        this.engine = engine;
        this.main = main;
        workload = new ArrayList<>();
	}
	

	
	
	
    protected void drawBoard(Pane p) {
    	Mode mode = engine.getMode();
        running = true;
        Rectangle[][] rec = new Rectangle[nSquares][nSquares];
        double squareWidth = p.getPrefWidth()/nSquares;

        for (int i = 0; i < nSquares; i ++) {
            for (int j = 0; j < nSquares; j ++) {
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * squareWidth);
                rec[i][j].setY(j * squareWidth);
                rec[i][j].setWidth(squareWidth);
                rec[i][j].setHeight(squareWidth);
                rec[i][j].setFill(boardColor);
                rec[i][j].setStroke(Color.BLUE);
                p.getChildren().add(rec[i][j]);
            }
        }
        drawBorder(boardPane);
        drawCars(boardPane);
        //boardPane.getChildren().add(curtain);
        //boardPane.getChildren().add(message);

        ///buttonNewGame.setDisable(false);
        //buttonPause.setDisable(false);
        //buttonPause.setText("Pause");
        //totalTime.setText(convertTime(totalSeconds));
        //curtain.setVisible(false);
        //message.setVisible(false);
        /*if(mode==Mode.STORY) {
            buttonHint.setDisable(true);
            buttonRestart.setDisable(true);
            buttonNewGame.setDisable(true);
        }
        else {
            buttonHint.setDisable(false);
            buttonRestart.setDisable(false);
            buttonNewGame.setDisable(false);
        }*/
    }

    public void MakeMove(int oldR, int oldC, int r, int c) {
    	if(!isGameWon) {
			if(engine.MakeMove(oldR, oldC, r,c)) {
	    		animating=true;	    		
				goalCar.CarMakeAnimatingMove(goalCar.getR(), engine.getBoardSize()-2, animTime);
                isGameWon=true;
				GameWon();
				//return true;
			}
			//return false;
    	}
    	movesMade.setText(Integer.toString(engine.getMoves()));

    }
    
    private void GameWon() {
        engine.GameWon(1);
    }
    
    
    public boolean GetAnimating() {
    	return animating;
    }
    
    public void AnimatingFin() {
    	animating=false;
    	/*if (isGameWon) {
    		
           // winCountDown.playFromStart();
        }*/
    }


    protected Car findCar(int r,int c) {
    	for(Car car: workload) {
    		if(r==car.getR() && c==car.getC()) {
    			return car;
    		}
    	}
    	return null;
    }
    
    public void AddCartoPane(Node c, Pane p) {
    	p.getChildren().remove(c);
        p.getChildren().add(c);
        c.toFront();
    }
    

    public void GetNewBoard() {
    	engine.getNewPuzzle();
        nSquares = engine.getBoardSize(); //this will be replaced dynamically.
        //this.squareWidth = boardPane.getPrefWidth()/nSquares;

        boardPane.getChildren().clear();
        //totalSeconds = engine.getTime();
        //currSeconds = totalSeconds;
        drawBoard(boardPane);
        //GameWon=false;
        animating=false;
        //countDown.playFromStart();
        movesMade.setText("0");
    }

    
	public int[] FindMoves(int r, int c) {
		return engine.FindMoves(r, c);
	}
	
    protected void drawBorder(Pane p) {
        double squareWidth = p.getPrefWidth()/nSquares;
        Line l= new Line();
        l.setEndY(nSquares*squareWidth);
        p.getChildren().add(l);
        l=new Line();
        l.setEndX(nSquares*squareWidth);
        p.getChildren().add(l);
        l=new Line();
        l.setStartY(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        p.getChildren().add(l);
        l=new Line();
        l.setStartX(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        p.getChildren().add(l);
    }

    protected void drawCars(Pane p) {
        double squareWidth = p.getPrefWidth()/nSquares;

        workload.clear();
    	workload = engine.GetCarList();
        for(Car c: workload) {
            c.frontEndCarConstructor(squareWidth, p.getBoundsInLocal(),this);
            Node car = c.getCar();
            p.getChildren().add(car);
            car.toFront();
            if (c.getCarType() == GOALCAR) {
                goalCar = c;
            }
        }

    }
    
    

}
