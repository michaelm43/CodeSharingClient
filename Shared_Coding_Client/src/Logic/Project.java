package Logic;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Project {
	private String name;
	private String creator;
	private List<Lines> text;
	private Integer lineNumber;
	
	public static List<Lines> initCode= new LinkedList<>(List.of(	
			new Lines("public class", 1), 
			new Lines("{",2),
			new Lines("public static void main()",3),
			new Lines("{",4),
			new Lines("}",5),
			new Lines("}",6))); 
		
	
	public Project(String name, String creator) {
		super();
		this.name = name;
		this.creator = creator;
		this.text = initCode;
		this.lineNumber = initCode.size();
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
	public List<Lines> getText() {
		return text;
	}
	public void setText(List<Lines> text) {
		this.text = text;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	@Override
	public String toString() {
		ListIterator<Lines> itr = text.listIterator();
		String stringText = "";
		
		while(itr.hasNext()) {
			stringText+= itr.next().toString();
		}
		
		return stringText;
	}
}
