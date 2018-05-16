import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Car {
    Image IMAGE; 
    ImageView redCar;
	public static final int  VERT=0;
	public static final int HORIZ=1;
	
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

	
	public Car(double squareLength,int r, int c,int type, int length, Pane pane, int id) {
		this.id=id;
		this.r=r;
		this.c=c;
		this.squareLength = squareLength;
		this.type=type;
		this.length = length;
		IMAGE = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/9/9d/Rush_Hour_sliding_block_puzzle.jpg/1920px-Rush_Hour_sliding_block_puzzle.jpg");
	    redCar = new ImageView(IMAGE);
		//redCar = new Rectangle();
	    max=squareLength*6;
	    redCar.setX(r*squareLength+1);
	    redCar.setY(c*squareLength+1);
	    if(type==1) {
		    redCar.setFitHeight(squareLength-2);
		    redCar.setFitWidth(squareLength*length-2);
	    	//redCar.setWidth(squareLength*length);
	    	//redCar.setHeight(squareLength);

	    }
	    else {
	    	//redCar.setWidth(squareLength);
	    	//redCar.setHeight(squareLength*length);


		    redCar.setFitHeight((squareLength)*length-2);
		    redCar.setFitWidth(squareLength-2);	    	
	    }

	    addMouseEvent();
	    this.pane = pane;
	    pane.getChildren().add(redCar);
	    redCar.toFront();
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
	        	if(type==VERT) {
	        		if(!checkIntersection()) {
		        		coord = event.getSceneY() - mousey;
		        		y = redCar.getLayoutY()+coord;
		        		redCar.setLayoutY(y);
		        		mousey = event.getSceneY();
	        		}

	        	}
	        	else {

	        		if(!checkIntersection()) {
		        		coord = event.getSceneX() - mousex;
		        		x+=coord;
		        		redCar.setLayoutX(x);
		        		mousex = event.getSceneX();	        		
	        		}
	        	}
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
            	
            	r=CoordtoN(redCar.getX()+redCar.getLayoutX());
            	c=CoordtoN(redCar.getY()+redCar.getLayoutY());
            	System.out.println("Coords r,c" + r+ ","+ c );


            }
        });
        
		
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
}
