import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameEngine extends Application {
	private static final int NumDifficulties=5;
	private static final int NumThreads = 5;
	ArrayList<BoundedQueue<Puzzle>> queueList = new ArrayList<BoundedQueue<Puzzle>>(NumDifficulties);
	BoundedQueue<Puzzle> queue=null;
	
	int currDifficulty=0;


	Puzzle currPuzzle;
	BoardController bCont;
	
    public GameEngine() {
		for(int i=0;i<NumDifficulties;i++) {
			queue = new BoundedQueue<Puzzle>(50);
			queueList.add(queue);
		}
		//Start up 5 background threads to quickly generate a few puzzles
		//One thread will be running until all puzzles are full
		ExecutorService executor = Executors.newFixedThreadPool(NumThreads);
		for(int i=0;i<NumThreads;i++) {
			Runnable run = new GenThread(queueList,1, 6, 2);
			executor.execute(run);
		}
        executor.shutdown();
        while(!executor.isTerminated()) {
       	executor.isTerminated();
        }
        Runnable run = new GenThread(queueList,100000, 6, 2);
		Thread t = new Thread(run);
		t.start();


        //queue = queueList.get(NumDifficulties-1);

        getNewPuzzle();

    }
	
	public Puzzle getNewPuzzle(){
		if(currDifficulty<0) {
			currDifficulty=0;
		}
        queue = queueList.get(currDifficulty);
        if(queue.isEmpty()) {
        	//Generate more boards quickly
    		//ExecutorService executor = Executors.newFixedThreadPool(NumThreads);
    		//for(int i=0;i<NumThreads;i++) {
    		//	Runnable run = new GenThread(queueList,10, 6, 2);
    		//	executor.execute(run);
    		//}
            //executor.shutdown();
            //while(!executor.isTerminated()) {
            //	executor.isTerminated();
            //}
        	
    		Runnable run = new GenThread(queueList,10, 6, 2);
    		Thread t = new Thread(run);
    		t.start();
	        if(queue.isEmpty()) {
	        	currDifficulty--;
	        	return getNewPuzzle();
	        	
	        }
        }
        currPuzzle = queue.remove();
		//Runnable run = new GenThread(queueList,10, 6, 2);
		//Thread t = new Thread(run);
		//t.start();
		//currPuzzle = new Puzzle(6,6);
		//currPuzzle.getBestMove();
		currDifficulty=currDifficulty+1;
		currDifficulty=currDifficulty%(NumDifficulties-1);
	
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
