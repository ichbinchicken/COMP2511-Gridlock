import java.util.*;
@SuppressWarnings("unused")




public class Search {
	Map<ArrayList<Integer>,NodeState> closedMap = new HashMap<ArrayList<Integer>,NodeState>();
	Queue<NodeState> queue = new LinkedList<NodeState>();
	int Cmax = 6;
	int Rmax = 6;
	int numCarPer = 3; //Max number of cars/trucks per row - number of options per row ie 2 cars 1 truck for 6, 3 cars 2 trucks 7
	int GoalC = Cmax-1;
	int GoalR = 2;
	int GoalCar = (Cmax+Rmax-1)*numCarPer+1;
	int GenC = 1;
	int GenR = 2;
	
	private static final int  VERT =0;
	private static final int  HORIZ =1; //Horizontal sliding

	
	
	//private NodeState arr;
	public Search() {
	}

	

	/**
	 * @param state Finished state
	 * @param movesMin Minimum nubmer of moves
	 * @param movesMax Maximum number of moves
	 * Returns a board with number of moves to solve as close to upper bound movesMax 
	 * If moves < movesMin - no solution with required difficulty
	 */
	public void GenBoard(BoardState state, int movesMin) {
		int j=0;
		ArrayList<Integer> arr = state.GetBoard();
		//arr.add(0); //Append number of moves taken - index of n^2 is number of moves
		NodeState curr = new NodeState(arr,0);
		//NodeStart curr2 = null;
		addQueue(curr,null);
		boolean solved = false;
		while(!queue.isEmpty()) {
			curr = queue.remove();
			curr.incMoves();
			/*if(isGen(curr, movesMin, movesMax)) {
				solved = true;
				//FindPrev(curr);
				printBoard(curr);
				break; //Finished
			}*/
			FindNeighbour(curr);
			j++;		
		}
		if(solved==false) {
		    NodeState gen = FindMoves(curr, movesMin);
		    System.out.println("Max Moves is "+ curr.getMoves() + "\nMoves to Solve " + gen.getMoves());
		    

		    printBoard(gen);
		}
	}
	
	public void SearchBoard(BoardState state) {
		int j=0;
		ArrayList<Integer> arr = state.GetBoard();
		//NodeState nullArr = new NodeState();
		NodeState curr = new NodeState(arr,0);
		addQueue(curr, null);
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
		if(solved==false) {
		    System.out.println("Puzzle Cannot Be Solved");

		}
		
		
	}
	
	
	private NodeState FindMoves(NodeState state, int moves) {
		while(state.getMoves()> moves) {
			state = closedMap.get(state.arr);
		}
		return state;
	}
	
	
	private int FindPrev(NodeState state) {
		NodeState prev = closedMap.get(state.arr);
        int step = (prev == null) ? 0 : FindPrev(prev) + 1;
        System.out.println(step);
        printBoard(state);
        //System.out.println((state));
        return step;

	}
	
	private void printBoard(NodeState state) {
		
		for(int i =0; i<state.size();i++) {
			if(i%Cmax==0) {
			    System.out.print('\n');
			}
		    System.out.format(" %2d", state.get(i));

		}
	    System.out.print('\n');

	}
	
	private void addQueue(NodeState next, NodeState prev) {
		NodeState next1 = copyNodeState(next);
		NodeState prev1 = null;
		if(prev!=null) {
			prev1 = copyNodeState(prev);
		}

		if(!closedMap.containsKey(next1.arr)) {
			closedMap.put(next1.arr, prev1);
			//closedMap.put(next1,prev1);
			queue.add(next1);
		}
	}
	
	private boolean isGoal(NodeState state) {
		if(state.get(RCtoI(GoalR,GoalC)) == GoalCar) {
			return true;
		}
		return false;
	}
	
	private boolean isGen(NodeState state, int moves, int max) {
		if(state.getMoves()>=moves && state.getMoves()<max) {
			if(state.get(RCtoI(GenR,GenC))==GoalCar) {
			
				return true;
			}
		}
		return false;
	}
	
	private void FindNeighbour(NodeState curr) {
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
	
	
	
	

	
	private int UpSpaces(NodeState state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		NodeState nState = copyNodeState(state);
		
		
		while((r-j >= 0) && state.get(RCtoI(r-j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r-j,c), id); 
			nState.set(RCtoI(r-j+length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	}
	
	
	private int DownSpaces(NodeState state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		NodeState nState = copyNodeState(state);
		
		while((r+j < Rmax) && state.get(RCtoI(r+j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r+j,c), id); 
			nState.set(RCtoI(r+j-length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	
	}
	
	private int LeftSpaces(NodeState state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		NodeState nState = copyNodeState(state);

		while((c-j >= 0) && state.get(RCtoI(r,c-j))==0){
			nState.set(RCtoI(r,c-j), id); 
			nState.set(RCtoI(r,c-j+length), 0); //Clear end
			addQueue(nState,state);

			j++;
		}
		return j;
	}
	
	
	
	private int RightSpaces(NodeState state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		NodeState nState = copyNodeState(state);
		
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

	
	public NodeState copyNodeState(NodeState orig) {
		//ArrayList<Integer> arr2 = 
		ArrayList<Integer> next1 = new ArrayList<Integer>(orig.getArr());
		NodeState n= new NodeState(next1,orig.getMoves());
		return n;
	}
	
	 private class NodeState {
		 ArrayList<Integer> arr;
		 int moves=0;
		public NodeState(ArrayList<Integer> arr, int moves) {
			this.arr = arr;
			this.moves = moves;
		}
		
		public int get(int i) {
			return arr.get(i);
		}
		
		public void set(int i, int j) {
			arr.set(i,j);
		}
		
		public int size() {
			return arr.size();
		}
		public void incMoves() {
			moves++;
		}
		public int getMoves() {
			return moves;
		}
		
		public ArrayList<Integer>  getArr(){
			return arr;
		}
		
		@Override
		public int hashCode() {
			return arr.hashCode();
		}

		
	}
}