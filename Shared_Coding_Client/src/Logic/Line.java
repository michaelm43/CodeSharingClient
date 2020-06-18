package Logic;

public class Line {

	private int number;
	private String code;
	private boolean locked;
	
	public Line(String line, int lineNumber) {
		super();
		this.code = line;
		this.number = lineNumber;
		this.locked = false;
	}
	
	public Line(Line line) {
		super();
		this.code = line.getCode();
		this.number = line.getNumber();
		this.locked = line.isLocked();
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String line) {
		this.code = line;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int lineNumber) {
		this.number = lineNumber;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public String toString() {
		return code; 
	}
	
	public String toStringWithNumbers() {
		return number + "\t" + code +"\n"; 
	}

}
