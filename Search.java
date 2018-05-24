import java.util.*;
@SuppressWarnings("unused")




public class Search {
	private static final int  EMPTY=0;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;

	Map<ArrayList<Integer>,GameBoard> closedMap;
	Queue<GameBoard> queue;
	int Cmax = 6;
	int Rmax = 6;
	int Size = 6;
	int GoalC = Cmax-1;
	int GoalR = 2;
	int GenC = 1;
	int GenR = 2;
	boolean gen=false;

	
	
	//private GameBoard arr;
	public Search(int GoalR, int Size) {
		this.Cmax=Size;
		this.Rmax=Size;
		this.Size=Size;
		GoalC = Size-1;
		this.GoalR= GoalR;
	}

	
	
	public void Clear() {
		closedMap=null;
	}

	/**
	 * @param state Finished state
	 * @param movesMin Minimum nubmer of moves
	 * @param movesMax Maximum number of moves
	 * Returns a board with number of moves to solve as close to upper bound movesMax 
	 * If moves < movesMin - no solution with required difficulty
	 */
	public GameBoard GenBoard(GameBoard board) {
		
		queue =  new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>,GameBoard>();
		Queue<GameBoard> solveList = new LinkedList<GameBoard>();
		ArrayList<Integer> arr = board.getArr();
		GameBoard curr = new GameBoard(arr,-1, Size);
		addQueue(curr,null);
		boolean solved = false;
		//gen=true;
		while(!queue.isEmpty()) {
			curr = queue.remove();
			if(isGoal(curr)) {
				solveList.add(curr);
			}
			//curr.printBoard();

			FindNeighbour(curr);
		}
		queue =  new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>,GameBoard>();
		gen=false;
		while(!solveList.isEmpty()) {
			curr = solveList.remove();
			addQueue(curr,null); //Add to queue and closed map
		}
		while(!queue.isEmpty()) {
			curr = queue.remove();
			curr.incMoves();
			FindNeighbour(curr);
			//curr.printBoard();


		}
		return curr;
	}

	
	public LinkedList<GameBoard> SearchBoard(GameBoard board) {
		queue =  new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>,GameBoard>();

		int j=0;
		GameBoard curr = board;
		addQueue(curr, null);
		boolean solved = false;
		
		while(!queue.isEmpty()) {
			curr = queue.remove();
			curr.incMoves();
			if(isGoal(curr) && !solved) {
				solved = true;
				break; //Finished
			}
			FindNeighbour(curr);
			j++;
		}

		int i=1;
		i++;
		if(solved==true) {
			LinkedList<GameBoard> list = new LinkedList<GameBoard>();
			FindPrev(curr, list);
		   // System.out.println("Puzzle Solved in " + curr.getMoves() +" moves");

			return list;
		}
		else{
		    System.out.println("Puzzle Cannot Be Solved"); //SHOULD NOT HAPPEN
		    return null;
		}
	}
	
	
	private GameBoard FindMoves(GameBoard state, int moves) {
		while(state.getMoves()> moves) {
			state = closedMap.get(state.getArr());
		}
		return state;
	}
	
	
	private int FindPrev(GameBoard state, LinkedList<GameBoard> list) {
		GameBoard prev = closedMap.get(state.getArr());
        int step = (prev == null) ? 0 : FindPrev(prev,list) + 1;
        list.add(state);
        //System.out.println(step);
       // state.printBoard();
        //System.out.println((state));
        return step;

	}
	

	
	private void addQueue(GameBoard next, GameBoard prev) {
		GameBoard next1 = next.copyGameBoard();
		GameBoard prev1 = null;
		if(prev!=null && gen==false) {
			prev1 = prev.copyGameBoard();
		}

		if(!closedMap.containsKey(next1.getArr())) {
			closedMap.put(next1.getArr(), prev1);
			//closedMap.put(next1,prev1);
			queue.add(next1);
		}
	}
	
	private boolean isGoal(GameBoard state) {
		if(state.get(RCtoI(GoalR,GoalC)) == GOALCAR) {
			return true;
		}
		return false;
	}
	private void FindNeighbour(GameBoard curr) {
		for( int r=0; r < Rmax; r++) {
			for (int c = 0; c<Cmax; c++) {

				int carId = curr.get(RCtoI(r,c));
				if(carId ==0) {
					continue;
				}
				if(carId == GOALCAR && gen==true) {
					continue;
				}
				else if(carId== HORTRUCK || carId == HORCAR || carId==GOALCAR) {
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
	
	
	
	
	
	

	
	private int UpSpaces(GameBoard state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
		
		while((r-j >= 0) && state.get(RCtoI(r-j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r-j,c), id); 
			nState.set(RCtoI(r-j+length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	}
	
	
	private int DownSpaces(GameBoard state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
		while((r+j < Rmax) && state.get(RCtoI(r+j,c))==0){ //If in bounds and space below is empty
			//Moving up - add to queue
			nState.set(RCtoI(r+j,c), id); 
			nState.set(RCtoI(r+j-length,c), 0); //Clear end
			addQueue(nState,state);
			j++;
		}
		return j;
	
	}
	
	private int LeftSpaces(GameBoard state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();

		while((c-j >= 0) && state.get(RCtoI(r,c-j))==0){
			nState.set(RCtoI(r,c-j), id); 
			nState.set(RCtoI(r,c-j+length), 0); //Clear end
			addQueue(nState,state);

			j++;
		}
		return j;
	}
	
	
	
	private int RightSpaces(GameBoard state, int r, int c) {
		int j=1;
		int id = state.get(RCtoI(r,c));
		
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
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
	
	/*private int IDtoType(int id) {
		if (id<=Cmax*numCarPer) {
			return VERT;
		}
		return HORIZ;
	}*/
	
	private int getSize(int id) { //Check this for scaling
		if(id==EMPTY) {
			return 0;
		}
		else if(id==VERCAR || id== HORCAR || id == GOALCAR) {
			return 2;
		}
		else if(id==VERTRUCK || id == HORTRUCK) {
			return 3;
		}
		return -1;
	}

	

	

}