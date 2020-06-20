package Logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

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
	
	public List<Line> initCode = new LinkedList<>(List.of(	
			new Line("public class ", 1), 
			new Line("{",2),
			new Line("public static void main(String[] args)",3),
			new Line("{",4),
			new Line("System.out.println(100);", 5),
//			new Line("",6),
//			new Line("",7),
//			new Line("",8),
//			new Line("",9),
			new Line("}",6),
			new Line("}",7)
			)); 
		
	
	public Project(String name, String creator) {
		super();
		this.name = name;
		this.creator = creator;
		
		this.linesOfCode = new LinkedList<>();
		this.users = new ArrayList<String>();
		this.activeUsers = new ArrayList<ActiveUser>();
		
		this.users.add(creator);
		this.activeUsers.add(new ActiveUser(creator,false,-1,0));
		
		this.linesOfCode = new LinkedList<>(initCode);
		this.linesOfCode.get(0).setCode(this.linesOfCode.get(0).getCode() + name);
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
		
		if(itr.hasNext())
			stringText+= itr.next().toString();
		
		while(itr.hasNext()) {
			stringText+= "\n" + itr.next().toString() ;
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
		if(itr.hasNext())
			stringText = itr.next().toString();
		
		for(int i = temp+1 ; i<=caretLine+2 && itr.hasNext() ; i++) {
			stringText += "\n" + itr.next().toString();
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
		else 
			return this.numberOfLines-1;

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
		return new ActionRequest().lockLines(user, this, start, end-start+1);

	}
	
	public boolean unLock(User user,int length) {
		
		/*for(int i=start; i <= end ; i++) {
			this.linesOfCode.get(i).setLocked(false);
			this.lockednumber--;
		}*/
		
		return new ActionRequest().unlockLines(user, this, length);
	}


	public void setText(int caretLine, String text) {
		String [] stringArr = text.split("\n");
		this.linesOfCode.get(caretLine).setCode(stringArr[0].toString());
	}
	
	public String getKey() {
		return this.creator + "-" + this.name;
	}
}