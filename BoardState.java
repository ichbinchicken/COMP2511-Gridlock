import java.util.*;


//1 is for vert car
//3 vert truck
//19 horiz car
//21 horiz truck
//34 is red car

public class BoardState {
	private ArrayList<Integer> arr; 
	
	private int n;
	
	int Cmax = 6;
	int Rmax = 6;

	int GoalC = Cmax-1;
	int GoalR = 2;
	

	private static final int  RED =34;
	private static final int  HCAR=19; //Horizontal sliding
	private static final int  HTRUCK =21;
	private static final int  VCAR =1; //Horizontal sliding
	private static final int  VTRUCK =3; //Horizontal sliding

	public BoardState(int n) {
		//arr= new ArrayList<Integer>(n*n);
		arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		this.n=n;

	}
	
	public void GivenBoard(Integer[] array) {
		arr = new ArrayList<Integer>(Arrays.asList(array));
	}

	public ArrayList<Integer> GetBoard(){
		return arr;
	}
	
	
	//In general - hardest solutions roughly ~13, 80, 60
	public void GenSolution(int maxCars, int carProb, int verProb) {

		Random rand = new Random();
		int type;
		//int maxCars = rand.nextInt(5)+6; //From 6 to 11 cars
		int carCount;
		int r=0,c;
		//blockers++;
		arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		arr.set(RCtoI(GoalR,GoalC), RED);
		arr.set(RCtoI(GoalR,GoalC-1), RED);
		arr.set(RCtoI(GoalR,GoalC-2), RED); //The space to left of red car must be empty
		
		carCount=1;
		int j=0;
		while(j<100 && carCount <= maxCars) {
			type=1;
			r = rand.nextInt(5);
			c = rand.nextInt(5);
			int r1 = rand.nextInt(101);
			int r2 = rand.nextInt(101);
			if(r1>carProb) {
				type+=2;
			}
			if(r2>verProb) {
				type+=18;
			}
			if(addCar(r,c,type)) {
				carCount++;
			}
			j++;
		}
		arr.set(RCtoI(GoalR,GoalC-2), 0);
		//arr.set(RCtoI(GoalR,GoalC-3), 0);

		
	}
	
	/**
	 * @param r
	 * @param c
	 * @param type
	 * @return
	 * R C are top left corner of car to insert
	 */
	private boolean addCar(int r, int c, int type) {
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
		for(int i =0; i<arr.size();i++) {
			if(i%(n)==0) {
			    System.out.print('\n');
			}
		    System.out.format(" %2d", arr.get(i));

		}
	    System.out.print('\n');

	}

}