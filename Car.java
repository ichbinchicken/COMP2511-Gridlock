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
	private WeakReference<BoardController> boardReference;
	private int initR;
	private int initC;
	private int[] moveSpace = {0,0,0,0};
	private Bounds moveBounds;
	double initX,initY;
    /***
     * @param r backend coordinate
     * @param c backend coordinate
     * @param  length back length
     */
	public Car(int r, int c, int type, int length) {
		this.r=r;
		this.c=c;
		initR=r;
		initC=c;
		this.type=type;
		this.length = length;
	}

	//
	public void frontEndCarConstructor(double squareLength, Bounds b, BoardController bcontroller) {

		this.squareLength = squareLength;
		this.boardReference = new WeakReference<BoardController>(bcontroller);
		if(type!=GOALCAR) {
		IMAGE = new Image("car.jpg");
		}
		if(type==GOALCAR) {
		IMAGE = new Image("goalcar.png");
		}
		carNode = new ImageView(IMAGE);
        initX = c*squareLength+1;
        initY = r*squareLength+1;
	    carNode.setX(initX);
	    carNode.setY(initY);
	    
	    if(type==VERCAR || type == VERTRUCK) {
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
	private int CoordtoN(double coord) {

		return (int) Math.round(coord/squareLength);
	}
	
	private double NtoCoord(int n) {
		return n*squareLength;
	}

	private void addMouseEvent() {
        carNode.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BoardController boardController = boardReference.get();
                if (boardController != null && !boardController.GetAnimating()) {
                    mousex = event.getSceneX();
                    mousey = event.getSceneY();
                    x = carNode.getLayoutX();
                    y = carNode.getLayoutY();
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

                BoardController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating() && moveBounds!=null) {

                    dragging = true;
                    if (type == VERCAR || type == VERTRUCK) {
                            double offsetY = event.getSceneY() - mousey;
                            event.getY();

                            double newY = y + offsetY;
                            y = setCoord(newY, moveBounds.getMinY(), moveBounds.getMaxY() - length * squareLength);
                            carNode.setLayoutY(y);
                        //}
                    } else {

                            double offsetX = event.getSceneX() - mousex;

                            double newX = x + offsetX;
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
                BoardController boardController = boardReference.get();
                if (boardController!= null && !boardController.GetAnimating()) {
                    dragging = false;
                }
            }
        });

        carNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                BoardController boardController = boardReference.get();
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
                        boardController.MakeMove(oldR, oldC, r, c);

                    }
                }
            }
        });

    }
	
	public int getCarType() {
		return type;
	}
    /***
     * Make move to newR and newC - animTime in ms
     */
	public void CarMakeAnimatingMove(int newR, int newC, int animTime) {
		double xshift = NtoCoord(newC-c);
		double yshift = NtoCoord(newR-r);
		Bounds cBnd = carNode.getBoundsInLocal();
		double xstart = NtoCoord(initC)+cBnd.getWidth()/2+1;
		double ystart = NtoCoord(initR)+cBnd.getHeight()/2+1;
		Path p = new Path();
		p.getElements().add(new MoveTo(xstart,ystart));
		p.getElements().add(new LineTo(xstart+xshift, ystart+yshift));
		PathTransition pt = new PathTransition(Duration.millis(animTime),p);
		pt.setNode(carNode);
		pt.setPath(p);
		pt.setCycleCount(1);
		
		pt.play();
		final int oldR=r;
		final int oldC=c;
		r=newR;
		c=newC;
		pt.setOnFinished(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
                BoardController boardController = boardReference.get();
				if (boardController != null) {
					carNode.setTranslateX(0);
					carNode.setTranslateY(0);
					carNode.relocate(NtoCoord(c)+1, NtoCoord(r)+1);
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
}