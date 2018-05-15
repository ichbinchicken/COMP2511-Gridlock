import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

@SuppressWarnings("unused")
public class SearchTest {

	/*@Test
	public void simpleBoard() {
		Integer[] array2 ={	19,19,0,0,0,18,
				3,0,0,12,0,18,
				3,34,34,12,0,18,
				3,0,0,12,0,0,
				1,0,0,0,28,28,
				1,0,33,33,33,0};
		
		Search search = new Search();
		BoardState state = new BoardState(6);
		state.GivenBoard(array2);
		int moves = search.SearchBoard(state);
		System.out.println("Puzzle Solved in " + moves +" moves");
		
	}
	
	@Test
	public void testCompBoard() {
		BoardState state = new BoardState(6);
		Integer[] array1 = {21,21,21,10,15,18,
							 1,22,22,10,15,18,
							 1, 0,34,34,15,18,
							25,25, 7, 0, 0, 0,
							 0, 4, 7, 0,28,28,
							 0, 4,31,31,32,32};
		

		
		
		

		Search search = new Search();
		state.GivenBoard(array1);
		int moves = search.SearchBoard(state);
		System.out.println("Puzzle Solved in " + moves +" moves");

		

		
	}
	
	@Test
	public void testCantSolve() {
		Search search = new Search();

		Integer[] array3 ={	01,19,19,21,21,21,
				01,22,22,23,23,23,
				00,34,34,12,15,18,
				00,00,00,12,15,18,
				00,00,00,12,15,18,
				00,31,31,33,33,33 };
		BoardState state = new BoardState(6);
		state.GivenBoard(array3);
		//search.SearchBoard(state);
		int moves = search.SearchBoard(state);
		if(moves>0) {
			System.out.println("Puzzle Solved in " + moves +" moves");
		}
	}
	
	@Test
	public void GenComp() {
		System.out.println("//GEN");
		Search search = new Search();
		Integer[] gen1 = { 	01,04,21,21,21,00,
							01,04,22,22,00,00,
							00,00,00,00,34,34,
							25,25,07,10,15,18,
							28,28,07,10,15,18,
							31,31,32,32,15,18};
		
		BoardState state = new BoardState(6);
		state.GivenBoard(gen1);
		search.GenBoard(state, 20);
		
		System.out.println("ENDGEN\\");
	}
	
	
	@Test
	public void GenCompSimpler() {
		System.out.println("//GEN");

		//1 is for vert car
		//3 vert truck
		//19 horiz car
		//21 horiz truck
		//34 is red car
		Search search = new Search();
		Integer[] gen1 = { 	0,0,0,0,0,1,
							0,0,0,0,0,1,
							0,0,0,0,34,34,
							0,0,3,0,0,0,
							0,0,3,0,0,0,
							0,0,3,19,19,0};
		
		BoardState state = new BoardState(6);
		state.GivenBoard(gen1);
		ArrayList<Integer> arr = search.GenBoard(state,5);
		if(arr!=null) {
			Integer[] search2= new Integer[arr.size()];
			search2 = arr.toArray(search2);
			state.GivenBoard(search2);
			
			int moves = search.SearchBoard(state);
			if (moves>0) {
				System.out.println("Puzzle Solved in " + moves +" moves");
			}
		}
		System.out.println("ENDGEN\\\\");

	}*/
	
	/*@Test
	public void BoardGen() {
		System.out.println("DEVBOARD\\\\");

		BoardState state = new BoardState(6);
		state.GenSolution();
		//state.printBoard();
		Search search = new Search();
		ArrayList<Integer> arr=null;
		int j=0;
		while(arr==null) {
			state.GenSolution();
			arr = search.GenBoard(state,10);
			j++;
		}
		System.out.println("J = "+j);
		if(arr!=null) {
			Integer[] search2= new Integer[arr.size()];
			search2 = arr.toArray(search2);
			state.GivenBoard(search2);
			
			int moves = search.SearchBoard(state);
			if (moves>0) {
				System.out.println("Puzzle Solved in " + moves +" moves");
			}
		}
		System.out.println("ENDBOARD\\\\");

	}*/
	
	@Test
	public void ThreadTester() {
		BoundedQueue<BoardState> queue = new BoundedQueue<BoardState>(10);
		Runnable run1 = new GenThread(queue,5);
		Runnable run2 = new GenThread(queue,5);
	    Thread thread1 = new Thread(run1);
	    Thread thread2 = new Thread(run2);

	    thread1.start();
	    thread2.start();
	      
	    
	    while(!queue.isFull()) {
	    	
	    }
	}

}


