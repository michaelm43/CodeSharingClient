package Logic;

public class Line {

	private String line;
	private int lineNumber;
	private boolean lock;
	
	public Line(String line, int lineNumber) {
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
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
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
		return line +"\n"; 
	}
	
	public String toStringWithNumbers() {
		return lineNumber + "\t" + line +"\n"; 
	}

}
