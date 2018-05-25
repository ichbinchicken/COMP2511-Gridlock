/**
 * GradLock Project
 * @author Michael Hamilton
 * Contains all possible grade levels for maps
 * Numbers from highest grade to lowest grade
 * String for each grade name

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
	 * @param s Grade name
	 * @param mark Grade mark
	 */
	GradeLvl(String s, int mark){
		this.s=s;
		this.mark=mark;
	}

	/**
	 * getter method for marks
	 * @return Grade Mark
	 */
	public int getMark() {
		return mark;
	}

	/**
	 * getter method for the grade name
	 * @return Grade name
	 */
	public String getString() {
		return s;
	}
}
