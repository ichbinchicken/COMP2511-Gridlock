import java.util.ArrayList;

public class NodeState {
		 ArrayList<Integer> arr;
		 int moves=-1;
		 int n=6;
		 
		 
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
		public void resetMoves() {
			moves=0;
		}
		
		public ArrayList<Integer>  getArr(){
			return arr;
		}

		public void printBoard() {
			
			for(int i =0; i<n*n;i++) {
				if(i%n==0) {
				    System.out.print('\n');
				}
			    System.out.format(" %2d", arr.get(i));

			}
		    System.out.print('\n');

		}
		
		@Override
		public int hashCode() {
			return arr.hashCode();
		}

		
	}