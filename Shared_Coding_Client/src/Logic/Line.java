package Logic;

public class Line {

	private String line;
	private Integer lineNumber;
	private boolean lock;
	private int tabs;
	private Integer start;
	private Integer end;
	
	public Line(String line, Integer lineNumber) {
		super();
		this.line = line;
		this.lineNumber = lineNumber;
		this.lock = false;
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public boolean isLock() {
		return lock;
	}
	public void setLock(boolean lock) {
		this.lock = lock;
	}

	@Override
	public String toString() {
		return lineNumber + "\t" + line +"\n"; 
	}
}
