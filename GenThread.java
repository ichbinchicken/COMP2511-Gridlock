import java.util.*;

/**
   An action that repeatedly inserts a greeting into a queue.
*/
public class GenThread implements Runnable
{
	ArrayList<BoundedQueue<NodeState>> queueList;
	private BoundedQueue<NodeState> queue=null;
	private int count;
	private static final int  PHD =18;
	private static final int  MASTERS =14;
	private static final int  BACH =9;
	private static final int  HSC =5;
	private static final int  SC =2;
	int size=6;
	int minMoves=SC;


   /**
      Constructs the producer object.
      @param aGreeting the greeting to insert into a queue
      @param aQueue the queue into which to insert greetings
      @param count the number of greetings to produce
   */
	public GenThread(ArrayList<BoundedQueue<NodeState>> queueList, int count, int size, int minMoves) {
		this.queueList = queueList;
		this.count = count;
		this.size=size;
		this.minMoves=minMoves;
	}

	private boolean queueListFull() {
		for(BoundedQueue<NodeState> q: queueList) {
			if(q.isFull()==false) {
				return false;
			}
		}
		return true;
	}
	
	
	public void run(){
		BoardState state = new BoardState(size);
		//ArrayList<Integer> arr=null;
		Search search = new Search();
		NodeState node=null;
		int moves=0;
		if(minMoves<SC) {
			minMoves=SC;
		}

		try{
			int i=1;
			while(i<=count) {
				//arr=null;
				moves=0;
				while(moves<minMoves) {
					state.GenSolution(13,80,60);
					node = search.GenBoard(state,minMoves);
					if(node!=null) {
						moves=node.getMoves();
					}
				}
				if(moves>=PHD) {
					queue = queueList.get(4);
				}
				else if(moves>=MASTERS) {
					queue = queueList.get(3);
				}				
				else if(moves>=BACH) {
					queue = queueList.get(2);
				}
				else if(moves>=HSC) {
					queue = queueList.get(1);
				}
				else {
					queue = queueList.get(0);
				}
				if(!queue.isFull()) {
					queue.add(node);
					System.out.println(moves);
					i++;
				}
				if(queueListFull()){
					return;
				}
	            Thread.sleep((int) (Math.random() * DELAY));         

			}
		}
		catch (InterruptedException exception){
	      }
	}
	   private static final int DELAY = 10;

}
