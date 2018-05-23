import java.util.*;


//1 is for vert car
//3 vert truck
//19 horiz car
//21 horiz truck
//34 is red car

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

	public Puzzle(int n, int minMoves, boolean gen) {
		//arr= new ArrayList<Integer>(n*n);
		//ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		this.Size=n;
		GoalC = Size-1;
		Random rand = new Random(System.currentTimeMillis());
		GoalR = rand.nextInt(Size-1);
		int maxCars=30; //13 for 6x6 
		int CarP = 80;
		int VerP = 60;
		GameBoard newBoard=null;
		search=new Search(GoalR,n);
		//while(moves < minMoves) {
		if(minMoves<7) {
			maxCars -= 	rand.nextInt(7);
			CarP = 60;
		}
		if(gen==true) {
			int moves=0;
			//while(initial==null) {
				board = GenSolution(maxCars,CarP,VerP);
				//board.printBoard();
		
				newBoard = search.GenBoard(board, minMoves);
				if(newBoard==null) {
					minInitMoves=0;
					return;
					
				}
				minInitMoves=newBoard.getMoves();
					//System.out.println(moves);
		
				initial = newBoard.copyGameBoard(); //For reset
				board = newBoard;
			//}
		}
		else {
			Random random = new Random();
			GameBoard nB = StoredBoard.values()[random.nextInt(StoredBoard.values().length)].getBoard();
			board = nB.copyGameBoard();
			initial = nB.copyGameBoard();
			minInitMoves=board.getMoves();
			GoalR = board.findGoalCarR();
		}
	}
	
	
	/**
	 * @return [currR,currC,newR,newC]
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

	public void RestartPuzzle() {
		board = initial.copyGameBoard();
		numMoves=0;
		numHints=0;
		
	}
	
	public int getHintsUsed() {
		return numHints;
	}
	
	public int getSize() {
		return Size;
	}
	public int getInitMoves() {
		return minInitMoves;
	}
	
	public ArrayList<Integer> GetBoard(){
		return board.getArr();
	}
	
	//Return isGoal if only move left is red to end
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
	
	public int getMoves() {
		return numMoves;
	}
	
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
	 * @param r
	 * @param c
	 * @param type
	 * @return
	 * R C are top left corner of car to insert
	 */
	private boolean addCar(GameBoard board, int r, int c, int type) {
		if(r<0 || c<0) { 
			return false;
		}
		
		if(type == HCAR) {
			if(c+1<Size-1) {
				if(board.getRC(r,c) ==0 && 0==board.getRC(r,c+1)){
					board.setRC(r,c, HCAR);
					board.setRC(r,c+1, HCAR);
					return true;
				}
			}
		}
		if(type == HTRUCK) {
			if(c+2<Size-1) {
				if(board.getRC(r,c) ==0 && board.getRC(r,c+2)==0 && board.getRC(r,c+1)==0){
					board.setRC(r,c, HTRUCK);
					board.setRC(r,c+1, HTRUCK);
					board.setRC(r,c+2, HTRUCK);
					return true;
				}
			}
		}
		if(type == VTRUCK) {
			if(r+2<Size-1) {
				if(board.getRC(r,c) ==0 && board.getRC(r+1,c)==0 && board.getRC(r+2,c)==0){
					board.setRC(r,c, VTRUCK);
					board.setRC(r+1,c, VTRUCK);
					board.setRC(r+2,c, VTRUCK);
					return true;
				}
			}
		}
		
		if(type == VCAR) {
			if(r+1<Size-1) {
				if(board.getRC(r,c)==0 && board.getRC(r+1,c)==0) {
					board.setRC(r,c, VCAR);
					board.setRC(r+1,c, VCAR);
				}
			}
			
		}
		return false;
	}
	private int RCtoI(int r, int c) {
		return r * (Size) + c;
	}
	
	
	public void printBoard() {
		board.printBoard();
	}
	
	
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
	
	public GameBoard getInitial(){
		return initial;
	}

}