import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class GameEngine  {
	private static final int NumDifficulties=5;
	//private static final int NumDiffSizes = 3;
	private static final int NumThreads = 5;
	private ArrayList<BoundedQueue<Puzzle>> queueList = new ArrayList<BoundedQueue<Puzzle>>(NumDifficulties);
	private BoundedQueue<Puzzle> queue=null;
	private ExecutorService executor;
	private boolean GameWin = false;
	private int currDifficulty=0;
	private int size=6;
	private Mode gameMode = Mode.TIMED;
	
	private int StoryLevel=0;


	Puzzle currPuzzle;
	BoardController bCont;
	
    public GameEngine() {
		for(int i=0;i<NumDifficulties;i++) {
			queue = new BoundedQueue<Puzzle>(50);
			queueList.add(queue);
		}
		//queue.setDebug(true);
		//Start up 5 background threads to quickly generate a few puzzles
		//One thread will be running until all puzzles are full
		executor = Executors.newFixedThreadPool(NumThreads);
        Runnable run = new GenThread(queueList,100000, size, 2);
        executor.execute(run);

		for(int i=0;i<NumThreads-1;i++) {
			run = new GenThread(queueList,100000, size, 2);
			executor.execute(run);
		}
		//Thread t = new Thread(run);
		//t.start();


        //queue = queueList.get(NumDifficulties-1);

        getAnyPuzzle();

    }
	
    public Puzzle getAnyPuzzle() {
    	int tempDiff=0;
        while(1!=2) {
        	queue = queueList.get(tempDiff);
        	if(!queue.isEmpty()) {
        		break;
        	}
        	tempDiff--;
        	if(tempDiff==-1) {
        		tempDiff=NumDifficulties-1;
        	}
        }
        try {
			currPuzzle = queue.remove();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        return currPuzzle;
        
    }
    
	public Puzzle getNewPuzzle(){
		if(size<6) {
			Puzzle p=null;
			int moves=0;
			while(moves<2) {
				p = new Puzzle(size,2,true);
				moves = p.getInitMoves();
			}
			currPuzzle = p;

		}
		else{
			if(GameWin==false && currPuzzle!=null) { //Previous game was not completed - add to end of queue to limit generation
			currPuzzle.RestartPuzzle();
			try {
				
				if(!queue.isFull() ) {
					queue.Forceadd(currPuzzle);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			GameWin=false;
	        queue = queueList.get(currDifficulty);
	        if(queue.isEmpty()) {
		        System.out.println("QUEUE IS EMPTY - TRY EASIER GAME" + currDifficulty);
		        int tempDiff=currDifficulty;
	        	int inc=-1;
		        while(1!=2) {
		        	queue = queueList.get(tempDiff);
		        	if(!queue.isEmpty()) {
		        		break;
		        	}
		        	tempDiff = tempDiff+inc;
		        	if(tempDiff==-1) {
		        		inc=1;
		        		tempDiff=0;
		        	}
		        	if(tempDiff>=NumDifficulties) {
		        		inc=-1;
		        		tempDiff = NumDifficulties-1;
		        	}
		        	
		        }
		        
	        }
	        
	        try {
				currPuzzle = queue.remove();
	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	        size = currPuzzle.getSize();
			return currPuzzle;
		
	}
	
	public ArrayList<Car> GetCarList(){
		return currPuzzle.GenCarList();
	}
	
	public boolean MakeMove(int r,int c,int newR,int newC) {
		boolean ret = currPuzzle.MakeMove(r, c, newR, newC);
		if(ret==true) {
			GameWin=true;
			if(gameMode== Mode.STORY) {
				StoryLevel++;
				if(StoryLevel<=2) {
					size++;
				}
				if(StoryLevel>2) {
					IncrementDifficulty();
				}
			}
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
		currPuzzle = new Puzzle(6,6,true);
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
	

	//May be other settings required
	public void setMode(Mode mode) {
		gameMode = mode;
		switch(gameMode) {
		case STORY:
			currDifficulty=0;
			size=4;
			break;
		case FREEPLAY: case TIMED:
			break;
		}
		System.out.println("GAME MODE" +gameMode);
	}

	public int getTime() {
		switch(gameMode) {
			case TIMED:
				return currPuzzle.getInitMoves()+10;
			case STORY:
				return currPuzzle.getInitMoves()+10;
			case FREEPLAY:
				return 3600*60;
			default:
				return 0;
		}
	}
	public Mode getMode() {
		return gameMode;
	}
	
	
	
	



}
