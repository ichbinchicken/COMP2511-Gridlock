import java.util.ArrayList;

public class GameBoard { //Representation of the board
		 private ArrayList<Integer> arr; //Current 
		 private int moves=-1;
		 private int n=6;
		 //int GoalRow;
		 
		 
		public GameBoard(ArrayList<Integer> arr, int moves) {
			this.arr = arr;
			this.moves = moves;
		}
		
		public int get(int i) {
			if(i<arr.size()) {
				return arr.get(i);
			}
			System.out.println("FATAL TRY GET WRONG SIZE");
			return -1;
		}
		
		public void set(int i, int j) {
			if(i<arr.size()) {
				arr.set(i,j);
				return;
			}
			System.out.println("FATAL SET GET WRONG SIZE");
			//return -1;

			
		}
		
		
		public void setRC(int r, int c, int value) {
			int i = RCtoI(r,c);
			set(i,value);
			//arr.set(RCtoI(r,c), value);
		}
		
		public int getRC(int r, int c) {
			int i = RCtoI(r,c);
			return get(i);
			//return arr.get(RCtoI( r,  c));
		}
		
		private int RCtoI(int r, int c) {
			return r * n + c;
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
		
		public ArrayList<Integer> getArr(){
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
		//Old board is previous move
		//This is latest board
		public int[] compareBoard(GameBoard oldB) {
			boolean first=false;
			boolean topLeft=false;
			int type1=0;
			int type2=0;
			int i=0;
			int r=0,c=0,newR=0,newC=0;
			ArrayList<Integer> trarr = new ArrayList<Integer>();
			ArrayList<Integer> tcarr = new ArrayList<Integer>();
			//Find first difference and last difference
			while(i<(arr.size())) {
				type1 = get(i);
				type2 = oldB.get(i);
				if(type1!=type2) {
					int tc=i%(n-1);
					int tr=(int)Math.floor(i/n);
					trarr.add( tr);
					tcarr.add(tc);
					if(first==false) {
						first=true;
						if(type1!=0) {
							topLeft=true;
						}
					}
				}
				i++;
			}
			int size = trarr.size();

			if(!topLeft) {
				r = trarr.get(0);
				c = tcarr.get(0);
			}
			else {
				r = trarr.get(size/2);
				c = tcarr.get(size/2);
			}
			if(trarr.get(0)==trarr.get(size-1)) { //Same row
				newC = c+size/2;
				newR=r;
			}
			else {
				newR = r+size/2;
				newC=c;
			}
			int[] array = {r,c,newR,newC};
			return array;
			//return {r,c,newR,newC};

		}

		
		
		@Override
		public int hashCode() {
			return arr.hashCode();
		}
		
		
		public GameBoard copyGameBoard() {
			ArrayList<Integer> newarr = new ArrayList<Integer>(arr);
			GameBoard n= new GameBoard(newarr,moves);
			return n;
		}

		
	}