import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class Car {
    private Image IMAGE;
    private ImageView carNode;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;
	private static final int X = 0;
    private static final int Y = 0;

	private int r;
    private int c;
	
	private double x;
	private double y;
	private int length;
	private double squareLength;
	private int type;
	private boolean dragging;
	private boolean movetoFront;
	private double mousex;
	private double mousey;
	private BoardController boardController;
	private double min=0;
	private Bounds bounds;
	//private double max;


	public Car(int r, int c, int type, int length) {
		this.r=r; // backend coordinate
		this.c=c; // backend coordinate
		this.type=type;
		this.length = length;  // backend length
	}

	//
	public void frontEndCarConstructor(double squareLength, Bounds b, BoardController bcontroller) {
		this.squareLength = squareLength;
		this.boardController = bcontroller;
		//Generate the Image
		if(type!=GOALCAR) {
		IMAGE = new Image("car.jpg");
		}
		if(type==GOALCAR) {
		IMAGE = new Image("goalcar.png");
		}
		carNode = new ImageView(IMAGE);
	    //max=squareLength*6;
        double initX = c*squareLength+1;
        double initY = r*squareLength+1;
	    carNode.setX(initX);
	    carNode.setY(initY);
	    bounds = new BoundingBox(b.getMinX()-initX, b.getMinY()-initY, b.getWidth(), b.getHeight());
	    
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


	private double mRound(double value, double factor) {
		return Math.round(value/factor) * factor;
	}
	

	private int CoordtoN(double coord) {
		//System.out.println(coord + "|");
		return (int) Math.round(coord/squareLength);
	}

	private void addMouseEvent() {
	    carNode.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	mousex = event.getSceneX();
	        	mousey = event.getSceneY();

	        	x = carNode.getLayoutX();
	        	y = carNode.getLayoutY();
                //x = getCoordInParent(X);
                //y = getCoordInParent(Y);
                //System.out.println("Car X: "+x+" Y: "+y);
	        	
	        	carNode.toFront();
	
	        }
	    });
	    
	    carNode.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	dragging=true;
	        	if(type==VERCAR || type == VERTRUCK) {
                    if(!boardController.checkIntersection(Car.this)) {
		        		double offsetY = event.getSceneY() - mousey;
		        		double newY = y + offsetY;
		        		y = setCoord(newY, bounds.getMinY(), bounds.getMaxY()-length*squareLength);
		        		//carNode.relocate(x, y);
		        		carNode.setLayoutY(y);
	        		}
	        	}
	        	else {

	        		if(!boardController.checkIntersection(Car.this)) {
                        double offsetX = event.getSceneX() - mousex;

                        double newX = x + offsetX;
                        x = setCoord(newX, bounds.getMinX(), bounds.getMaxX()-length*squareLength);
		        		//carNode.relocate(x, y);
                        carNode.setLayoutX(x);
	        		}
	        	}
	    		mousey = event.getSceneY();
	    		mousex = event.getSceneX();	        		
	
	    		event.consume();
	
	        }
	    });
	    
	    carNode.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	            dragging = false;
	        }
	    });
	    
	    carNode.setOnMouseReleased(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {

                x = carNode.getLayoutX();
                y = carNode.getLayoutY();
                //x = getCoordInParent(X);
                //y = getCoordInParent(Y);

	        	double scaledY=Math.round(y/squareLength) * squareLength;
	        	double scaledX=Math.round(x/squareLength) * squareLength;

	        	//carNode.relocate(scaledX, scaledY);

	        	carNode.setLayoutX(scaledX);
	        	carNode.setLayoutY(scaledY);
	        	event.consume();
	        	
	        	if(r!=CoordtoN(carNode.getX()+ carNode.getLayoutX())||c!=CoordtoN(carNode.getY()+ carNode.getLayoutY())) {
	        		r=CoordtoN(carNode.getX()+ carNode.getLayoutX());
	        		c=CoordtoN(carNode.getY()+ carNode.getLayoutY());
	        		//Update Coordinates - tell game engine we have moved
	        		
	        	}
	        	c=CoordtoN(carNode.getY()+ carNode.getLayoutY());
	        	//System.out.println("Coords r,c" + r+ ","+ c );
	
	        }
	    });
	    
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