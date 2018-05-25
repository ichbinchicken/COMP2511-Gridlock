import java.util.*;




/**
 * @author Michael Hamilton
 * 
 * Puzzle -  each puzzle is the board and counts data directly relating to board
 * Contains current board - initial starting point, moves made etc
 * Also generates new boards, and searches through
 *
 */
public class Puzzle {
	private GameBoard board=null; //Current State of Board
	private GameBoard initial=null; //Initial State of Board (Reset)
	private int minInitMoves=0;
	private int numHints=0;
	private int numMoves=0;
	//private ArrayList<Car> carList=null; //List of frontend cars
	//private int difficulty;
	private Search search; //Add parameters here for size etc
	private int Size=6;
	private int GoalC = Size-1;
	private int GoalR =2 ; //Shifted randomly

	
	private static final int  EMPTY=0;
	private static final int  GOALCAR =5;
	private static final int  HCAR=4; //Horizontal sliding
	private static final int  HTRUCK =3;
	private static final int  VCAR =1; //Horizontal sliding
	private static final int  VTRUCK =2; //Horizontal sliding

	public Puzzle(int n) {
		//arr= new ArrayList<Integer>(n*n);
		//ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		this.Size=n;
		GoalC = Size-1;		
	}
	
	
	
	/**
	 * Generate a puzzle 
	 * First get initial state
	 * Then search through and determine furthest point from all state and minimal number of moves
	 * @param MinMoves to solve puzzle
	 * @return number of moves to solve
	 */
	public int GeneratePuzzle(int MinMoves) {
		int maxCars=13; //13 for 6x6 
		int CarP = 80;
		int VerP = 60;
		Random rand = new Random(System.nanoTime());
		GoalR = rand.nextInt(Size-1);
		search=new Search(GoalR,Size);
		//while(moves < minMoves) {
		if(MinMoves<7) {
			maxCars -= 	rand.nextInt(7);
			CarP = 60;
		}
		GameBoard newBoard=null;
		int moves = 0;
		do  {
			board = GenSolution(maxCars,CarP,VerP); //Generate initial board
			newBoard = search.GenBoard(board, MinMoves);
			if(newBoard!=null) {
				 moves=newBoard.getMoves();
			 }
		} while(moves<MinMoves);
		
		minInitMoves=newBoard.getMoves();
		initial = newBoard.copyGameBoard(); //For reset
		board = newBoard;

		return minInitMoves;
	}
	
	/**
	 * Get a previously stored puzzle
	 * @return minimal number of moves to solve puzzle
	 */
	public int GetStoredPuzzle() {
		Random rand = new Random(System.currentTimeMillis());
		GameBoard nB = StoredBoard.values()[rand.nextInt(StoredBoard.values().length)].getBoard();
		board = nB.copyGameBoard();
		initial = nB.copyGameBoard();
		minInitMoves=board.getMoves();
		GoalR = board.findGoalCarR();
		return minInitMoves;
	}
	
	/**
	 * Duplicate current puzzle
	 * @return new Puzzle
	 */
	public Puzzle DuplicatePuzzle() {
		Puzzle p = new Puzzle(Size );
		p.SetNewPuzzleVars(board, initial, GoalC, GoalR, minInitMoves);
		
		return p;
	}
	
	
	/**
	 * Generate puzzle from network sent array
	 * @param arr
	 * @param moves
	 * @param GoalR
	 */
	public void GeneratePuzzlefromArr(ArrayList<Integer> arr, int moves, int GoalR) {
		board = new GameBoard(arr,moves,6);
		initial = board.copyGameBoard();
		this.minInitMoves=moves;
		this.GoalR=GoalR;
		
	}
	/**
	 * Set all variables for a new puzzle - for duplication
	 * @param board Current game board
	 * @param init Initial game board 
	 * @param GoalC - GoalCar column
	 * @param GoalR GoalCar row
	 * @param minInit minimal initial moves to solve
	 */
	public void SetNewPuzzleVars(GameBoard board, GameBoard init, int GoalC, int GoalR, int minInit) {
		this.board= board.copyGameBoard();
		this.initial = init.copyGameBoard();
		this.GoalC = GoalC;
		this.GoalR = GoalR;
		numHints=0;
		numMoves=0;
		minInitMoves=minInit;
		this.search = new Search(GoalR, Size);
	}
	
	
	
	/**
	 * Find a hint - search for best move
	 * 
	 * @return [currR,currC,newR,newC]
	 * Row and Column of piece and new location to move to
	 */
	public int[] getBestMove() {
		
		GameBoard sb = board.copyGameBoard();
		LinkedList<GameBoard> list = search.SearchBoard(sb);
		sb = list.removeFirst(); //Remove start board - current location
		sb = list.removeFirst();
		
		int[] arr = sb.compareBoard(board);
		//System.out.println("ARRAY" + arr[0] + " " + arr[1] + " " + arr[2] + " "+ arr[3]);
		numHints++;
		return arr;
	}
	
