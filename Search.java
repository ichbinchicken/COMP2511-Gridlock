import java.util.*;




public class Search {
	Map<ArrayList<Integer>,ArrayList<Integer>> closedMap = new HashMap<ArrayList<Integer>,ArrayList<Integer>>();
	Queue<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
	int Cmax = 6;
	int Rmax = 6;
	int numCarPer = 3; //Max number of cars/trucks per row - number of options per row ie 2 cars 1 truck for 6, 3 cars 2 trucks 7
	int GoalC = Cmax-1;
	int GoalR = 2;
	int GoalCar = (Cmax+Rmax-1)*numCarPer+1;
	
	private static final int  VERT =0;
	private static final int  HORIZ =1; //Horizontal sliding

	
	
	private ArrayList<Integer> arr;
	public Search() {
	}

	public void SearchBoard(BoardState state) {
		int j=0;
		arr = state.GetBoard();
		//ArrayList<Integer> nullArr = new ArrayList<Integer>();
		ArrayList<Integer> curr;
		addQueue(arr, null);
		boolean solved = false;
		
		while(!queue.isEmpty()) {
			curr = queue.remove();
			if(isGoal(curr) && !solved) {
				solved = true;
				FindPrev(curr);
				break; //Finished
			}
			FindNeighbour(curr);
			j++;
		}
		int i=1;
		i++;
		
		
	}
	
	
	
	private int FindPrev(ArrayList<Integer> state) {
		ArrayList<Integer> prev = closedMap.get(state);
        int step = (prev == null) ? 0 : FindPrev(prev) + 1;
        System.out.println(step);
        printBoard(state);
        //System.out.println((state));
        return step;

	}
	
	private void printBoard(ArrayList<Integer> state) {
		
		for(int i =0; i<state.size();i++) {
			if(i%Cmax==0) {
			    System.out.print('\n');
			}
		    System.out.format(" %2d", state.get(i));

		}
	    System.out.print('\n');

	}
	
	private void addQueue(ArrayList<Integer> next, ArrayList<Integer> prev) {
		ArrayList<Integer> next1 = new ArrayList<Integer>(next);
		ArrayList<Integer> prev1 = null;
		if(prev!=null) {
			prev1 = new ArrayList<Integer>(prev);
		}

		if(!closedMap.containsKey(next1)) {
			closedMap.put(next1,prev1);
			queue.add(next1);
		}
	}
	
	private boolean isGoal(ArrayList<Integer> state) {
		if(state.get(RCtoI(GoalR,GoalC)) == GoalCar) {
			return true;
		}
		return false;
	}
	
	private void FindNeighbour(ArrayList<Integer> curr) {
		for( int r=0; r < Rmax; r++) {
			for (int c = 0; c<Cmax; c++) {
				int carId = curr.get(RCtoI(r,c));
				if(carId ==0) {
					continue;
				}
				if(IDtoType(carId)== HORIZ ) {
					if(c>0) {
					LeftSpaces(curr,r,c);
					}
					if(c<Cmax-1) {
						RightSpaces(curr,r,c);
					}
				}
					
				else {
					if(r>0) {
					UpSpaces(curr,r,c);
					}
					if(r<Rmax-1) {
					DownSpaces(curr,r,c);
					}
				}
			}
		}
	}
	
	
	
	

	
	private int UpSpaces(ArrayList<Integer> state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		ArrayList<Integer> nState = new ArrayList<Integer>(state);
		
		
		while((r-j >= 0) && state.get(RCtoI(r-j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r-j,c), id); 
			nState.set(RCtoI(r-j+length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	}
	
	
	private int DownSpaces(ArrayList<Integer> state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		ArrayList<Integer> nState = new ArrayList<Integer>(state);
		
		while((r+j < Rmax) && state.get(RCtoI(r+j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r+j,c), id); 
			nState.set(RCtoI(r+j-length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	
	}
	
	private int LeftSpaces(ArrayList<Integer> state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		ArrayList<Integer> nState = new ArrayList<Integer>(state);

		while((c-j >= 0) && state.get(RCtoI(r,c-j))==0){
			nState.set(RCtoI(r,c-j), id); 
			nState.set(RCtoI(r,c-j+length), 0); //Clear end
			addQueue(nState,state);

			j++;
		}
		return j;
	}
	
	
	
	private int RightSpaces(ArrayList<Integer> state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		ArrayList<Integer> nState = new ArrayList<Integer>(state);
		
		while((c+j < Cmax) && state.get(RCtoI(r,c+j))==0){
			nState.set(RCtoI(r,c+j), id);
			nState.set(RCtoI(r,c+j-length),0);
			addQueue(nState,state);

			j++;
		}
		return j;
	}
	
	
	private int RCtoI(int r, int c) {
		return r * Cmax + c;
	}
	
	private int IDtoType(int id) {
		if (id<=Cmax*numCarPer) {
			return VERT;
		}
		return HORIZ;
	}
	
	private int getSize(int id) { //Check this for scaling
		if(id==0) {
			return 0;
		}
		if(id==GoalCar) {
			return 2;
		}
		else if(id%numCarPer==0) { //Truck
			return 3;
		}
		return 2;
	}
	
}
