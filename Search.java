import java.util.*;
@SuppressWarnings("unused")




public class Search {
	private static final int  EMPTY=0;
	private static final int  GOALCAR=5;
	private static final int  HORCAR=4;
	private static final int  HORTRUCK=3;
	private static final int  VERCAR=1;
	private static final int  VERTRUCK=2;

	//private static final int  VERT =;
	//private static final int  HORI =; //Horizontal sliding

	
	
	Map<ArrayList<Integer>,GameBoard> closedMap;
	Queue<GameBoard> queue;
	int Cmax = 6;
	int Rmax = 6;
	int Size = 6;
	int GoalC = Cmax-1;
	int GoalR = 2;
	//int GoalCar = (Cmax+Rmax-1)*numCarPer+1;
	int GenC = 1;
	int GenR = 2;
	boolean gen=false;

	
	
	//private GameBoard arr;
	/**
	 * @param GoalR Row with goal car
	 * @param Size of search
	 */
	public Search(int GoalR, int Size) {
		this.Cmax=Size;
		this.Rmax=Size;
		this.Size=Size;
		GoalC = Size-1;
		this.GoalR= GoalR;
	}

	
	
	/**
	 * Clear closedMap
	 */
	public void Clear() {
		closedMap=null;
		queue = null;
	}

	
	public GameBoard GenBoardImpr(GameBoard board){
		queue = new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>, GameBoard>();
		Queue<GameBoard> leafNode  = new LinkedList<GameBoard>();
		ArrayList<Integer> arr = board.getArr();
		GameBoard curr = new GameBoard(arr,-1, Size);
		//ArrayList<GameBoard> boardList = new ArrayList<GameBoard>();
		addQueue(curr,null);
		while(!queue.isEmpty()) {
			curr = queue.remove();
			curr.incMoves();
			if(isGoal(curr)) {
				curr.resetMoves();
				
			}
			if(FindNeighbour(curr)) {
				leafNode.add(curr);
				//curr.printBoard();
			}
		}
		GameBoard max = UpdateMoves(leafNode.remove());
		while(!leafNode.isEmpty()) {
			GameBoard temp = UpdateMoves(leafNode.remove());
			//System.out.println("Moves" + temp.getMoves());
			//temp.printBoard();
			if(temp.getMoves()>max.getMoves()) {
				
				max=temp;
			}
		}
		Clear();
		System.out.println("Max Moves" + max.getMoves());
		max.printBoard();
		return max;
		//return boardList;
		//closedMap.
	}
	
	
	
	/**
	 * @param state Finished state
	 * @param movesMin Minimum nubmer of moves
	 * @param movesMax Maximum number of moves
	 * Returns a board with number of moves to solve as close to upper bound movesMax 
	 * If moves < movesMin - no solution with required difficulty
	 */
	//public ArrayList<Integer> GenBoard(BoardState state, int movesMin) {
	public GameBoard GenBoard(GameBoard board, int minMoves) {
		
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
			curr.incMoves();
			if(isGoal(curr)) {
				solveList.add(curr);
			}
			//curr.printBoard();

			FindNeighbour(curr);
		}
		int maxMoves = curr.getMoves();
		if(maxMoves<minMoves) {
			System.out.println("Cant Do Max Gen" + maxMoves + "Req:" + minMoves);
			return null; //Cant generate hard board with this config
		}
		//System.out.println(maxMoves);
	