	/*public void GivenBoard(Integer[] array) {
		arr = new ArrayList<Integer>(Arrays.asList(array));
	}*/

	/**
	 * Restart puzzle
	 * Board = inital board
	 * Reset moves and hints
	 */
	public void RestartPuzzle() {
		board = initial.copyGameBoard();
		numMoves=0;
		numHints=0;
		
	}
	
	/**
	 * @return Number of hints used on current puzzle
	 */
	public int getHintsUsed() {
		return numHints;
	}
	
	/**
	 * @return size of board (length)
	 * nxn - return n
	 */
	public int getSize() {
		return Size;
	}
	/**
	 * @return Minimal number of moves to solve
	 */
	public int getInitMoves() {
		return minInitMoves;
	}
	
	/**
	 * @return Array of board
	 */
	public ArrayList<Integer> GetBoard(){
		return board.getArr();
	}
	
	//Return isGoal if only move left is red to end
	/**
	 * @return true if board is goal state - completed
	 */
	private boolean isGoalState() {
		int i=GoalC;
		int id = board.getRC(GoalR, i );
		while(id==0) {
			i--;
			id = board.getRC(GoalR,i);
		}
		if(id==GOALCAR) {
			//System.out.println("GOAL STATE");
			return true;
		}
		return false;
	}
	
	/**
	 * Move piece in r,c to newR,newC
	 * 
	 * @param r Row Inital
	 * @param c Column  Initial
	 * @param newR New row
	 * @param newC new column
	 * @return true if move leads to solution
	 * @pre r,c contains piece
	 * @pre newR,newC valid
	 */
	public boolean MakeMove(int r,int c, int newR,int newC) {
		int type = board.getRC(r, c);
		switch(type) {
		case HCAR: case GOALCAR: 
			board.setRC(r, c, EMPTY);
			board.setRC(r, c+1, EMPTY);
			board.setRC(newR, newC, type);
			board.setRC(newR, newC+1, type);
			break;
		case HTRUCK: 
			board.setRC(r, c, EMPTY);
			board.setRC(r, c+1, EMPTY);
			board.setRC(r, c+2, EMPTY);
			board.setRC(newR, newC, type);
			board.setRC(newR, newC+1, type);
			board.setRC(newR, newC+2, type);
			break;
		case VCAR: 
			board.setRC(r, c, EMPTY);
			board.setRC(r+1, c, EMPTY);
			board.setRC(newR, newC, type);
			board.setRC(newR+1, newC, type);
			break;
		case VTRUCK: 
			board.setRC(r, c, EMPTY);
			board.setRC(r+1, c, EMPTY);
			board.setRC(r+2, c, EMPTY);
			board.setRC(newR, newC, type);
			board.setRC(newR+1, newC, type);
			board.setRC(newR+2, newC, type);
			break;
		default:
			break;
		}
		//board.printBoard();
		numMoves++;
		
		return isGoalState();
		//if(isGoalState()) 
	}
	
	/**
	 * @return Number of moves made
	 */
	public int getMoves() {
		return numMoves;
	}
	
	/**
	 * Generate a list of cars, their type and position on board
	 * @return ArrayList Cars on the board
	 */
	public ArrayList<Car> GenCarList(){
		ArrayList<Car> list = new ArrayList<Car>();
		ArrayList<Integer> arr = initial.getArr();
		Car car=null;
		int i=0;
		int r=0;
		int c=0;
		int id;
		while(i<(Size*Size-1)) {
			 id = board.get(i);
			if(id==HCAR) {
				i++;
				c=i%Size-1;
				r=(int)Math.floor(i/Size);
				car = new Car(r,c, HCAR, 2);
				list.add(car);
			}
			else if(id==HTRUCK) {
				i+=2;
				c=i%Size-2;
				r=(int)Math.floor(i/Size);
				car = new Car(r,c, HTRUCK, 3);
				list.add(car);

			}
			else if(id==GOALCAR) {
				i++;
				c=i%Size-1;
				r=(int)Math.floor(i/Size);
				car = new Car(r,c, GOALCAR, 2);
				list.add(car);

			}
			i++;
		}
		i=0;
		int col=0;
		int row=0;
		while(i<arr.size()) {
			id = board.get(i);
			if(id==VCAR) {
				row++;
				c=i%Size;
				r=(int)Math.floor(i/Size);
				car = new Car(r,c, VCAR, 2);
				list.add(car);

			}
			else if(id==VTRUCK) {
				row+=2;
				c=i%Size;
				r=(int)Math.floor(i/Size);
				car = new Car(r,c, VTRUCK, 3);
				list.add(car);

			}
			row++;
			if(row>=Size) {
				row=0;
				col++;
			}
			if(col==Size) {
				return list;
			}
			i=RCtoI(row,col);
		}

		
		return list;
	}
		
		
	
	
	
	
	
