import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;


public class BoardController extends Controller {
	public static final int  VERT=0;
	public static final int HORIZ=1;

	@FXML
    private Pane boardPane;

    @FXML
    private Label totalTime;

    @FXML
    private Button buttonPause;

    @FXML
    private Button buttonResume;

    private double squareWidth;
    private int nSquares;
    private Timeline countDown;
    private int totalSeconds;

    private ArrayList<Car> workload;

    public BoardController() {
        this.nSquares = 6; //this will be replaced dynamically.
        this.totalSeconds = 10; // 5 mins
        workload = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        this.squareWidth = boardPane.getPrefWidth()/nSquares;
        //System.out.println(boardPane.getPrefWidth());
        //System.out.println(squareWidth);
        drawBoard();
        totalTime.setText(convertTime(totalSeconds));
        buttonPause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                countDown.pause();
            }
        });
        buttonResume.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                countDown.play();
            }
        });

        countDown = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                totalSeconds--;
                totalTime.setText(convertTime(totalSeconds));
                if (totalSeconds <= 0) {
                    countDown.stop();
                    Label gameOver = new Label("GAME OVER YOU IDIOT!!!");
                    gameOver.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 24));
                    gameOver.setTextFill(Color.RED);
                    //boardPane.getChildren().add(gameOver);
                }
            }
        }));
        countDown.setCycleCount(Animation.INDEFINITE);
        countDown.playFromStart();
    }

    private void drawBoard() {
        Rectangle[][] rec = new Rectangle[nSquares][nSquares];

        for (int i = 0; i < nSquares; i ++) {
            for (int j = 0; j < nSquares; j ++) {
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * squareWidth);
                rec[i][j].setY(j * squareWidth);
                rec[i][j].setWidth(squareWidth);
                rec[i][j].setHeight(squareWidth);
                rec[i][j].setFill(Color.valueOf("orange"));
                rec[i][j].setStroke(Color.BLUE);
                boardPane.getChildren().add(rec[i][j]);
            }
        }
        drawBoarder();
        addRedCar();
    }


    private void drawBoarder() {
        Line l= new Line();
        l.setEndY(nSquares*squareWidth);
        boardPane.getChildren().add(l);
        l=new Line();
        l.setEndX(nSquares*squareWidth);
        boardPane.getChildren().add(l);
        l=new Line();
        l.setStartY(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        boardPane.getChildren().add(l);
        l=new Line();
        l.setStartX(nSquares*squareWidth);
        l.setEndX(nSquares*squareWidth);
        l.setEndY(nSquares*squareWidth);
        boardPane.getChildren().add(l);
    }
    private void addRedCar() {
        /*Homework assn1 = new Homework(squareWidth, squareWidth, squareWidth, 2*squareWidth,
                Color.YELLOW, boardPane, squareWidth);
        Homework assn2 = new Homework(2 * squareWidth, 3 * squareWidth, 2 * squareWidth, squareWidth,
                Color.GREENYELLOW, boardPane, squareWidth);
        workload.add(assn1);
        workload.add(assn2);*/
    	int r=1;
    	int c=1;
    	int length = 2;
    	Car c1 = new  Car(squareWidth,r,c, VERT, length, boardPane,1);
    	Car c2 = new  Car(squareWidth,4,4, HORIZ, length, boardPane,2);
    	Car c3 = new Car(squareWidth,0,0, HORIZ,3,boardPane,3);

    	workload.add(c1);
    	workload.add(c2);
    	workload.add(c3);

    }


    private String convertTime(long secondDelta) {
        // this snippet taken from https://stackoverflow.com/questions/43892644
        long seconds = 1;
        long minutes = seconds * 60;
        long hours = minutes * 60;

        long elapsedHours = secondDelta / hours;
        secondDelta = secondDelta % hours;

        long elapsedMinutes = secondDelta / minutes;
        secondDelta = secondDelta % minutes;

        long elapsedSeconds = secondDelta;
        return String.format("%02d", elapsedHours) + ":" +
                String.format("%02d", elapsedMinutes) + ":" +
                String.format("%02d", elapsedSeconds);
    }
}
