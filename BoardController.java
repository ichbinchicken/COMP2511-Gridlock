import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;


public class BoardController extends Controller {
	// public static final int  VERT=0;
	// public static final int HORIZ=1;

	@FXML
    private Pane boardPane;
    @FXML
    private Label totalTime;
    @FXML
    private Button buttonPause;
    @FXML
    private Button buttonRestart;

    private Rectangle curtain;
    private Label gameOver;

    private double squareWidth;
    private int nSquares;
    private Timeline countDown;
    private final int totalSeconds;  // The duration of game, should not changed
    private int currSeconds;
    private boolean running;
    private Puzzle puzzle;

    private ArrayList<Car> workload;

    private final Color boardColor = Color.ORANGE;

    public BoardController() {
        nSquares = 6; //this will be replaced dynamically.
        totalSeconds = currSeconds = 3600;
        workload = new ArrayList<>();
        running = true; // this is to check whether the game is paused. Initially, it's running.
    }

    @FXML
    public void initialize() {
        this.squareWidth = boardPane.getPrefWidth()/nSquares;

        // init curtain
        curtain = new Rectangle(boardPane.getPrefWidth(), boardPane.getPrefHeight(), boardColor);
        curtain.setX(0);
        curtain.setY(0);

        // init game over message
        gameOver = new Label("GAME OVER");
        gameOver.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 40));
        gameOver.setTextFill(Color.WHITESMOKE);
        // place the label in the centre
        gameOver.layoutXProperty().bind(boardPane.widthProperty().subtract(gameOver.widthProperty()).divide(2));
        gameOver.layoutYProperty().bind(boardPane.heightProperty().subtract(gameOver.heightProperty()).divide(2));

        GenNewPuzzle();

        // must call drawBoard after curtain and gameOver are init'd, and after GenNewPuzzle
        drawBoard();

        // init buttons
        buttonPause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (running) {
                    countDown.pause();
                    running = false;
                    buttonPause.setText("Resume");
                    curtain.setVisible(true);
                    curtain.toFront();
                } else {
                    countDown.play();
                    running = true;
                    buttonPause.setText("Pause");
                    curtain.setVisible(false);
                    //curtain.toFront();
                }
            }
        });

        buttonRestart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                boardPane.getChildren().clear();
                currSeconds = totalSeconds;
                drawBoard();
                countDown.playFromStart();
            }
        });

        // init timer
        countDown = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                currSeconds--;
                totalTime.setText(convertTime(currSeconds));
                if (currSeconds <= 0) {
                    countDown.stop();
                    buttonPause.setDisable(true);
                    curtain.setVisible(true);
                    curtain.toFront();
                    gameOver.setVisible(true);
                    gameOver.toFront();
                }
            }
        }));

        countDown.setCycleCount(Animation.INDEFINITE);
        countDown.playFromStart(); // initialise the timer at the first time
    }

    private void drawBoard() {
        running = true;
        buttonPause.setDisable(false);
        buttonPause.setText("Pause");
        totalTime.setText(convertTime(totalSeconds));
        curtain.setVisible(false);
        gameOver.setVisible(false);

        Rectangle[][] rec = new Rectangle[nSquares][nSquares];

        for (int i = 0; i < nSquares; i ++) {
            for (int j = 0; j < nSquares; j ++) {
                rec[i][j] = new Rectangle();
                rec[i][j].setX(i * squareWidth);
                rec[i][j].setY(j * squareWidth);
                rec[i][j].setWidth(squareWidth);
                rec[i][j].setHeight(squareWidth);
                rec[i][j].setFill(boardColor);
                rec[i][j].setStroke(Color.BLUE);
                boardPane.getChildren().add(rec[i][j]);
            }
        }
        drawBorder();
        drawCars();
        boardPane.getChildren().add(curtain);
        boardPane.getChildren().add(gameOver);
    }


    // draw border for the board
    private void drawBorder() {
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

    private void drawCars() {
        workload.clear();
    	workload = puzzle.GenCarList();
        for(Car c: workload) {
            c.frontEndCarConstructor(squareWidth, boardPane.getBoundsInLocal(),this);
            Node car = c.getCar();
            boardPane.getChildren().add(car);
            car.toFront();
        }

    }

    public boolean checkIntersection(Car car) {
        Bounds bounds = car.getCar().getBoundsInParent();
        for (Car c: workload) {
            if (c == car) {
                continue;
            }
            Bounds cbounds = c.getCar().getBoundsInParent();
            if(cbounds.intersects(bounds)) {
                return true;
            }
        }
        return false;
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

    private void GenNewPuzzle(){
        puzzle = new Puzzle(6,6);
        puzzle.printBoard();
    }
}