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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.util.ArrayList;


public class BoardController extends Controller {
    @FXML
    private Pane boardPane;

    @FXML
    private Label totalTime;

    @FXML
    private Button buttonPause;

    private Rectangle curtain;
    private Label gameOver;

    private double squareWidth;
    private int nSquares;
    private Timeline countDown;
    private int totalSeconds;
    private boolean running;

    private ArrayList<Homework> workload;

    private final Color boardColor = Color.ORANGE;

    public BoardController() {
        this.nSquares = 6; //this will be replaced dynamically.
        this.totalSeconds = 10; // 5 mins
        workload = new ArrayList<>();
        running = true;
    }

    @FXML
    public void initialize() {
        this.squareWidth = boardPane.getPrefWidth()/nSquares;
        //System.out.println(boardPane.getPrefWidth());
        //System.out.println(squareWidth);
        drawBoard();
        totalTime.setText(convertTime(totalSeconds));

        // init curtain
        curtain = new Rectangle(boardPane.getPrefWidth(), boardPane.getPrefHeight(), boardColor);
        curtain.setVisible(false);
        curtain.setX(0);
        curtain.setY(0);
        boardPane.getChildren().add(curtain);

        // init game over message
        gameOver = new Label("GAME OVER");
        gameOver.setFont(new Font("DejaVu Sans Mono for Powerline Bold", 40));
        gameOver.setTextFill(Color.WHITESMOKE);
        gameOver.layoutXProperty().bind(boardPane.widthProperty().subtract(gameOver.widthProperty()).divide(2));
        gameOver.layoutYProperty().bind(boardPane.heightProperty().subtract(gameOver.heightProperty()).divide(2));
        gameOver.setVisible(false);
        boardPane.getChildren().add(gameOver);

        // init button
        buttonPause.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (running) {
                    countDown.pause();
                    running = false;
                    buttonPause.setText("Resume");
                    curtain.setVisible(true);
                } else {
                    countDown.play();
                    running = true;
                    buttonPause.setText("Pause");
                    curtain.setVisible(false);
                }
            }
        });

        // init timer
        countDown = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                totalSeconds--;
                totalTime.setText(convertTime(totalSeconds));
                if (totalSeconds <= 0) {
                    countDown.stop();
                    buttonPause.setDisable(true);
                    curtain.setVisible(true);
                    gameOver.setVisible(true);
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
                rec[i][j].setFill(boardColor);
                rec[i][j].setStroke(Color.BLUE);
                boardPane.getChildren().add(rec[i][j]);
            }
        }
        addRedCar();
    }


    private void addRedCar() {
        Homework assn1 = new Homework(squareWidth, squareWidth, squareWidth, 2*squareWidth,
                Color.YELLOW, boardPane, squareWidth);
        Homework assn2 = new Homework(2 * squareWidth, 3 * squareWidth, 2 * squareWidth, squareWidth,
                Color.GREENYELLOW, boardPane, squareWidth);
        workload.add(assn1);
        workload.add(assn2);
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
