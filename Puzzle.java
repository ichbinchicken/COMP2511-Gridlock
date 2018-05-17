import java.util.*;


//1 is for vert car
//3 vert truck
//19 horiz car
//21 horiz truck
//34 is red car

public class Puzzle {
	//private ArrayList<Integer> initial;
	//private ArrayList<Integer> current;
	private GameBoard board;
	private GameBoard initial=null;
	private int minInitMoves=0;
	int difficulty;
	Search search = new Search(); //Add parameters here for size etc


	private int n;
	
	int Cmax = 6;
	int Rmax = 6;

	int GoalC = Cmax-1;
	int GoalR = 2;
	

	private static final int  GOALCAR =5;
	private static final int  HCAR=4; //Horizontal sliding
	private static final int  HTRUCK =3;
	private static final int  VCAR =1; //Horizontal sliding
	private static final int  VTRUCK =2; //Horizontal sliding

	public Puzzle(int n, int minMoves) {
		//arr= new ArrayList<Integer>(n*n);
		//ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		this.n=n;
		int moves=0;
		GameBoard newBoard=null;
		while(moves < minMoves) {
			board = GenSolution(13,80,60);
			newBoard = search.GenBoard(board);
			moves=newBoard.getMoves();
			//System.out.println(moves);

		}
		minInitMoves = moves;
		initial = newBoard.copyGameBoard(); //For reset
		GenCarList();
	}
	
	/*public void GivenBoard(Integer[] array) {
		arr = new ArrayList<Integer>(Arrays.asList(array));
	}*/

	public int getInitMoves() {
		return minInitMoves;
	}
	
	public ArrayList<Integer> GetBoard(){
		return board.getArr();
	}
	
	
	public ArrayList<Car> GenCarList(){
		ArrayList<Car> list = new ArrayList<Car>();
		ArrayList<Integer> arr = initial.getArr();
		Car car=null;
		int i=0;
		int r=0;
		int c=0;
		int id;
		while(i<arr.size()) {
			 id = arr.get(i);
			if(id==HCAR) {
				i++;
				c=i%n-1;
				r=(int)Math.floor(i/n);
				car = new Car(r,c, HCAR, 2);
				list.add(car);
			}
			else if(id==HTRUCK) {
				i+=2;
				c=i%n-2;
				r=(int)Math.floor(i/n);
				car = new Car(r,c, HTRUCK, 3);
				list.add(car);

			}
			else if(id==GOALCAR) {
				i++;
				c=i%n-1;
				r=(int)Math.floor(i/n);
				car = new Car(r,c, GOALCAR, 2);
				list.add(car);

			}
			i++;
		}
		i=0;
		int botleft = n*(n-1);
		int col=0;
		int row=0;
		i=0;
		while(i<arr.size()) {
			id = arr.get(i);
			if(id==VCAR) {
				row++;
				c=i%n;
				r=(int)Math.floor(i/n);
				car = new Car(r,c, VCAR, 2);
				list.add(car);

			}
			else if(id==VTRUCK) {
				row+=2;
				c=i%n;
				r=(int)Math.floor(i/n);
				car = new Car(r,c, VTRUCK, 3);
				list.add(car);

			}
			row++;
			if(row>=n) {
				row=0;
				col++;
			}
			if(col==n) {
				return list;
			}
			i=RCtoI(row,col);
		}

		
		return list;
	}
		
		
	
	
	
	
	
	//In general - hardest solutions roughly ~13, 80, 60
	public GameBoard GenSolution(int maxCars, int carProb, int verProb) {
		Random rand = new Random();
		int type;
		//int maxCars = rand.nextInt(5)+6; //From 6 to 11 cars
		int carCount;
		int r=0,c;
		//blockers++;
		ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		arr.set(RCtoI(GoalR,GoalC), GOALCAR);
		arr.set(RCtoI(GoalR,GoalC-1), GOALCAR);
		arr.set(RCtoI(GoalR,GoalC-2), GOALCAR); //The space to left of red car must be empty
		
		carCount=1;
		int j=0;
		while(j<100 && carCount <= maxCars) {
			type=1;
			r = rand.nextInt(5);
			c = rand.nextInt(5);
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
			if(addCar(arr,r,c,type)) {
				carCount++;
			}
			j++;
		}
		arr.set(RCtoI(GoalR,GoalC-2), 0);
		GameBoard board = new GameBoard(arr,-1);
		return board;
		
	}
	
	/**
	 * @param r
	 * @param c
	 * @param type
	 * @return
	 * R C are top left corner of car to insert
	 */
	private boolean addCar(ArrayList<Integer> arr, int r, int c, int type) {
		if(r<0 || c<0) { 
			return false;
		}
		
		if(type == HCAR) {
			if(c+1<Cmax-1) {
				if(arr.get(RCtoI(r,c)) ==0 && 0==arr.get(RCtoI(r,c+1))){
					arr.set(RCtoI(r,c), HCAR);
					arr.set(RCtoI(r,c+1), HCAR);
					return true;
				}
			}
		}
		if(type == HTRUCK) {
			if(c+2<Cmax-1) {
				if(arr.get(RCtoI(r,c)) ==0 && arr.get(RCtoI(r,c+2))==0 && arr.get(RCtoI(r,c+1))==0){
					arr.set(RCtoI(r,c), HTRUCK);
					arr.set(RCtoI(r,c+1), HTRUCK);
					arr.set(RCtoI(r,c+2), HTRUCK);
					return true;
				}
			}
		}
		if(type == VTRUCK) {
			if(r+2<Rmax-1) {
				if(arr.get(RCtoI(r,c)) ==0 && arr.get(RCtoI(r+1,c))==0 && arr.get(RCtoI(r+2,c))==0){
					arr.set(RCtoI(r,c), VTRUCK);
					arr.set(RCtoI(r+1,c), VTRUCK);
					arr.set(RCtoI(r+2,c), VTRUCK);
					return true;
				}
			}
		}
		
		if(type == VCAR) {
			if(r+1<Rmax-1) {
				if(arr.get(RCtoI(r,c))==0 && arr.get(RCtoI(r+1,c))==0) {
					arr.set(RCtoI(r,c), VCAR);
					arr.set(RCtoI(r+1,c), VCAR);
				}
			}
			
		}
		return false;
	}
	private int RCtoI(int r, int c) {
		return r * (Cmax) + c;
	}
	
	
	public void printBoard() {
		board.printBoard();
	}

}