		queue =  new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>,GameBoard>();
		gen=false;
		while(!solveList.isEmpty()) {
			curr = solveList.remove();
			curr.resetMoves();
			addQueue(curr,null); //Add to queue and closed map
		}
		while(!queue.isEmpty()) {
			curr = queue.remove();
			curr.incMoves();
			FindNeighbour(curr);
		}
		//System.out.println("REAL MAX MOVES"+curr.getMoves());

		
		Clear();
		//System.out.println("NUM MOVES" +curr.getMoves());
		return curr;
	}
	
	

	
	/**
	 * Search Board for minimal solution
	 * @param board Current board location
	 * @return List of board containg solution
	 */
	public LinkedList<GameBoard> SearchBoard(GameBoard board) {
		queue =  new LinkedList<GameBoard>();
		closedMap = new HashMap<ArrayList<Integer>,GameBoard>();

		int j=0;
		//ArrayList<Integer> arr = state.GetBoard();
		//GameBoard nullArr = new GameBoard();
		GameBoard curr = board;
		//GameBoard curr = new GameBoard(arr,-1);
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
	
	
	/**
	 * @param state 
	 * @param moves
	 * @return
	 */
	private GameBoard FindMoves(GameBoard state, int moves) {
		while(state.getMoves()> moves) {
			state = closedMap.get(state.getArr());
		}
		return state;
	}
	
	
	
	
	/**
	 * Find previous board from list and current state
	 * @param state
	 * @param list
	 * @return
	 */
	private int FindPrev(GameBoard state, LinkedList<GameBoard> list) {
		GameBoard prev = closedMap.get(state.getArr());
        int step = (prev == null) ? 0 : FindPrev(prev,list) + 1;
        list.add(state);
        //System.out.println(step);
       // state.printBoard();
        //System.out.println((state));
        return step;

	}
	
	private GameBoard UpdateMoves(GameBoard leaf) {
		GameBoard prev = closedMap.get(leaf.getArr());
		GameBoard max = leaf;
		int maxMoves=leaf.getMoves();
		int newMoveCount=leaf.getMoves();
		while(prev!=null) {

			if(prev.getMoves()==0) { //Goal State
				newMoveCount=0;
			}
			else {
				System.out.println("MOVES"+prev.getMoves());
				prev.printBoard();
				if(prev.getMoves()>newMoveCount) {
					prev.setMoves(newMoveCount);
				}


			}
			if(prev.getMoves()>maxMoves) {
				System.out.println("MOVES"+prev.getMoves());
				prev.printBoard();

				max=prev;
				maxMoves=prev.getMoves();
			}
			newMoveCount++;
			
			prev = closedMap.get(prev.getArr());
			
		}
		return max;
	}
	

	
	/**
	 * Add new board to queue
	 * @param next
	 * @param prev
	 */
	private boolean addQueue(GameBoard next, GameBoard prev) {
		GameBoard next1 = next.copyGameBoard();
		GameBoard prev1 = null;
		if(prev!=null && gen==false) {
			prev1 = prev.copyGameBoard();
		}

		if(!closedMap.containsKey(next1.getArr())) {
			closedMap.put(next1.getArr(), prev1);
			//closedMap.put(next1,prev1);
			queue.add(next1);
			return true;
		}
		return false;
	}
	
	/**
	 * Is board a goal state
	 * @param state
	 * @return
	 */
	/*private boolean isGoal(GameBoard state) {
		if(state.get(RCtoI(GoalR,GoalC)) == GOALCAR) {
			return true;
		}
		return false;
	}*/
	
	//Return isGoal if only move left is red to end
	private boolean isGoal(GameBoard state) {
		int i=GoalC;
		int id = state.getRC(GoalR, i );
		while(id==0) {
			i--;
			id = state.getRC(GoalR,i);
		}
		if(id==GOALCAR) {
			//System.out.println("GOAL STATE");
			return true;
		}
		return false;
	}
	

	/**
	 * Find Neighbouring boards
	 * @param curr
	 * @return 
	 */
	private boolean FindNeighbour(GameBoard curr) {
		boolean leaf=true;
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
						if(LeftSpaces(curr,r,c)) {
							leaf=false;
						}
					}
					if(c<Cmax-1) {
						if(RightSpaces(curr,r,c)) {
							leaf=false;
						}
					}
				}
					
				else {
					if(r>0) {
						if(UpSpaces(curr,r,c)) {
							leaf=false;
						}
					}
					if(r<Rmax-1) {
						if(DownSpaces(curr,r,c)) {
							leaf=false;
						}
					}
				}
			}
		}
		return leaf;
	}
	
	
	
	
	
	

	
	/**
	 * Check Spaces Above piece
	 * @param state board
	 * @param r row
	 * @param c column
	 * @return number of spaces
	 */
	private boolean UpSpaces(GameBoard state, int r, int c) {
		boolean flag=false;
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
		
		while((r-j >= 0) && state.get(RCtoI(r-j,c))==0){ //If in bounds and space above is empty
			//Moving up - add to queue
			nState.set(RCtoI(r-j,c), id); 
			nState.set(RCtoI(r-j+length,c), 0); //Clear end
			if(addQueue(nState,state)) {
				flag=true;
			}

			j++;
		}
		return flag;
	}

	
	/**
	 * Check Spaces below piece
	 * @param state board
	 * @param r row
	 * @param c column
	 * @return number of spaces
	 */
	private boolean DownSpaces(GameBoard state, int r, int c) {
		boolean flag=false;
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
		while((r+j < Rmax) && state.get(RCtoI(r+j,c))==0){ //If in bounds and space below is empty
			//Moving up - add to queue
			nState.set(RCtoI(r+j,c), id); 
			nState.set(RCtoI(r+j-length,c), 0); //Clear end
			if(addQueue(nState,state)) {
				flag=true;
			}

			j++;
		}
		return flag;
	}

	/**
	 * Check Spaces left of piece
	 * @param state board
	 * @param r row
	 * @param c column
	 * @return number of spaces
	 */
	private boolean LeftSpaces(GameBoard state, int r, int c) {
		boolean flag=false;
		int j=1;
		int id = state.get(RCtoI(r,c));
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();

		while((c-j >= 0) && state.get(RCtoI(r,c-j))==0){
			nState.set(RCtoI(r,c-j), id); 
			nState.set(RCtoI(r,c-j+length), 0); //Clear end
			if(addQueue(nState,state)) {
				flag=true;
			}

			j++;
		}
		return flag;
	}
	
	
	/**
	 * Check Spaces right of piece
	 * @param state board
	 * @param r row
	 * @param c column
	 * @return number of spaces
	 */
	private boolean RightSpaces(GameBoard state, int r, int c) {
		boolean flag=false;
		int j=1;
		int id = state.get(RCtoI(r,c));
		
		int length = getSize(id);
		GameBoard nState = state.copyGameBoard();
		
		while((c+j < Cmax) && state.get(RCtoI(r,c+j))==0){
			nState.set(RCtoI(r,c+j), id);
			nState.set(RCtoI(r,c+j-length),0);
			if(addQueue(nState,state)) {
				flag=true;
			}

			j++;
		}
		return flag;
	}
	
	
	/**
	 * Row Column to int in array
	 * @param r
	 * @param c
	 * @return
	 */
	private int RCtoI(int r, int c) {
		return r * Cmax + c;
	}
	

	/**
	 * GetSize of piece by ID type
	 * @param id
	 * @return
	 */
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