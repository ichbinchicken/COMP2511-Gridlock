import java.util.ArrayList;
/**
 * GradLock Project
 * @author Michael Hamilton
 * This class is the bridge connecting frontend and backend
 */
public class GameEngine  {
	private static final int NumDifficulties=5;
	private ArrayList<BoundedQueue<Puzzle>> queueList = new ArrayList<BoundedQueue<Puzzle>>(NumDifficulties);
	private BoundedQueue<Puzzle> queue=null;
	private boolean GameWin = false;
	private int currDifficulty=0;
	private int size=6;
	private Mode gameMode = Mode.TIMED;

	private int StoryLevel=0;
	private ArrayList<GradeLvl> gradeList;  
	private boolean StoryModeEnd = false;
	
	
	private boolean NetworkMode = false;
	private boolean NetworkWaiting = true;
	private Puzzle networkPuzzle = null;
	private boolean NetworkOpponentWon=true;


	Puzzle currPuzzle;
	BoardController bCont;

	/**
	 * Constructor of game engine
	 * @pre none
	 * @post none
	 */
    public GameEngine() {
		for(int i=0;i<NumDifficulties;i++) {
			queue = new BoundedQueue<Puzzle>(10);
			//queue.setDebug(true);
			queueList.add(queue);
		}
		//queue.setDebug(true);
		//Start up 5 background threads to quickly generate a few puzzles
		//One thread will be running until all puzzles are full
		//executor = Executors.newFixedThreadPool(NumThreads);
        Runnable run1 = new GenThread(queueList,-1, size, 2);
        Runnable run2 = new GenThread(queueList,-1, size, 2);
        Runnable run3 = new GenThread(queueList,10, size, 2);
        Runnable run4 = new GenThread(queueList,10, size, 2);

        Thread t1 = new Thread(run1);
        Thread t2 = new Thread(run2);
        Thread t3 = new Thread(run3);
        Thread t4 = new Thread(run4);

        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t3.setPriority(Thread.MIN_PRIORITY);
        t4.setPriority(Thread.MIN_PRIORITY);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        
        getAnyPuzzle();

    }

	/**
	 * getter method of story level
	 * @return the story level
	 * @pre none
	 * @post none
	 */
	public int getStoryLevel() {
    	return StoryLevel;
    }

	/**
	 * get the current puzzle
	 * @return the puzzle
	 * @pre none
	 * @post puzzle != null
	 */
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

