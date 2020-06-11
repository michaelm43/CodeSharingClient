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
	
	public static List<Line> initCode= new LinkedList<>(List.of(	
			new Line("public class Application", 1), 
			new Line("{",2),
			new Line("public static void main(String[] args)",3),
			new Line("{",4),
			new Line("System.out.println(100);", 5),
			new Line("}",6),
			new Line("}",7)
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
}