	//In general - hardest solutions roughly ~13, 80, 60
	/**
	 * Generate a 'solved board'
	 * Randomly place pieces if room
	 * 
	 * @param maxCars maximal number of cars on board
	 * @param carProb Probability of a car (opposed to truck) /100
	 * @param verProb Probability piece will be vertical faced /100
	 * @return solved board
	 */
	public GameBoard GenSolution(int maxCars, int carProb, int verProb) {
		ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(Size*Size, 0));
		GameBoard board = new GameBoard(arr,-1, Size);

		Random rand = new Random(System.currentTimeMillis()+1);
		int type;
		//int maxCars = rand.nextInt(5)+6; //From 6 to 11 cars
		int carCount;
		int r=0,c;
		//blockers++;
		board.setRC(GoalR,GoalC,GOALCAR);
		board.setRC(GoalR,GoalC-1,GOALCAR);
		board.setRC(GoalR,GoalC-2,GOALCAR);
		
		carCount=1;
		int j=0;
		while(j<100 && carCount <= maxCars) {
			type=1;
			r = rand.nextInt(Size-1);
			c = rand.nextInt(Size-1);
			int r1 = rand.nextInt(101);
			int r2 = rand.nextInt(101);
			if(r1>carProb) {
				if(r2>verProb) {
					type=HTRUCK;
				}
				else {
					type=VTRUCK;
				}
			}
			else {
				if(r2>verProb) {
					type=HCAR;
				}
				else {
					type=VCAR;
				}
			}
			if(r == GoalR && (type==HCAR || type ==HTRUCK)) {
				//Dont add car 
			}
			else if(addCar(board,r,c,type)) {
				carCount++;
			}
			j++;
		}
		board.setRC(GoalR,GoalC-2,EMPTY);
		return board;
		
	}
	
	/**
	 * @param r Row
	 * @param c Column
	 * @param type Type of car
	 * @return true if car added
	 * R C are top left corner of car to insert
	 * Add car to R,C if room
	 * If no room - car not added return false
	 */
	private boolean addCar(GameBoard board, int r, int c, int type) {
		if(r<0 || c<0) { 
			return false;
		}
		
		if(type == HCAR) {
			if(c+1<=Size-1) {
				if(board.getRC(r,c) ==0 && 0==board.getRC(r,c+1)){
					board.setRC(r,c, HCAR);
					board.setRC(r,c+1, HCAR);
					return true;
				}
			}
		}
		if(type == HTRUCK) {
			if(c+2<=Size-1) {
				if(board.getRC(r,c) ==0 && board.getRC(r,c+2)==0 && board.getRC(r,c+1)==0){
					board.setRC(r,c, HTRUCK);
					board.setRC(r,c+1, HTRUCK);
					board.setRC(r,c+2, HTRUCK);
					return true;
				}
			}
		}
		if(type == VTRUCK) {
			if(r+2<=Size-1) {
				if(board.getRC(r,c) ==0 && board.getRC(r+1,c)==0 && board.getRC(r+2,c)==0){
					board.setRC(r,c, VTRUCK);
					board.setRC(r+1,c, VTRUCK);
					board.setRC(r+2,c, VTRUCK);
					return true;
				}
			}
		}
		
		if(type == VCAR) {
			if(r+1<=Size-1) {
				if(board.getRC(r,c)==0 && board.getRC(r+1,c)==0) {
					board.setRC(r,c, VCAR);
					board.setRC(r+1,c, VCAR);
				}
			}
			
		}
		return false;
	}
	/**
	 * @param r ROw
	 * @param c Column
	 * @return Index of array
	 */
	private int RCtoI(int r, int c) {
		return r * (Size) + c;
	}
	
	
	/**
	 * Print current board
	 */
	public void printBoard() {
		board.printBoard();
	}
	
	
	/**
	 * @param r Row
	 * @param c Column
	 * @return Int[left,up,right,down] moves in each direction
	 * Find distance piece at location r,c can travel in each direction
	 */
	public int[] FindMoves(int r, int c) {
		return board.FindMoves(r, c);
	}

	
	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		Puzzle other = (Puzzle) obj;
		if(initial.equals(other.getInitial())){
			return true;
		}
		return false;
	}
	
	/**
	 * @return Inital board
	 */
	public GameBoard getInitial(){
		return initial;
	}
	
	public int getGoalR() {
		return GoalR;
	}
	
	


}