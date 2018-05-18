import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameEngine  {
	private static final int NumDifficulties=5;
	private static final int NumThreads = 5;
	private ArrayList<BoundedQueue<Puzzle>> queueList = new ArrayList<BoundedQueue<Puzzle>>(NumDifficulties);
	private BoundedQueue<Puzzle> queue=null;
	private ExecutorService executor;
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
		executor = Executors.newFixedThreadPool(NumThreads);
		for(int i=0;i<NumThreads;i++) {
			Runnable run = new GenThread(queueList,1, 6, 2);
			executor.execute(run);
		}
        Runnable run = new GenThread(queueList,100000, 6, 2);
        executor.execute(run);
		//Thread t = new Thread(run);
		//t.start();


        //queue = queueList.get(NumDifficulties-1);

        getNewPuzzle();

    }
	
	public Puzzle getNewPuzzle(){
		if(currDifficulty<0) {
			currDifficulty=0;
		}
        queue = queueList.get(currDifficulty);
        if(queue.isEmpty()) {
        	/*Generate more boards quickly
    		ExecutorService executor = Executors.newFixedThreadPool(NumThreads);
    		for(int i=0;i<NumThreads;i++) {
    			Runnable run = new GenThread(queueList,10, 6, 2);
    			executor.execute(run);
    		}
            executor.shutdown();
            while(!executor.isTerminated()) {
            	executor.isTerminated();
            }*/
        	
    		Runnable run = new GenThread(queueList,10, 6, 2);
    		executor.execute(run); //Add new thread
	        if(queue.isEmpty()) {
	        	if(currDifficulty!=0) { //Give an easier puzzle
	        		currDifficulty--;
	        		return getNewPuzzle();
	        	
	        	}
	        	else {
	        		while(queue.isEmpty()) {
	        			try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							System.out.println("Exception Catch");
						}
	        		}
	        	}
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
	
	public int[] getNextMove() {
		return currPuzzle.getBestMove();
	}
	
	
	//Only use for other testing
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

	
	


    

}
