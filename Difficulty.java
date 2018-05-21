
public enum Difficulty {
	PHD (18),
	MASTERS (14),
	BACH (9),
	HSC (5),
	SC (2);
	
	/*ULTRAPHD =18;
	PHD =18;
	MASTERS =14;
	BACH =9;
	HSC =5;
	SC =2;
*/
	private final int moves;
	Difficulty(int moves){
		this.moves = moves;
	}
	public int getMoves() { 
		return moves;
	}
}
