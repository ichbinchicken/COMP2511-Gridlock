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

public class GameController extends Controller {
	
	protected static final int  GOALCAR =5;
    protected static final int animTime = 500;
    protected static final Color boardColor = Color.rgb(244, 244, 244);
    protected static final Color lineColor = Color.rgb(154, 154, 154);
    protected static final Color primaryTextColor = Color.rgb(229, 105, 96);
    protected static final Color secondaryTextColor = Color.rgb(77, 77, 77);

	protected boolean isGameWon = false;
    protected GameEngine engine;
    protected Main main;
    protected double squareWidth;
    protected int nSquares;
    protected ArrayList<Car> workload;
    private Car goalCar;
    protected boolean running;
    protected boolean animating = false;
    protected Rectangle curtain;


	@FXML
	protected Pane boardPane;
    @FXML
    protected Label movesMade;
    @FXML
    protected Button buttonNewGame;

	public GameController(Stage s, GameEngine engine, Main main) {
        this.engine = engine;
        this.main = main;
        workload = new ArrayList<Car>();
	}
	

	@FXML
	public void initialize() {
    	curtain = new Rectangle(boardPane.getPrefWidth(), boardPane.getPrefHeight(), boardColor);
        curtain.setX(0);
        curtain.setY(0);
        boardPane.getChildren().add(curtain);

	}
	
	protected void curtainShow() {
		curtain.setVisible(true);
		curtain.toFront();
	}
	
	protected void curtainHide() {
		curtain.setVisible(false);
		curtain.toBack();
	}

	@FXML
	void MainMenuAction() {
		workload.clear();
		boardPane.getChildren().clear();
		main.ShowMenuScreen();
	}
	
	@FXML
	void NewGameAction() {
	    GetNewBoard();
	}
	
    protected ArrayList<Car> drawBoard(Pane p, ArrayList<Car>  wkload, boolean move) {
        //running = true;
        p.getChildren().clear();

        wkload.clear();
        running=true;
        
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
                rec[i][j].setStroke(lineColor);
                p.getChildren().add(rec[i][j]);
            }
        }

        drawBorder(p);

        return drawCars(p, wkload, move);
    }

    public void MakeMove(int oldR, int oldC, int r, int c) {
    	if(!isGameWon) {
			if(engine.MakeMove(oldR, oldC, r,c)) {
	    		animating=true;	    		
				goalCar.CarMakeAnimatingMove(goalCar.getR(), engine.getBoardSize()-2, animTime);
	            isGameWon=true;
				GameWon();


			}
    	}
    	movesMade.setText(Integer.toString(engine.getMoves()));
    }
    
    
    protected void GameWon() {
        engine.GameWon(1);
    }
    
    
    public boolean GetAnimating() {
    	return animating;
    }
    
    public void AnimatingFin() {
    	animating=false;
    	if (isGameWon) {
    		DisplayWinScreen();

            //winCountDown.playFromStart();
        }
    }
    
    public void DisplayWinScreen() {

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

        
        workload = drawBoard(boardPane, workload,true);
        if(!boardPane.getChildren().contains(curtain)) {
        	boardPane.getChildren().add(curtain);
        	curtainHide();
        }
        isGameWon=false;
        animating=false;
        running=true;
        movesMade.setText("0");
    }

    
	public int[] FindMoves(int r, int c) {
		return engine.FindMoves(r, c);
	}
	
    protected void drawBorder(Pane p) {
        double squareWidth = p.getPrefWidth()/nSquares;
        Line l= new Line();
        l.setEndY(nSquares*squareWidth);
        l.setStroke(secondaryTextColor);
        p.getChildren().add(l);
        l=new Line();
        l.setEndX(nSquares*squareWidth);
        l.setStroke(secondaryTextColor);
        p.getChildren().add(l);
        l=new Line();
        l.setStartY(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        l.setStroke(secondaryTextColor);
        p.getChildren().add(l);
        l=new Line();
        l.setStartX(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        l.setStroke(secondaryTextColor);
        p.getChildren().add(l);
    }

    protected ArrayList<Car> drawCars(Pane p, ArrayList<Car> wkload, boolean move) {
        
    	double squareWidth = p.getPrefWidth()/nSquares;
        wkload.clear();
        wkload = engine.GetCarList();
        for(Car c: wkload) {
            c.frontEndCarConstructor(squareWidth, p.getBoundsInLocal(),this,move);
            Node car = c.getCar();
            p.getChildren().add(car);
            car.toFront();
            if (c.getCarType() == GOALCAR && p==boardPane) {
                goalCar = c;
            }
        }
        return wkload;

    }
    
    
    protected void setCenterX(Pane pane, Label label) {
        label.layoutXProperty().bind(pane.widthProperty().subtract(label.widthProperty()).divide(2));
    }

    protected void setCenterY(Pane pane, Label label) {
        label.layoutYProperty().bind(pane.heightProperty().subtract(label.heightProperty()).divide(2));
    }


}
