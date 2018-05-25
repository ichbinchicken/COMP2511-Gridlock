/**
 * GradLock Project
 * @author Michael Hamilton
 * This class represents different levels of grade
 */
public enum GradeLvl {
	HD ("High Distinction", 5),
	D ("Distinction", 4),
	C ("Credit", 3),
	P ("Pass", 2),
	PC ("Pass Conceded", 1),
	F ("Fail", 0);
	
	private final int mark;
	private final String s;

	/**
	 * Constructor of grade level
	 * @param s the msg
	 * @param mark the mark
	 */
	GradeLvl(String s, int mark){
		this.s=s;
		this.mark=mark;
	}

	/**
	 * getter method for marks
	 * @return the number of marks
	 */
	public int getMark() {
		return mark;
	}
	/**
	 * getter method for the msg
	 * @return the msg
	 */
	public String getString() {
		return s;
	}
}
