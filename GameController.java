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
/**
 * GradLock Project
 * @author Michael Hamilton and Ziming Zheng
 * This class represents the parent controller of game
 */
public abstract class GameController extends Controller {
	
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

    /**
     * Contructor of GameController
     * @param s the stage required by javafx
     * @param engine the "interface" connecting frontend and backend
     * @param main the game launcher for switching screens
     * @pre s != null && engine != null && main != null
     * @post workload != null
     */
	public GameController(Stage s, GameEngine engine, Main main) {
        this.engine = engine;
        this.main = main;
        workload = new ArrayList<Car>();
	}

    /**
     * Initialize the settings of all game screens
     * @pre none
     * @post none
     */
	@FXML
	public void initialize() {
    	curtain = new Rectangle(boardPane.getPrefWidth(), boardPane.getPrefHeight(), boardColor);
        curtain.setX(0);
        curtain.setY(0);
        boardPane.getChildren().add(curtain);

	}

    /**
     * show the curtain to let player not see the board
     * @pre none
     * @post none
     */
	protected void curtainShow() {
		curtain.setVisible(true);
		curtain.toFront();
	}

    /**
     * hide the curtain to let player see the board
     * @pre none
     * @post none
     */
	protected void curtainHide() {
		curtain.setVisible(false);
		curtain.toBack();
	}

    /**
     * handle action when "Menu" button is clicked
     */
	@FXML
	void MainMenuAction() {
		workload.clear();
		boardPane.getChildren().clear();
		main.ShowMenuScreen();
	}

    /**
     * handle action when "new game" button is clicked
     */
	@FXML
	void NewGameAction() {
	    GetNewBoard();
	}

    /**
     * draw the frontend board for the game
     * @param p the pane as placeholder required by javafx
     * @param wkload all cars to be drawn
     * @param move boolean for move or not
     * @return array of cars
     * @pre p != null && wkload != null
     * @post the returned list is not null
     */
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

    /**
     * Make moves
     * @param oldR old row number
     * @param oldC old column number
     * @param r new row number
     * @param c new column number
     * @pre none
     * @post none
     */
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

    /**
     * set the game won
     */
    protected void GameWon() {
        engine.GameWon(1);
    }

    /**
     * get the boolean of animating or nor
     * @return the boolean of animating
     */
    public boolean GetAnimating() {
    	return animating;
    }

    /**
     * let animating finished
     */
    public void AnimatingFin() {
    	animating=false;
    	if (isGameWon) {
    		DisplayWinScreen();

            //winCountDown.playFromStart();
        }
    }

    public void DisplayWinScreen() {
    }

    /**
     * find the car given the row and column
     * @param r the row
     * @param c the column
     * @return the car
     */
    protected Car findCar(int r,int c) {
    	for(Car car: workload) {
    		if(r==car.getR() && c==car.getC()) {
    			return car;
    		}
    	}
    	return null;
    }
    
    /*public void AddCartoPane(Node c, Pane p) {
    	p.getChildren().remove(c);
        p.getChildren().add(c);
        c.toFront();
    }*/

    /**
     * generate a new board for the game
     */
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

    /**
     * find the moves give row and column
     * @param r the row number
     * @param c the column number
     * @return the moves
     * @pre none
     * @post none
     */
	public int[] FindMoves(int r, int c) {
		return engine.FindMoves(r, c);
	}

    /**
     * draw the boarder of the board
     * @param p pane required by javafx
     * @pre p != null
     * @post none
     */
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

    /**
     * draw the frontend cars for the game
     * @param p pane required by javafx
     * @param wkload all the cars to be drawn
     * @param move the boolean for move or not
     * @return the array of frontend cars
     * @pre wkload != null && p != null
     * @post wkload != null
     */
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

    /**
     * Set x coordinate in the centre
     * @param pane pane required by javafx
     * @param label the label
     * @pre label != null
     * @post null
     */
    protected void setCenterX(Pane pane, Label label) {
        label.layoutXProperty().bind(pane.widthProperty().subtract(label.widthProperty()).divide(2));
    }

    /**
     * Set y coordinate in the centre
     * @param pane pane required by javafx
     * @param label the label
     * @pre label != null
     * @post null
     */
    protected void setCenterY(Pane pane, Label label) {
        label.layoutYProperty().bind(pane.heightProperty().subtract(label.heightProperty()).divide(2));
    }


}
