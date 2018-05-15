import java.util.*;



public class BoardState {
	private ArrayList<Integer> arr; 
	public BoardState(int n) {
		//arr= new ArrayList<Integer>(n*n);
		arr = new ArrayList<Integer>(Collections.nCopies(n*n, 0));

	}
	
	public void GivenBoard(Integer[] array) {
		arr = new ArrayList<Integer>(Arrays.asList(array));
	}

	public ArrayList<Integer> GetBoard(){
		return arr;
	}
	
}
