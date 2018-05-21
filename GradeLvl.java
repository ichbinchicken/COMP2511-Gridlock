
public enum GradeLvl {
	HD ("High Distinction", 5),
	D ("Distinction", 4),
	C ("Credit", 3),
	P ("Pass", 2),
	PC ("Pass Conceded", 1),
	F ("Fail", 0);
	
	private final int mark;
	private final String s;
	GradeLvl(String s, int mark){
		this.s=s;
		this.mark=mark;
	}
	public int getMark() {
		return mark;
	}
	public String getString() {
		return s;
	}
}
