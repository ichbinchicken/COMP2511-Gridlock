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
					
					int tc=i%(n);
					int tr=(int)Math.floor(i/(n));
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
						
				/*	}
					if(direc==VCAR||direc==VTRUCK) {
						r = newR+(size/2);
						c = newC;
					}
					else {
						c = newC+(size/2);
						r = newR;
					}
					//r = trarr.get(size/2);
					//c= tcarr.get(size/2);
				}
				else {
					r=trarr.get(0);
					c=tcarr.get(0);
					if(direc==VCAR||direc==VTRUCK) {
						newR = r-(size/2);
						c=newC;
					}
					else {
						newC = c-(size/2);
						r=newR;
					}*/
					//newR=trarr.get(size/2);
					//newC=tcarr.get(size/2);
				//}
			//}
			//else {
			//	if(upleft=true) {
					
				//}
			//}
			/*if(!topLeft) {
				r = trarr.get(0);
				c = tcarr.get(0);
			}
			else {
				r = trarr.get(size/2);
				c = tcarr.get(size/2);
			}
			if(trarr.get(0)==trarr.get(size-1)) { //Same row
				if(topLeft) {
					newC=c-size/2;
				}
				else {
					newC = c+size/2;
				}
				newR=r;
			}
			else {
				if(topLeft) {
					newR=r-size/2;
				}
				else {
				newR = r+size/2;
				}
				newC=c;
			}*/
			//return {r,c,newR,newC};

		//}

		
		
		@Override
		public int hashCode() {
			return arr.hashCode();
		}
		
		
		public GameBoard copyGameBoard() {
			ArrayList<Integer> newarr = new ArrayList<Integer>(arr);
			GameBoard n= new GameBoard(newarr,moves);
			return n;
		}

		private static final int  EMPTY=0;
		private static final int  GOALCAR =5;
		private static final int  HCAR=4; //Horizontal sliding
		private static final int  HTRUCK =3;
		private static final int  VCAR =1; //Horizontal sliding
		private static final int  VTRUCK =2; //Horizontal sliding
		
	}