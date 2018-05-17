import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.*;

public class Car {
    Image IMAGE; 
    ImageView redCar;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;
	
	int id;
	int r;
	int c;
	
	double x;
	double y;
	int length;
	double squareLength;
	int type;
	boolean dragging;
	boolean movetoFront;
	private double mousex;
	private double mousey;
	Pane pane;
	double min=0;
	double max;

	public Car(int r, int c, int type, int length) {
		this.r=r;
		this.c=c;
		this.type=type;
		this.length = length;
	}
	
	public void CarGraphics(double squareLength, Pane pane) {
		this.squareLength = squareLength;
		this.pane = pane;
		//Generate the Image
		if(type!=GOALCAR) {
		IMAGE = new Image("https://vignette.wikia.nocookie.net/fantendo/images/6/6e/Small-mario.png/revision/latest?cb=20120718024112");
		}
		if(type==GOALCAR) {
		IMAGE = new Image("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPyPahrzg7s6Gx_qlYznekiF6KjZvCAQjTa_lef0KqEICiS_60");
		}
		redCar = new ImageView(IMAGE);
	    max=squareLength*6;
	    redCar.setX(c*squareLength+1);
	    redCar.setY(r*squareLength+1);
	    
	    if(type==VERCAR || type == VERTRUCK) {
	    	//redCar.setWidth(squareLength);
	    	//redCar.setHeight(squareLength*length);
		    redCar.setFitHeight((squareLength)*length-2);
		    redCar.setFitWidth(squareLength-2);	   
	    }
	    else {
 	
		    redCar.setFitHeight(squareLength-2);
		    redCar.setFitWidth(squareLength*length-2);
	    	//redCar.setWidth(squareLength*length);
	    	//redCar.setHeight(squareLength);

	    }
	    addMouseEvent();
	    pane.getChildren().add(redCar);
	    redCar.toFront();


	}
	
	
	public Node getCar() {
		return redCar;
	}
	
	private boolean checkIntersection() {
		ObservableList<Node> l = pane.getChildren();
		Bounds bounds = redCar.getBoundsInParent();
		for(Node block : l) {
			if(block==redCar || block instanceof Rectangle) {
				continue;
			}
			//if(intersect(redCar,block.)) {
			if(block.getBoundsInParent().intersects(bounds)) {
				return true;
			}
		}
		return false;
	}
	

	
	private double mRound(double value, double factor) {
		return Math.round(value/factor) * factor;
	}
	

	private int CoordtoN(double coord) {
		//System.out.println(coord + "|");
		return (int) Math.round(coord/squareLength);
	}




	private void addMouseEvent() {
	    redCar.setOnMousePressed(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	mousex = event.getSceneX();
	        	mousey = event.getSceneY();
	        	
	        	x = redCar.getLayoutX();
	        	y = redCar.getLayoutY();
	        	
	        	redCar.toFront();
	
	        }
	    });
	    
	    redCar.setOnMouseDragged(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	double coord=0;
	        	dragging=true;
	        	//boolean collision=false;
	        	if(type==VERCAR || type == VERTRUCK) {
	        		if(!checkIntersection()) {
		        		coord = event.getSceneY() - mousey;
		        		y = redCar.getLayoutY()+coord;
		        		redCar.setLayoutY(y);
	        		}
	        		else {
	        			redCar.setLayoutY(Math.round(y/squareLength) * squareLength);
	
	        		}
	
	        	}
	        	else {
	
	        		if(!checkIntersection()) {
		        		coord = event.getSceneX() - mousex;
		        		x+=coord;
		        		redCar.setLayoutX(x);
	        		}
	        		else {
	        			redCar.setLayoutX(Math.round(x/squareLength) * squareLength);
	        		}
	        	}
	    		mousey = event.getSceneY();
	    		mousex = event.getSceneX();	        		
	
	    		event.consume();
	
	        }
	    });
	    
	    redCar.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	
	            dragging = false;
	        }
	    });
	    
	    redCar.setOnMouseReleased(new EventHandler<MouseEvent>() {
	        @Override
	        public void handle(MouseEvent event) {
	        	
	        	x = redCar.getLayoutX();
	        	y = redCar.getLayoutY();
	        	
	        	double scaledY=Math.round(y/squareLength) * squareLength;
	        	double scaledX=Math.round(x/squareLength) * squareLength;
	        
	        	redCar.setLayoutX(scaledX);
	        	redCar.setLayoutY(scaledY);
	        	event.consume();
	        	
	        	if(r!=CoordtoN(redCar.getX()+redCar.getLayoutX())||c!=CoordtoN(redCar.getY()+redCar.getLayoutY())) {
	        		r=CoordtoN(redCar.getX()+redCar.getLayoutX());
	        		c=CoordtoN(redCar.getY()+redCar.getLayoutY());
	        		//Update Coordinates - tell game engine we have moved
	        		
	        	}
	        	c=CoordtoN(redCar.getY()+redCar.getLayoutY());
	        	//System.out.println("Coords r,c" + r+ ","+ c );
	
	        }
	    });
	    
	}
}