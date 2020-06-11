package Logic;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class File {
	private String name;
	private String creator;
	private String text;
	private Integer lineNumber;
	

	public File(String name, String creator) {
		super();
		this.name = name;
		this.creator = creator;
		initEncodeText();
		this.lineNumber = 6;
	}
	
	/*
	 * init decode
	 */
	private void initDecodeText() {
		this.text = text.concat("public class Application\n")
			.concat("{\n")
			.concat("public static void main(String[] args)\n")
			.concat("{\n")
			.concat("System.out.println(100);\n")
			.concat("}\n")
			.concat("}\n");
		//have to encode in the end
		Encoding.encode(this.text);
		}
	
	/*
	 * init encode
	 * %0A - new line
	 * %7B - {
	 * + - space
	 * %28 - (
	 * %29 - )
	 * %7D - }
	 */
	private void initEncodeText() {
		this.text = "public+class%0A%7B%0Apublic+static+void+main%28%29%0A%7B%0A%7D%0A%7D%0A";
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
	public String getText() {
		return Encoding.decode(this.text);
	}
	public void setText(String text) {
		this.text = Encoding.encode(text);
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	@Override
	public String toString() {
		return text;
	}
}
