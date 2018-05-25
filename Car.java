import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.lang.ref.WeakReference;
import java.util.concurrent.ThreadLocalRandom;

/**
 * GradLock
 * @author Michael Hamilton and Ziming Zheng
 * This class represents a car on the board
 */
public class Car {
    private Image IMAGE;
    private ImageView carNode;
	private static final int  GOALCAR=5;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;

	private int r;
    private int c;
	
	private double x;
	private double y;
	private int length;
	private double squareLength;
	private int type;
	private boolean dragging;
	private double mousex;
	private double mousey;
	private WeakReference<GameController> boardReference;
	private int initR;
	private int initC;
	private int[] moveSpace = {0,0,0,0};
	private Bounds moveBounds;
	private double initX,initY;
	private boolean moveable;

	/**
	 * Constructor of Car class
	 * @param r the row coordinate in the backend
	 * @param c the column coordinate in the backend
	 * @param type the type of car
	 * @param length the lengh of car
	 * @pre none
	 * @post none
	 */
	public Car(int r, int c, int type, int length) {
		this.r=r; // backend coordinate
		this.c=c; // backend coordinate
		initR=r;
		initC=c;
		this.type=type;
		this.length = length;  // backend length
	}

	/**
	 * Putting images and setting frontend coordinate
	 * @param squareLength the length of tile on the board
	 * @param b the bound required by javafx
	 * @param bcontroller the controller to connect frontend and backend
	 * @param Moveable boolean to check whether a car is movable or not
	 * @pre b != null && bcontroller != null
	 * @post carNode != null
	 */
	public void frontEndCarConstructor(double squareLength, Bounds b, GameController bcontroller, boolean Moveable) {
		//this.engine = engine;
		this.moveable=Moveable;
		this.squareLength = squareLength;
		//this.boardController = bcontroller;
		this.boardReference = new WeakReference<GameController>(bcontroller);
        //Generate the Image
	    //max=squareLength*6;
        initX = c*squareLength+1;
        initY = r*squareLength+1;
	    //bounds = new BoundingBox(b.getMinX()-initX, b.getMinY()-initY, b.getWidth(), b.getHeight());

		int randNum = ThreadLocalRandom.current().nextInt(1, 4+1);
		//System.out.println("randnum is "+randNum);

	    if(type==VERCAR || type == VERTRUCK) {
	    	//carNode.setWidth(squareLength);
	    	//carNode.setHeight(squareLength*length);
			IMAGE = new Image("images/"+length+" high-0"+randNum+".png");
			carNode = new ImageView(IMAGE);
			carNode.setFitHeight((squareLength)*length-2);
		    carNode.setFitWidth(squareLength-2);

	    } else {
			if(type==GOALCAR) {
				IMAGE = new Image("images/2 Wide-you.png");
			} else {
				IMAGE = new Image("images/"+length+" Wide-0"+randNum+".png");
				//IMAGE = new Image("images/"+length+" Wide-03.png");
			}
			carNode = new ImageView(IMAGE);
		    carNode.setFitHeight(squareLength-2);
		    carNode.setFitWidth(squareLength*length-2);

	    }
		carNode.setX(initX);
		carNode.setY(initY);
	    addMouseEvent();

	}

	/**
	 * getter method for carNode
	 * @return the carNode
	 * @pre none
	 * @post none
	 */
	public Node getCar() {
		return carNode;
	}

	/**
	 * convert to backend coordinate(row/column) given the local board coordinate
	 * @param coord the local board coordinate
	 * @return the number of row or column
	 * @pre none
	 * @post none
	 */
	private int CoordtoN(double coord) {
		//System.out.println(coord + "|");
		return (int) Math.round(coord/squareLength);
	}

	/**
	 * convert to local board coordinate given the number of squares for a side
	 * @param n the number of squares
	 * @return the local board coordinate
	 * @pre none
	 * @post none
	 */
	private double NtoCoord(int n) {
		return n*squareLength;
	}