	/**
	 * get the new puzzle
	 * @return the new puzzle
	 * @pre none
	 * @post puzzle != null
	 */
	public Puzzle getNewPuzzle(){
		if(size<6) {
			Puzzle p=null;
			int moves=0;
			while(moves<2) {
				p = new Puzzle(size);
				p.GeneratePuzzle(2);
				moves = p.getInitMoves();
			}
			currPuzzle = p;

		}
		else if(size>6) {
			currPuzzle = new Puzzle(size);
			currPuzzle.GetStoredPuzzle();
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
	        networkPuzzle = currPuzzle.DuplicatePuzzle();
	        NetworkOpponentWon= false;
			return currPuzzle;
		
	}

	/**
	 * get the list of frontend cars
	 * @return list of frontend cars
	 * @pre none
	 * @post list != null
	 */
	public ArrayList<Car> GetCarList(){
		return currPuzzle.GenCarList();
	}

	/**
	 * make a move
	 * @param r the curr row
	 * @param c the curr column
	 * @param newR the new row
	 * @param newC the new column
	 * @return boolean for valid move
	 * @pre none
	 * @post none
	 */
	public boolean MakeMove(int r,int c,int newR,int newC) {
		boolean ret = currPuzzle.MakeMove(r, c, newR, newC);
		//if(NetworkMode==true) {
			//NetworkSendMove( r,  c,  newR,  newC);
		//}
		return ret;
	}

	/**
	 * the won state for each level in story mode
	 * @param time the time
	 * @return the level of grade
	 * @pre none
	 * @post gdlvl != null
	 */
	public GradeLvl GameWon(int time) {
		GameWin=true;
		GradeLvl gdlvl = CalculateGrade(time);
		if(gameMode== Mode.STORY) {
			gradeList.add(gdlvl);
			StoryLevel++;
			if(StoryLevel==10) {
				//BEATEN STORY MODE - GIVE GRADE
				StoryModeEnd=true;
				GetFinalGrade();
			}
			if(StoryLevel<=2) {
				size++;
			}
			else if(StoryLevel>=8) {
				size=7;
			}
			else if(StoryLevel>2) {
				IncrementDifficulty();
			}
			
		}
		//System.out.println(gdlvl.getString());
		return gdlvl;

	}

	/**
	 * get the finial grade for story mode
	 * @return the final grade
	 * @pre none
	 * @post none
	 */
	public GradeLvl GetFinalGrade() {
		float mark=0;
		for(GradeLvl glv: gradeList) {
			mark+=glv.getMark();
		}
		mark=mark/gradeList.size();
		int Fmark=(int)Math.round(mark);
		//System.out.println("FINAL GRADE" + Fmark);
		switch(Fmark) {
		case 0:
			return GradeLvl.F;
		case 1:
			return GradeLvl.PC;
		case 2:
			return GradeLvl.P;
		case 3:
			return GradeLvl.C;
		case 4:
			return GradeLvl.D;
		case 5:
			return GradeLvl.HD;
		default:
			return GradeLvl.HD;
		}

	}

	/**
	 * get the all grades of rounds
	 * @return the list of grades
	 * @pre none
	 * @post list != null
	 */
	public ArrayList<GradeLvl> StoryGetAllGrades(){
		return gradeList;
	}

	/**
	 * get the game level you achieved
	 * @return the level you achieved
	 * @pre none
	 * @post none
	 */
	public String StoryGetGradLevel() {
		if(StoryLevel>=10) {
			return "PHD";
		}
		else if(StoryLevel>=8) {
			return "Masters";
		}
		else if(StoryLevel>=6) {
			return "Bachelors";
		}
		else if(StoryLevel>=4) {
			return "High School Certificate";
		}
		else if(StoryLevel>=2) {
			return "School Certificate";
		}
		else {
			return "FAILED";
		}
		
	}

	/**
	 * calculate the grade of each round
	 * @param timeLeft the left time
	 * @return the grade level
	 * @pre none
	 * @post none
	 */
	public GradeLvl CalculateGrade(int timeLeft) {
		int gradeMoves = getMoves() + currPuzzle.getHintsUsed();
		//System.out.println("MIN MOVES" + getMinMoves());
		//System.out.println("GRADE MOVES" + gradeMoves);
		//System.out.println("HINTS " + currPuzzle.getHintsUsed());
		if(timeLeft<=0) {
			return GradeLvl.F;
		}
		else if( gradeMoves<=getMinMoves()) {
			return GradeLvl.HD;
		}
		else if( gradeMoves<=(getMinMoves()*1.5)) {
			return GradeLvl.D;
		}
		else if( gradeMoves<(getMinMoves()*2)) {
			return GradeLvl.C;
		}
		else if(timeLeft>0) {
			return GradeLvl.P;
		}
		return GradeLvl.F;
		
	}

	/**
	 * action of lose a game
	 */
	public void GameLoss() {
		
		if(gameMode==Mode.STORY) {
			StoryModeEnd=true;
			GradeLvl gdlvl = CalculateGrade(0);
			//System.out.println("LOSS WITH" + gdlvl.getString());

			gradeList.add(gdlvl);
			gdlvl = GetFinalGrade();


			//StoryLevel=0;
			//System.out.println("GRAD WITH" + gdlvl.getString());
		}
	}

	/**
	 * get the next move
	 * @return array of next moves
	 */
	public int[] getNextMove() {
		return currPuzzle.getBestMove();
	}

	/**
	 * get the size of board
	 * @return the size of board
	 */
	public int getBoardSize() {
		return currPuzzle.getSize();
	}
	
	
	//Only use for other testing
	/*public ArrayList<Car> UIGetPuzzle(){
		currPuzzle = new Puzzle(6);
		currPuzzle.GeneratePuzzle(2);
		return currPuzzle.GenCarList();
	}*/

	/**
	 * restart the puzzle
	 */
	public void RestartPuzzle(){
		currPuzzle.RestartPuzzle();
		//return currPuzzle.GenCarList();
	}

	/**
	 * Get number of moves made on this puzzle
	 * @return number of moves
	 */
	public int getMoves() {
		return currPuzzle.getMoves();
	}

	/**
	 * get the enemy's moves on this puzzle
	 * @return the number of moves
	 */
	public int getOppMoves() {
		return networkPuzzle.getMoves();
	}

	/**
	 * get the minimum moves of this puzzle
	 * @return the minimum moves
	 */
	public int getMinMoves() {
		return currPuzzle.getInitMoves()-1;
	}

	/**
	 * Find all permitted next moves
	 * @param r the row of the current car
	 * @param c the column of the current car
	 * @return array of moves in all permitted directions, each represented by int
	 */
	public int[] FindMoves(int r, int c) {
		return currPuzzle.FindMoves(r, c);
	}

	/**
	 * Setter for current difficulty level
	 * @param difficulty the difficulty level as an int
	 */
	public void SetDifficulty(int difficulty) {
		currDifficulty=difficulty;
	}

	/**
	 * Increase the difficulty level of the game by 1
	 */
	public void IncrementDifficulty() {
		currDifficulty++;
		if(currDifficulty>=NumDifficulties) {
			currDifficulty=NumDifficulties-1;
		}
		//System.out.println("Diff"+ currDifficulty);
	}

	/**
	 * Decrease the difficulty level of the game by 1
	 */
	public void DecrementDifficulty() {
		currDifficulty--;
		if(currDifficulty<0) {
			currDifficulty=0;
		}
	}

	/**
	 * Configure the game mode and set related variables
	 * @param mode the game mode chosen
	 */
	public void setMode(Mode mode) {
		gameMode = mode;
		switch(gameMode) {
		case STORY:
			currDifficulty=0;
			StoryLevel=0;
			size=4;
			gradeList = new ArrayList<GradeLvl>(10);
			break;
		case FREEPLAY: case TIMED:
			
			size=6;
			break;
		}
		//System.out.println("GAME MODE" +gameMode);
	}

	/**
	 * Get the dynamically calculated time limit for the game depending on its mode and difficulty
	 * @return the time limit of the game
	 */
	public int getTime() {
		switch(gameMode) {
			case TIMED:
				return (int)(currPuzzle.getInitMoves()*1.5) +5;
			case STORY:
				return (int)(currPuzzle.getInitMoves()*1.5) +5;//+10; //For testing - dec to 10 for real
			case FREEPLAY:
				return 3600*60;
			default:
				return 0;
		}
	}

	/**
	 * Getter for current game mode
	 * @return the game mode
	 */
	public Mode getMode() {
		return gameMode;
	}

	/**
	 * Getter for story mode game state
	 * @return whether or not the story mode game is ended
	 */
	public boolean StoryModeEnd() {
		if(	StoryModeEnd==true) {
			StoryModeEnd=false;
			return true;
		}
			
		return false;
	}

	/**
	 * Get the next move of the AI opponent
	 * @return the optimal next move of the current game
	 */
	public int[] NetworkGetMove(){
		
		int[] arr = networkPuzzle.getBestMove();
		if(networkPuzzle.MakeMove(arr[0],arr[1],arr[2],arr[3])){
			NetworkOpponentWon=true;
		}
		return arr;

	}

	/**
	 * Getter for opponent move state
	 * @return whether or not the opponent AI should make a move
	 */
	public boolean NetworkMoveWaiting() {
		return NetworkWaiting;
	}

	/**
	 * set network mode
	 * @param net
	 */
	public void NetworkSetMode(boolean net) {
		NetworkMode = net;
	}

	/**
	 * get network won boolean
	 * @return network won boolean
	 */
	public boolean NetworkOpponentWon() {
		return NetworkOpponentWon;
	}
}
