package Compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;


import javafx.scene.control.TextArea;

public class Comp extends OutputStream implements Runnable {
	private String class_name;
	private TextArea output;
	private Process p;
	private String code;
	private String[] text;

	public Comp(String code, TextArea output) {
		this.code = code;
		this.output = output;
	}

	public void run() {
		try {
			PrintStream out = new PrintStream(this);
			System.setOut(out);
//			String search = "public class ";
//			int offset_p = code.indexOf(search);
//			int space = code.indexOf(" ", offset_p + search.length());
//			int enter = code.indexOf("\n", offset_p + search.length());
//			if (offset_p != -1 && (enter != -1 || space != -1)) {
//				if (enter != -1 && Note.enter != -1) {
					//class_name = code.substring(offset_p + search.length(), enter);
					class_name = "Application";
					FileOutputStream fout = new FileOutputStream("." + "\\" + class_name + ".java");
					PrintStream ps = new PrintStream(fout);
					System.setErr(ps);
//					for (int i = 0; i < tf.getLineCount(); i++) {
						//text = code.getText().split("\\n"); // TODO CHECK?
						text = code.split("\n"); 
//					}
					for (int j = 0; j < text.length; j++)
						System.err.println(text[j]);
					p = Runtime.getRuntime().exec("cmd /c " + "cd .&&" + "javac " + class_name + ".java");
					//Note.space = -1;
//				} else {
//					if (space != -1 && Note.space != -1) {
//						class_name = code.substring(offset_p + search.length(), space);
//						FileOutputStream fout = new FileOutputStream("." + "\\" + class_name + ".java");
//						PrintStream ps = new PrintStream(fout);
//						System.setErr(ps);
//						for (int i = 0; i < tf.getLineCount(); i++) {
//							text = tf.getText().split("\\n");
//						}
//						for (int j = 0; j < text.length; j++)
//							System.err.println(text[j]);
//						p = Runtime.getRuntime().exec("cmd /c " + "cd .&&" + "javac " + class_name + ".java");
//						Note.enter = -1;
//					}
//				}
//			} else {
//
//				class_name = "Test";
//				FileOutputStream fout = new FileOutputStream("." + "\\" + class_name + ".java");
//				PrintStream ps = new PrintStream(fout);
//				System.setErr(ps);
//				for (int i = 0; i < tf.getLineCount(); i++) {
//					text = tf.getText().split("\\n");
//				}
//				for (int j = 0; j < text.length; j++)
//					System.err.println(text[j]);
//				p = Runtime.getRuntime().exec("cmd /c " + "cd .&&" + "javac " + class_name + ".java");
//			}
			p.waitFor();
			BufferedReader reader_error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = reader_error.readLine();
			while (line != null) {
				System.out.println(line + "\n");
				line = reader_error.readLine();
			}
		} catch (Exception e1) {
			System.out.println(e1);
			//System.out.println("getLineCount=" + code.get + " from exception");
		}
	}

	public void write(int b) throws IOException {
		output.appendText(String.valueOf((char) b));
	}
}
