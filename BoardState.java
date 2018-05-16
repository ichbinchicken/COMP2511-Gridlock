import java.util.*;


//1 is for vert car
//3 vert truck
//19 horiz car
//21 horiz truck
//34 is red car

public class BoardState {
	private ArrayList<Integer> arr; 
	private int n;
	
	int GoalC = n-1;
	int GoalR = 3;
	

	private static final int carProb =20;
	private static final int verProb =90;

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
	
	public void GenSolution() {
		Random rand = new Random();
		int maxCars = rand.nextInt(5)+6; //From 6 to 11 cars
		int carCount;
		int blockers = rand.nextInt(6); //Must always have at least one vertical car near exit
		int r=0,c;
		blockers++;
		arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));
		arr.set(RCtoI(GoalR,GoalC), RED);
		arr.set(RCtoI(GoalR,GoalC-1), RED);
		arr.set(RCtoI(GoalR,GoalC-2), RED);
		arr.set(RCtoI(GoalR,GoalC-3), RED);

		int type = VCAR;
		if(blockers>3) {
			r=GoalR+1;
			//Type
			int r2 = rand.nextInt(1);
			if(r2==1) {
				type = VTRUCK;
			}
		}
		c=GoalC-blockers%3;
		addCar(r,c,type);
		carCount=2;
		int j=0;
		while(j<100 && carCount < maxCars) {
			type=1;
			r = rand.nextInt()%(n-1);
			c = rand.nextInt()%(n-1);
			int r1 = rand.nextInt()%(100);
			int r2 = rand.nextInt()%100;
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
		arr.set(RCtoI(GoalR,GoalC-3), 0);

		
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
			if(c+1<n) {
				if(arr.get(RCtoI(r,c)) ==0 && 0==arr.get(RCtoI(r,c+1))){
					arr.set(RCtoI(r,c), HCAR);
					arr.set(RCtoI(r,c+1), HCAR);
					return true;
				}
			}
		}
		if(type == HTRUCK) {
			if(c+2<n) {
				if(arr.get(RCtoI(r,c)) ==0 && arr.get(RCtoI(r,c+2))==0 && arr.get(RCtoI(r,c+1))==0){
					arr.set(RCtoI(r,c), HTRUCK);
					arr.set(RCtoI(r,c+1), HTRUCK);
					arr.set(RCtoI(r,c+2), HTRUCK);
					return true;
				}
			}
		}
		if(type == VTRUCK) {
			if(r+2<n) {
				if(arr.get(RCtoI(r,c)) ==0 && arr.get(RCtoI(r+1,c))==0 && arr.get(RCtoI(r+2,c))==0){
					arr.set(RCtoI(r,c), VTRUCK);
					arr.set(RCtoI(r+1,c), VTRUCK);
					arr.set(RCtoI(r+2,c), VTRUCK);
					return true;
				}
			}
		}
		
		if(type == VCAR) {
			if(r+1<n) {
				if(arr.get(RCtoI(r,c))==0 && arr.get(RCtoI(r+1,c))==0) {
					arr.set(RCtoI(r,c), VCAR);
					arr.set(RCtoI(r+1,c), VCAR);
				}
			}
			
		}
		return false;
	}
	private int RCtoI(int r, int c) {
		return r * n + c;
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
