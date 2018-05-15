import java.util.*;

/**
   An action that repeatedly inserts a greeting into a queue.
*/
public class GenThread implements Runnable
{
	private BoundedQueue<BoardState> queue;
	private int count;
   /**
      Constructs the producer object.
      @param aGreeting the greating to insert into a queue
      @param aQueue the queue into which to insert greetings
      @param count the number of greetings to produce
   */
	public GenThread(BoundedQueue<BoardState> queue, int count) {
		this.queue = queue;
		this.count = count;
	}

	public void run(){
		BoardState state = new BoardState(6);
		ArrayList<Integer> arr=null;
		Search search = new Search();

		try{
			int i=1;
			while(i<=count) {
				while(arr==null) {
					state.GenSolution();
					arr = search.GenBoard(state,10);
				}
				if(!queue.isFull()) {
					queue.add(state);
					i++;
				}
	            Thread.sleep((int) (Math.random() * DELAY));         

			}
		}
		catch (InterruptedException exception){
	      }
	}
	   private static final int DELAY = 10;

}
