import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;

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
		int cars=13;
		int carProb = 80; 
		int verProb = 60;
		BoardState state = new BoardState(6);
		//state.GenSolution();
		//state.printBoard();
		Search search = new Search();
		ArrayList<Integer> arr=null;
		int j=0;
		int moves;
		//while(arr==null) {
		for(j=0;j<100;j++) {
			state.GenSolution(cars,carProb,verProb);
			moves = search.GenBoard(state,10);
			//j++;
			System.out.println("Puzzle Gen  " + moves +" moves");
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
		int NumDifficulties=5;
		ArrayList<BoundedQueue<NodeState>> queueList = new ArrayList<BoundedQueue<NodeState>>(5);
		BoundedQueue<NodeState> queue=null;
		
		for(int i=0;i<NumDifficulties;i++) {
			queue = new BoundedQueue<NodeState>(10);
			queueList.add(queue);
		}
		int numThreads=5;
		
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);
		for(int i=0;i<numThreads;i++) {
			Runnable run = new GenThread(queueList,10, 6, 2);
			executor.execute(run);
		}
        executor.shutdown();
        while(!executor.isTerminated()) {
        	executor.isTerminated();
        }
        queue = queueList.get(NumDifficulties-1);
        NodeState node = queue.remove();
        node.printBoard();
	}
	
	
/*	@Test
	public void BoardGen() throws FileNotFoundException {
		//System.out.println("DEVBOARD\\\\");
       // PrintWriter writer = new PrintWriter("BoardGen.csv");
		BoardState state = new BoardState(6);
		Search search = new Search();
		ArrayList<Integer> arr=null;
		ArrayList<Integer> result = new ArrayList<Integer>(Collections.nCopies(50, 0));
		int j=0;
		int moves;
		int cars=12;
		int carProb = 80; 
		int verProb = 60;
		//System.out.println(cars+","+ carProb + " ," + verProb +"," + "[0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20]");
		int big10 =0;
		int big15 = 0;
		int big20 = 0,big25=0,big30=0;;
		//writer.println("Cars,CProb,VProb,0,1,2,3,4,5,6,7,8,9");
		//for(cars=6;cars<15;cars++) {
		//	for(carProb=50;carProb<=90;carProb +=10) {
				for(verProb=60;verProb<=90;verProb +=10) {
					big10=0;big15=0;big20=0;big25=0;big30=0;
					for(j=0;j<10000;j++) {
						state.GenSolution(cars,carProb,verProb);
						moves = search.GenBoard(state,0);
						if(moves>=10) {
							big10++;
						}
						if(moves>=15) {
							big15++;
						}
						if(moves>=20) {
							big20++;
						}
						if(moves>=25) {
							big25++;
						}
						if(moves>=30) {
							big30++;
						}
						int get = result.get(moves);
						result.set(moves, get+1);
						
					}
					System.out.println(cars+","+ carProb + " ," + verProb +"," + result);
					//writer.println(result);
					result = new ArrayList<Integer>(Collections.nCopies(50, 0));
					
					System.out.println("\n\nVerProp" + verProb+ " Big10: " + big10 + " Big15: " + big15 + " Big20: " +big20+ " Big25: " +big25+ " Big30: " +big30);
				}
		//	}
			
		//}
		//writer.close();
		//System.out.println("ENDBOARD\\\\");
	}**/
	
	

}