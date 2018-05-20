import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GameEngine  {
	private static final int NumDifficulties=5;
	private static final int NumThreads = 5;
	private ArrayList<BoundedQueue<Puzzle>> queueList = new ArrayList<BoundedQueue<Puzzle>>(NumDifficulties);
	private BoundedQueue<Puzzle> queue=null;
	private ExecutorService executor;
	private boolean GameWin = false;
	private int currDifficulty=0;


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
        Runnable run = new GenThread(queueList,10000, 6, 2);
        executor.execute(run);

		for(int i=0;i<NumThreads-1;i++) {
			run = new GenThread(queueList,10000, 6, 2);
			executor.execute(run);
		}
		//Thread t = new Thread(run);
		//t.start();


        //queue = queueList.get(NumDifficulties-1);

        getNewPuzzle();

    }
	
	public Puzzle getNewPuzzle(){
		if(GameWin==false && currPuzzle!=null) { //Previous game was not completed - add to end of queue to limit generation
			currPuzzle.RestartPuzzle();
			try {
				if(!queue.isFull()) {
					queue.add(currPuzzle);
				}
				//currPuzzle = queue.remove();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//DecrementDifficulty();
			//return currPuzzle;
		}
		///else if(currPuzzle!=null) {
		//	//IncrementDifficulty();
		//}
		//currDifficulty= (currDifficulty++)%(NumDifficulties-1);
		//currDifficulty=NumDifficulties-1;
		//currDifficulty=currDifficulty+1;
		//currDifficulty=currDifficulty%(NumDifficulties-1);
		//else {
			GameWin=false;
			//if(currDifficulty<0) {
			//	currDifficulty=0;
			//}
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
	        	
	    		//for(int i=0;i<NumThreads;i++) {
	    		//	Runnable run = new GenThread(queueList,10, 6, 2);
	    		//	executor.execute(run);
	    		//}
	    		/*queue.
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
		        }*/
		        
	        }
	        try {
				currPuzzle = queue.remove();
				//Runnable run = new GenThread(queueList,1, 6, 2);
				//executor.execute(run);
	
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Runnable run = new GenThread(queueList,10, 6, 2);
			//Thread t = new Thread(run);
			//t.start();
			//currPuzzle = new Puzzle(6,6);
			//currPuzzle.getBestMove();
		
			return currPuzzle;
		
	}
	
	public ArrayList<Car> GetCarList(){
		return currPuzzle.GenCarList();
	}
	
	public boolean MakeMove(int r,int c,int newR,int newC) {
		boolean ret = currPuzzle.MakeMove(r, c, newR, newC);
		if(ret==true) {
			GameWin=true;
		}
		return ret;
	}
	
	public int[] getNextMove() {
		return currPuzzle.getBestMove();
	}
	
	public int getBoardSize() {
		return currPuzzle.getSize();
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
	
	//Get number of moves made on this puzzle
	public int getMoves() {
		return currPuzzle.getMoves();
	}
	
	public int getMinMoves() {
		return currPuzzle.getInitMoves();
	}

	public int[] FindMoves(int r, int c) {
		return currPuzzle.FindMoves(r, c);
	}

	public void SetDifficulty(int difficulty) {
		currDifficulty=difficulty;
	}
	
	public void IncrementDifficulty() {
		currDifficulty++;
		if(currDifficulty>=NumDifficulties) {
			currDifficulty=NumDifficulties-1;
		}
		System.out.println("Diff"+ currDifficulty);
	}
	
	public void DecrementDifficulty() {
		currDifficulty--;
		if(currDifficulty<0) {
			currDifficulty=0;
		}
	}


    

}
