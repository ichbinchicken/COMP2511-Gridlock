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

public class Car {
    private Image IMAGE;
    private ImageView carNode;
	private static final int  GOALCAR=5;
	//private static final int  HORCAR=4;
	//private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;
	//private static final int X = 0;
    //private static final int Y = 0;

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
	//private BoardController boardController;
	private WeakReference<gameController> boardReference;
	//private Bounds bounds;
	private int initR;
	private int initC;
	private int[] moveSpace = {0,0,0,0};
	private Bounds moveBounds;
	double initX,initY;
	//private GameEngine engine;
	//private double max;


	public Car(int r, int c, int type, int length) {
		this.r=r; // backend coordinate
		this.c=c; // backend coordinate
		initR=r;
		initC=c;
		this.type=type;
		this.length = length;  // backend length
	}

	//
	public void frontEndCarConstructor(double squareLength, Bounds b, gameController bcontroller) {
		//this.engine = engine;
		this.squareLength = squareLength;
		//this.boardController = bcontroller;
		this.boardReference = new WeakReference<>(bcontroller);
        //Generate the Image
		if(type!=GOALCAR) {
		IMAGE = new Image("car.jpg");
		}
		if(type==GOALCAR) {
		IMAGE = new Image("goalcar.png");
		}
		carNode = new ImageView(IMAGE);
	    //max=squareLength*6;
        initX = c*squareLength+1;
        initY = r*squareLength+1;
	    carNode.setX(initX);
	    carNode.setY(initY);
	    //bounds = new BoundingBox(b.getMinX()-initX, b.getMinY()-initY, b.getWidth(), b.getHeight());
	    
	    if(type==VERCAR || type == VERTRUCK) {
	    	//carNode.setWidth(squareLength);
	    	//carNode.setHeight(squareLength*length);
		    carNode.setFitHeight((squareLength)*length-2);
		    carNode.setFitWidth(squareLength-2);
	    }
	    else {
 	
		    carNode.setFitHeight(squareLength-2);
		    carNode.setFitWidth(squareLength*length-2);

	    }
	    addMouseEvent();

	}
	
	
	public Node getCar() {
		return carNode;
	}


	/*private double mRound(double value, double factor) {
		return Math.round(value/factor) * factor;
	}*/
	

	private int CoordtoN(double coord) {
		//System.out.println(coord + "|");
		return (int) Math.round(coord/squareLength);
	}
	
	private double NtoCoord(int n) {
		return n*squareLength;
	}

	private void addMouseEvent() {
        carNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                gameController boardController = boardReference.get();
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

            	gameController boardController = boardReference.get();
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
            	gameController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating()) {
                    dragging = false;
                }
            }
        });

        carNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	gameController boardController = boardReference.get();
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
	
	public int getCarType() {
		return type;
	}
	//Make move to newR and newC - animTime in ms
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
				gameController boardController = boardReference.get();
				if (boardController != null) {
					carNode.setTranslateX(0); //Must reset these to 0 (remove effect of animation)
					carNode.setTranslateY(0);
					carNode.relocate(NtoCoord(c)+1, NtoCoord(r)+1); //Must set coordinates again to cancel translate
                    boardController.MakeMove(oldR, oldC, r, c);
                    boardController.AnimatingFin();
                }
			}

		});

	}
	
	public int getR() {
		return r;
	}
	public int getC() {
		return c;
	}
	
	
	
	private double setCoord(double newCoord, double minCoord, double maxCoord) {
        if (newCoord > maxCoord) {
            return maxCoord;
        }
        if (newCoord < minCoord) {
            return minCoord;
        }
        return newCoord;
    }

    /*
    private double getCoordInParent(int flag) {
        double localX = carNode.getLayoutX();
        double localY = carNode.getLayoutY();
        Point2D XYInParent = carNode.localToParent(localX, localY);
        if (flag == X) {
            return XYInParent.getX();
        } else {
            return XYInParent.getY();
        }

    }
    */
}