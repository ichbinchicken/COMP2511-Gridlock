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


public class BoardController extends Controller {
    @FXML
    private Pane boardPane;

    @FXML
    private SplitPane splitPane;

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
    private double mousex;
    private double mousey;
    private double x;
    private double y;
    private boolean dragging;
    private boolean moveToFront;

    public BoardController() {
        this.nSquares = 6; //this will be replaced dynamically.
        this.totalSeconds = 10; // 5 mins
        x = 0;
        y = 0;
        mousex = 0;
        mousey = 0;
        dragging = false;
        moveToFront = true;
    }

    @FXML
    public void initialize() {
        this.squareWidth = boardPane.getPrefWidth()/nSquares;
        this.splitPane.setDividerPositions(0.65);
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
                    boardPane.getChildren().add(gameOver);
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
        addRedCar();
    }


    private void addRedCar() {
        Rectangle redCar = new Rectangle();
        redCar.setX(0);
        redCar.setY(0);
        redCar.setWidth(squareWidth);
        redCar.setHeight(2 * squareWidth);
        redCar.setFill(Color.YELLOW);
        redCar.setStroke(Color.BLUE);

        redCar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // record the current mouse X and Y position on Node
                mousex = event.getSceneX();
                mousey = event.getSceneY();

                x = redCar.getLayoutX();
                y = redCar.getLayoutY();

                if (moveToFront) {
                    redCar.toFront();
                }
            }
        });

        redCar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                // Get the exact moved X and Y

                double offsetX = event.getSceneX() - mousex;
                double offsetY = event.getSceneY() - mousey;

                x += offsetX;
                y += offsetY;

                double scaledX = x;
                double scaledY = y;

                redCar.setLayoutX(scaledX);
                redCar.setLayoutY(scaledY);

                dragging = true;

                // again set current Mouse x AND y position
                mousex = event.getSceneX();
                mousey = event.getSceneY();

                event.consume();
            }
        });

        redCar.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    dragging = false;
                }
            }
        );

        boardPane.getChildren().add(redCar);
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
