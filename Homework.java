import javafx.event.EventHandler;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Homework {
    Rectangle slider;
    //Image image;
    double width;
    double height;
    double squareWidth;
    Pane pane;
    Bounds boundsLocal;
    Bounds boundsParent;

    private double mousex;
    private double mousey;
    private double x;
    private double y;
    private boolean dragging;
    private boolean moveToFront;

    public Homework(double initX, double initY, double width, double height, Color fillColor, Pane pane, double squareWidth) {
        slider = new Rectangle();
        Image IMAGE = new Image("https://en.wikipedia.org/wiki/Rush_Hour_(puzzle)#/media/File:Rush_Hour_sliding_block_puzzle.jpg");
        ImageView redCar = new ImageView(IMAGE);
        
        slider.setX(initX);
        slider.setY(initY);
        this.width = width;
        this.height = height;
        this.pane = pane;
        this.squareWidth = squareWidth;
        //image = new Image("");
        Bounds bLocal = pane.getBoundsInLocal();
        Bounds bParent = pane.getBoundsInParent();

        if (width > height) {
            boundsLocal = new BoundingBox(bLocal.getMinX() - initX, bLocal.getMinY(), bLocal.getWidth(), height);
            boundsParent = new BoundingBox(bParent.getMinX() - initX, bParent.getMinY(), bParent.getWidth(), height);
        } else {
            boundsLocal = new BoundingBox(bLocal.getMinX(), bLocal.getMinY() - initY, width, bLocal.getHeight());
            boundsParent = new BoundingBox(bParent.getMinX(), bParent.getMinY() - initY, width, bParent.getHeight());
        }

        slider.setWidth(width);
        slider.setHeight(height);
        slider.setFill(fillColor);
        slider.setStroke(Color.BLUE);

        this.x = 0;
        this.y = 0;
        mousex = 0;
        mousey = 0;
        dragging = false;
        moveToFront = true;

        addMouseEvent();
        this.pane.getChildren().add(slider);
    }

    private void addMouseEvent() {

        slider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // record the current mouse X and Y position on Node
                mousex = event.getSceneX();
                mousey = event.getSceneY();

                x = slider.getLayoutX();
                y = slider.getLayoutY();

                if (moveToFront) {
                    slider.toFront();
                }
            }
        });

        slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Get the exact moved X and Y

                double offsetX = event.getSceneX() - mousex;
                double offsetY = event.getSceneY() - mousey;

                double newX = x + offsetX;
                double newY = y + offsetY;

                x = setCoord(newX, boundsLocal.getMinX(), boundsLocal.getMaxX() - width);
                y = setCoord(newY, boundsLocal.getMinY(), boundsLocal.getMaxY() - height);

                System.out.println("x is "+x+", y is "+y);

                double scaledX = Math.round(x / squareWidth) * squareWidth;
                double scaledY = Math.round(y / squareWidth) * squareWidth;

                slider.setLayoutX(scaledX);
                slider.setLayoutY(scaledY);

                dragging = true;

                // again set current Mouse x AND y position
                System.out.println("eventX is "+event.getSceneX()+", eventY is "+event.getSceneY());
                mousex = setCoord(event.getSceneX(), boundsParent.getMinX(), boundsParent.getMaxX() - width);
                mousey = setCoord(event.getSceneY(), boundsParent.getMinY(), boundsParent.getMaxY() - height);
                System.out.println("mousex is "+mousex+", mousey is "+mousey);

                event.consume();
            }
        });

        slider.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                dragging = false;
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
    
    
    private void checkIntersection() {
    	
    }
}