	/**
	 * Mouse handler for all sorts of actions to the car on the board
	 * @pre none
	 * @post none
	 */
	private void addMouseEvent() {
        carNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(!moveable) {
            		return;
            	}
                GameController boardController = boardReference.get();
                if (boardController != null && !boardController.GetAnimating()) {
                    mousex = event.getSceneX();
                    mousey = event.getSceneY();

                    x = carNode.getLayoutX();
                    y = carNode.getLayoutY();
                    //x = getCoordInParent(X);
                    //y = getCoordInParent(Y);
                    //System.out.println("Car X: "+x+" Y: "+y);

                    carNode.toFront();
                    
                    moveSpace = boardController.FindMoves(r, c);
                    int left = moveSpace[0];
                    int up = moveSpace[1];
                    int down = moveSpace[3];
                    int right = moveSpace[2];
                    moveBounds = new BoundingBox(squareLength*(c-left)+1-initX,squareLength*(r-up)+1-initY, 
                    		squareLength*(length+right+left), squareLength*(length+up+down));
                }
                

            }
        });

        carNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(!moveable) {
            		return;
            	}
            	GameController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating() && moveBounds!=null) {

                    dragging = true;
                    if (type == VERCAR || type == VERTRUCK) {
                    	//Check spaces up and down
                            double offsetY = event.getSceneY() - mousey;
                            event.getY();

                            double newY = y + offsetY;
                            y = setCoord(newY, moveBounds.getMinY(), moveBounds.getMaxY() - length * squareLength);
                            //carNode.relocate(x, y);

                            carNode.setLayoutY(y);
                        //}
                    } else {

                            double offsetX = event.getSceneX() - mousex;

                            double newX = x + offsetX;
                            //x = setCoord(newX, bounds.getMinX(), bounds.getMaxX() - length * squareLength);
                            x = setCoord(newX, moveBounds.getMinX(), moveBounds.getMaxX() - length * squareLength);

                            carNode.setLayoutX(x);
                    }
                    mousey = event.getSceneY();
                    mousex = event.getSceneX();

                    event.consume();
                }

            }
        });

        carNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(!moveable) {
            		return;
            	}
            	GameController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating()) {
                    dragging = false;
                }
            }
        });

        carNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(!moveable) {
            		return;
            	}

            	GameController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating() && dragging == true) {
                    x = carNode.getLayoutX();
                    y = carNode.getLayoutY();
                    //x = getCoordInParent(X);
                    //y = getCoordInParent(Y);

                    double scaledY = Math.round(y / squareLength) * squareLength;
                    double scaledX = Math.round(x / squareLength) * squareLength;

                    //carNode.relocate(scaledX, scaledY);

                    carNode.setLayoutX(scaledX);
                    carNode.setLayoutY(scaledY);
                    event.consume();
                    int oldR = r;
                    int oldC = c;
                    c = CoordtoN(carNode.getX() + carNode.getLayoutX());
                    r = CoordtoN(carNode.getY() + carNode.getLayoutY());
                    if (r != oldR || c != oldC) {
                        //Update Coordinates - tell game engine we have moved
                        boardController.MakeMove(oldR, oldC, r, c);

                    }
                    //c=CoordtoN(carNode.getY()+ carNode.getLayoutY());
                    //System.out.println("Coords r,c" + r+ ","+ c );
                }
            }
        });

    }

	/**
	 * getter method for the car type
	 * @return the type of car
	 */
	public int getCarType() {
		return type;
	}

	/**
	 * Make Animating Move and update the backend
	 * @param newR the new row number
	 * @param newC the new column number
	 * @param animTime the time of animation
	 * @pre none
	 * @post none
	 */
	public void CarMakeAnimatingMove(int newR, int newC, int animTime) {
		double xshift = NtoCoord(newC-c);
		double yshift = NtoCoord(newR-r);
		Bounds cBnd = carNode.getBoundsInLocal();
		double xstart = NtoCoord(initC)+cBnd.getWidth()/2+1; //I dont know why NtoCoord(c) shouldnt be added
		double ystart = NtoCoord(initR)+cBnd.getHeight()/2+1;
		//carNode.relocate(NtoCoord(c), NtoCoord(r));
		Path p = new Path();
		p.getElements().add(new MoveTo(xstart,ystart));
		p.getElements().add(new LineTo(xstart+xshift, ystart+yshift));
		PathTransition pt = new PathTransition(Duration.millis(animTime),p);
		pt.setNode(carNode);
		pt.setPath(p);
		pt.setCycleCount(1);
		
		pt.play();
		int oldR=r;
		int oldC=c;
		r=newR;
		c=newC;
		pt.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				GameController boardController = boardReference.get();
				if (boardController != null) {
					carNode.setTranslateX(0); //Must reset these to 0 (remove effect of animation)
					carNode.setTranslateY(0);
					carNode.relocate(NtoCoord(c)+1, NtoCoord(r)+1); //Must set coordinates again to cancel translate
                    boardController.AnimatingFin();
					boardController.MakeMove(oldR, oldC, r, c);
                }
			}

		});

	}
	
	
	/**
	 * 
	 * Make Animating Move without updating backend
	 * @param newR the new row number
	 * @param newC the new column number
	 * @param animTime the animation time
	 * @pre none
	 * @post none
	 */
	public void makeSetAnimateMove(int newR, int newC, int animTime) {
		double xshift = NtoCoord(newC-c);
		double yshift = NtoCoord(newR-r);
		Bounds cBnd = carNode.getBoundsInLocal();
		double xstart = NtoCoord(initC)+cBnd.getWidth()/2+1; //I dont know why NtoCoord(c) shouldnt be added
		double ystart = NtoCoord(initR)+cBnd.getHeight()/2+1;
		//carNode.relocate(NtoCoord(c), NtoCoord(r));
		Path p = new Path();
		p.getElements().add(new MoveTo(xstart,ystart));
		p.getElements().add(new LineTo(xstart+xshift, ystart+yshift));
		PathTransition pt = new PathTransition(Duration.millis(animTime),p);
		pt.setNode(carNode);
		pt.setPath(p);
		pt.setCycleCount(1);
		
		pt.play();
		//int oldR=r;
		//int oldC=c;
		r=newR;
		c=newC;
		pt.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				GameController boardController = boardReference.get();
				if (boardController != null) {
					carNode.setTranslateX(0); //Must reset these to 0 (remove effect of animation)
					carNode.setTranslateY(0);
					carNode.relocate(NtoCoord(c)+1, NtoCoord(r)+1); //Must set coordinates again to cancel translate
                    //boardController.AnimatingFin();
					//boardController.MakeMove(oldR, oldC, r, c);
                }
			}

		});

	}

	/**
	 * getter method for row number in backend
	 * @return the row number
	 * @pre none
	 * @post none
	 */
	public int getR() {
		return r;
	}

	/**
	 * getter method for column in backend
	 * @return the column number
	 * @pre none
	 * @post none
	 */
	public int getC() {
		return c;
	}

	/**
	 * bounding the coordinate within the board
	 * @param newCoord the new frontend coordinate
	 * @param minCoord the minimum frontend coordinate
	 * @param maxCoord the maximum frontend coordinate
	 * @return the updated frontend coordinate
	 */
	private double setCoord(double newCoord, double minCoord, double maxCoord) {
        if (newCoord > maxCoord) {
            return maxCoord;
        }
        if (newCoord < minCoord) {
            return minCoord;
        }
        return newCoord;
    }

	/**
	 * setter methods for moveable
	 * @param move the boolean for moveable
	 */
	public void setMoveable(boolean move) {
		moveable = move;
	}
}