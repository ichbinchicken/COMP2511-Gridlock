import java.util.ArrayList;
import java.util.Arrays;

public class GameBoard { //Representation of the board
		 private ArrayList<Integer> arr; //Current 
		 private int moves=-1;
		 private int Size;

		 //int GoalRow;
		 
		 
		public GameBoard(ArrayList<Integer> arr, int moves, int Size) {
			this.arr = arr;
			this.moves = moves;
			this.Size = Size;
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
			return r * Size + c;
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
			
			for(int i =0; i<Size*Size;i++) {
				if(i%Size==0) {
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
			boolean upleft=false;
			int type1=0;
			int type2=0;
			int i=0;
			int r=0,c=0,newR=0,newC=0;
			int direc=0;
			ArrayList<Integer> trarr = new ArrayList<Integer>();
			ArrayList<Integer> tcarr = new ArrayList<Integer>();
			//Find first difference and last difference
			while(i<(arr.size())) {
				type1 = get(i);
				type2 = oldB.get(i);
				if(type1!=type2) {
					
					int tc=i%(Size);
					int tr=(int)Math.floor(i/(Size));
					if(type1==0) {
						//Move up or left
						
					}
					else {
						//Move down or right
					}
					
					//System.out.println("tC" +tc + "tR" + tr);
					trarr.add( tr);
					tcarr.add(tc);
					if(first==false) {
						first=true;
						if(type1==0) {
							upleft=true;
							direc=type2;
						}
						else {
							direc=type1;
						}
					}
				}
				i++;
			}
			
			int size = trarr.size(); //How far move +1
			//if(direc == VCAR || direc== VTRUCK ) {
				if(upleft==true) {
					newR=trarr.get(0);
					newC=tcarr.get(0);
					r=trarr.get(size-1);
					c=tcarr.get(size-1);
					switch(direc) {
					case VCAR: r=r-1; break;
					case VTRUCK: r=r-2; break;
					case HCAR: case GOALCAR: c=c-1;  break;
					case HTRUCK: c=c-2; break;
					
					}
				}
				else {
					r=trarr.get(0);
					c=tcarr.get(0);
					newR=trarr.get(size-1);
					newC=tcarr.get(size-1);
					switch(direc) {
						case VCAR: newR=newR-1; break;
						case VTRUCK: newR=newR-2; break;
						case HCAR: case GOALCAR: newC=newC-1;  break;
						case HTRUCK: newC=newC-2; break;
					}
				}
				int[] array = {newR,newC,r,c};
				return array;
		}
				

		
		
		/**
		 * @param r Top Left Row
		 * @param c Top Left Column
		 * @return Array with spaces in each direction in form {left, up, right,down}
		 */
		public int[] FindMoves(int r, int c) {
			int id = arr.get(RCtoI(r,c));
			int size = getSize(id);
			int cRight = c+(size-1);
			int rDown = r+(size-1);
			boolean[] flags = {false,false,false,false};
			boolean[] goalF = {true,false,true,false};
			boolean[] goalH = {false,true,false,true};
			int[] spaces = {0,0,0,0};
			//Go left
			int j=1;
			while(!Arrays.equals(flags, goalF) && !Arrays.equals(flags, goalH)) {
				switch(id) {
				case HCAR: case HTRUCK: case GOALCAR:
					if(c-j >= 0 && (arr.get(RCtoI(r,c-j))==0) && flags[0]==false) { //LEFT
						spaces[0]++;
					}
					else {
						flags[0]=true;
					}
					if(cRight+j < Size && (arr.get(RCtoI(r,cRight+j))==0) && flags[2] ==false) { //RIGHT
						spaces[2]++;
					}
					else {
						flags[2]=true;
					}
					break;
				case VCAR: case VTRUCK:
					if(r-j >=0 && (arr.get(RCtoI(r-j,c))==0) && flags[1]==false){
						spaces[1]++;
					}
					else {
						flags[1]=true;
					}
					if(rDown+j < Size && arr.get(RCtoI(rDown+j,c))==0 && flags[3]==false){
						spaces[3]++;
					}
					else {
						flags[3]=true;
					}
					break;
				
				
				}
				j++;
			}
			return spaces;
		}
		@Override
		public int hashCode() {
			return arr.hashCode();
		}
		
		
		@Override
		public boolean equals(Object obj) {
			if(obj==null) {
				return false;
			}
			GameBoard other = (GameBoard) obj;
			if(arr.equals(other.getArr())) {
				return true;
			}
			return false;
		}
		
		private int getSize(int id) { //Check this for scaling
			if(id==EMPTY) {
				return 0;
			}
			else if(id==VCAR || id== HCAR || id == GOALCAR) {
				return 2;
			}
			else if(id==VTRUCK || id == HTRUCK) {
				return 3;
			}
			return -1;
		}

		
		public GameBoard copyGameBoard() {
			ArrayList<Integer> newarr = new ArrayList<Integer>(arr);
			GameBoard n= new GameBoard(newarr,moves, Size);
			return n;
		}

		private static final int  EMPTY=0;
		private static final int  GOALCAR =5;
		private static final int  HCAR=4; //Horizontal sliding
		private static final int  HTRUCK =3;
		private static final int  VCAR =1; //Horizontal sliding
		private static final int  VTRUCK =2; //Horizontal sliding
		
	}