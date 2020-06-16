package Logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Project {
	private String name;
	private String creator;
	private int numberOfLines;
	private List<String> users;
	private List<String> activeUsers;
	private List<Line> linesOfCode;
	
	//to check how many lines were changed
	private int lockednumber = 0;
	
	public static List<Line> initCode= new LinkedList<>(List.of(	
			new Line("public class Application", 1), 
			new Line("{",2),
			new Line("public static void main(String[] args)",3),
			new Line("{",4),
			new Line("System.out.println(100);", 5),
			new Line("}",6),
			new Line("}",7),
			new Line("8",8),
			new Line("9",9),
			new Line("10",10),
			new Line("11",11),
			new Line("12",12),
			new Line("13",13),
			new Line("14",14)
			)); 
		
	
	public Project(String name, String creator) {
		super();
		this.name = name;
		this.creator = creator;
		
		this.linesOfCode = new LinkedList<>();
		this.users = new ArrayList<String>();
		this.activeUsers = new ArrayList<String>();
		
		this.users.add(creator);
		this.activeUsers.add(creator);
		
		this.linesOfCode = initCode;
		this.numberOfLines = initCode.size();
	}
	
	public Project(Project proj) {
		super();
		this.name = proj.getName();
		this.creator = proj.getCreator();
		
		this.linesOfCode = proj.getLinesOfCode();
		this.users = proj.getUsers();
		this.activeUsers = proj.getActiveUsers();
		
		this.numberOfLines = proj.getNumberOfLines();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public List<String> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<String> activeUsers) {
		this.activeUsers = activeUsers;
	}

	public List<Line> getLinesOfCode() {
		return linesOfCode;
	}

	public void setLinesOfCode(List<Line> linesOfCode) {
		this.linesOfCode = linesOfCode;
	}
	
	public void addLine(int lineNumber, Line text) {
		linesOfCode.set(lineNumber, text);
	}
	
	public Line removeLine(int lineNumber) {
		return linesOfCode.remove(lineNumber);
	}

	@Override
	public String toString() {
		ListIterator<Line> itr = linesOfCode.listIterator();
		String stringText = "";
		
		while(itr.hasNext()) {
			stringText+= itr.next().toString();
		}
		
		return stringText;
	}
	
	public String toString(int caretLine) {
		ListIterator<Line> itr;
		int temp = 0;
		if(caretLine-2 >= 0)
			temp = caretLine -2; 
		
		itr = linesOfCode.listIterator(temp);
		
		String stringText = "";
		
		for(int i = temp ; i<=caretLine+2 && itr.hasNext() ; i++) {
			stringText+= itr.next().toString();
		}
		
		return stringText;
	}


	public int get2LinesUpFromCaret(int caretLine) {
		if(caretLine >= 2) 
			return caretLine-2;
		else 
			return 0;
	}
	
	public int get2LinesDownfromCaret(int caretLine) {
		if(caretLine < this.numberOfLines-2)
			return caretLine+2;
		else 
			return this.numberOfLines-1;

	}
	
	public boolean Lock(int caretLine) {
	}
	
	public void unLock(int caretLine) {
	}


	public List<Line> setText(int caretLine, String text) {
		
		String [] stringArr = text.split("\n");
		int length = stringArr.length;
		int start = 0;;
		List<Line> newText = new LinkedList<>();
		
		/*
		 * check what is the first elem
		 */
		if(caretLine >= 2) {
			start = caretLine - 2;
		}
		
		/*
		 * delete locked lines - not relevant cause they changed
		 */
		for(int i=0;i<this.lockednumber;i++) {
			linesOfCode.remove(i+start);
		}
		
		/*
		 * add all new lines (and the locked lines) to the list
		 */
		for(int i=0 ; i<length;i++) {
			Line tempLine = new Line(stringArr[i],start+i);
			linesOfCode.add(start+i, tempLine);
			newText.add(tempLine);
		}
		
		/*
		 * update all the line numbers
		 */
		for(int i = length+start; i <linesOfCode.size();i++) {
			linesOfCode.get(i).setNumber(i);
		}
		
		System.out.println(this.toString());
		return newText;
	}
	
	public String getKey() {
		return this.creator + "-" + this.name;
	}
}
