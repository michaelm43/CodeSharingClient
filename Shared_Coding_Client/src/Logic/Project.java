package Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import HttpRequests.ActionRequest;

public class Project {
	private String name;
	private String creator;
	private int numberOfLines;
	private List<String> users;
	private List<ActiveUser> activeUsers;
	private List<Line> linesOfCode;
	//TODO do we need lastEdited
	
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
		this.activeUsers = new ArrayList<ActiveUser>();
		
		this.users.add(creator);
		this.activeUsers.add(new ActiveUser(creator,false,-1));
		
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

	public List<ActiveUser> getActiveUsers() {
		return activeUsers;
	}

	public void setActiveUsers(List<ActiveUser> activeUsers) {
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
	
	public int get2LinesDownFromCaret(int caretLine) {
		if(caretLine < this.numberOfLines-2)
			return caretLine+2;
		else if(caretLine >= this.lockednumber)
			return caretLine-1;
		else  
			return caretLine;

	}
	
	public boolean Lock(int caretLine, User user) {
		int start = get2LinesUpFromCaret(caretLine);
		int end = get2LinesDownFromCaret(caretLine);
		
		
		/*
		if(this.linesOfCode.get(start).isLocked() || this.linesOfCode.get(end).isLocked()) {
			return false;
		}
		else 
			for(int i = start; i <= end; i++) {
				this.linesOfCode.get(i).setLocked(true);
				this.lockednumber++;
			}*/
		System.out.println("LOCK");
		System.out.println(start);
		System.out.println(end-start+1);
		return new ActionRequest().lockLines(user, this, start, end-start+1);

	}
	
	public boolean unLock(int caretLine, User user) {
		int start = get2LinesUpFromCaret(caretLine);
		int end = get2LinesDownFromCaret(caretLine);
		
		/*for(int i=start; i <= end ; i++) {
			this.linesOfCode.get(i).setLocked(false);
			this.lockednumber--;
		}*/
		System.out.println("UNLOCK");
		System.out.println(start);
		System.out.println(end-start+1);
		return new ActionRequest().unlockLines(user, this, start, end-start+1);
	}


	public void setText(int caretLine, String text) {
		
		String [] stringArr = text.split("\n");
		int length = stringArr.length;
		int start = get2LinesUpFromCaret(caretLine);
		int end = get2LinesDownFromCaret(caretLine);
				
		/*
		 * delete locked lines - not relevant cause they changed
		 */
		for(int i = 0 ; i <= (end - start); i++) {
			this.linesOfCode.remove(start);
		}
		
		/*
		 * add all new lines (and the locked lines) to the list
		 */
		for(int i=start, j=0 ; i < length+start ; i++,j++) {
			Line tempLine = new Line(stringArr[j],i);
			linesOfCode.add(i, tempLine);
		}
		
		/*
		 * update all the line numbers
		 */
		for(int i = length+start; i <linesOfCode.size();i++) {
			linesOfCode.get(i).setNumber(i);
		}
		
	}
	
	public String getKey() {
		return this.creator + "-" + this.name;
	}
}