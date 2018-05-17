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
    private Image IMAGE;
    private ImageView redCar;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
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
	private boolean movetoFront;
	private double mousex;
	private double mousey;
	private Pane pane;
	private BoardController boardController;
	private double min=0;
	//private double max;


	//public Car(double squareLength,int r, int c,int type, int length, Pane pane, BoardController bcontroller, int id) {
	//	this.id=id;
	public Car(int r, int c, int type, int length) {
		this.r=r;
		this.c=c;
		this.type=type;
		this.length = length;
		//this.boardController = bcontroller;
	}
	
	public void CarGraphics(double squareLength, Pane pane, BoardController bcontroller) {
		this.squareLength = squareLength;
		this.pane = pane;
		this.boardController = bcontroller;
		//Generate the Image
		if(type!=GOALCAR) {
		IMAGE = new Image("car.jpg");
		}
		if(type==GOALCAR) {
		IMAGE = new Image("goalcar.png");
		}
		redCar = new ImageView(IMAGE);
	    //max=squareLength*6;
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

	    }
	    addMouseEvent();
	    pane.getChildren().add(redCar);
	    redCar.toFront();


	}
	
	
	public Node getCar() {
		return redCar;
	}

	/*
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
	*/
	

	
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
                    if(!boardController.checkIntersection(Car.this)) {
		        		coord = event.getSceneY() - mousey;
		        		y += coord;
		        		//y = redCar.getLayoutY()+coord;
		        		redCar.setLayoutY(y);
	        		}
	        		else {
	        			redCar.setLayoutY(Math.round(y/squareLength) * squareLength);
	
	        		}
	
	        	}
	        	else {

	        		if(!boardController.checkIntersection(Car.this)) {
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