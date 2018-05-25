/**
 * GradLock Project
 * @author Michael Hamilton
 * This class represents different levels of game
 */
public enum Difficulty {
	PHD (18),
	MASTERS (14),
	BACH (9),
	HSC (5),
	SC (2);

	private final int moves;

	/**
	 * Constructor of Difficulty
	 * @param moves the number of moves to get goal state
	 * @pre none
	 * @post none
	 */
	Difficulty(int moves){
		this.moves = moves;
	}

	/**
	 * getter method for moves
	 * @return the number of moves
	 */
	public int getMoves() { 
		return moves;
	}
}
