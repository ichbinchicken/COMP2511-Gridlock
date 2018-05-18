import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameEngine extends Application {
	private static final int  EMPTY=0;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;

	Puzzle currPuzzle;
	BoardController bCont;
	
    public GameEngine() {
        getNewPuzzle();

    }
	
	public Puzzle getNewPuzzle(){
		currPuzzle = new Puzzle(6,6);
		currPuzzle.getBestMove();
		return currPuzzle;
	}
	
	public ArrayList<Car> GetCarList(){
		return currPuzzle.GenCarList();
	}
	
	public boolean MakeMove(int r,int c,int newR,int newC) {
		return currPuzzle.MakeMove(r, c, newR, newC);
	}
	
	public ArrayList<Car> UIGetPuzzle(){
		currPuzzle = new Puzzle(6,6);
		return currPuzzle.GenCarList();
	}
	
	public void RestartPuzzle(){
		currPuzzle.RestartPuzzle();
		//return currPuzzle.GenCarList();
	}
	
	public int getMoves() {
		return currPuzzle.getMoves();
	}
	
	public int getMinMoves() {
		return currPuzzle.getInitMoves();
	}

	

    @Override
    public void start(Stage primaryStage) throws Exception {
    	
        GameEngine engine = new GameEngine();
        Screen screen = new BoardScreen(primaryStage,engine);
        screen.show();

    }

    public static void main(String[] args) {
        launch(args);

    }
    

}
