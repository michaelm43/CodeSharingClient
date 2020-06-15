package Compile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import Logic.Project;
import javafx.scene.control.TextArea;

public class Run extends OutputStream implements Runnable {
	private String class_name;
	private TextArea output;
	private Process p;
	private Project proj;
	private String[] text;

	public Run(Project proj, TextArea output) {
		this.proj = proj;
		this.output = output;
	}

	public void run() {
		try {
			PrintStream out = new PrintStream(this);
			System.setOut(out);
			class_name = proj.getName();
			FileOutputStream fout = new FileOutputStream("." + "\\" + class_name + ".java");
			PrintStream ps = new PrintStream(fout);
			System.setErr(ps);
			text = proj.toString().split("\n");
			for (int j = 0; j < text.length; j++) {
				System.err.println(text[j]);
			}
			p = Runtime.getRuntime().exec("cmd /c " + "cd .&&" + "javaw " + class_name + ".java");
			output.setText("");
			InputStream in = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			p.waitFor();
			//BufferedReader reader_error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line + "\n"); // PRINTS THE OUTPUT TO HERE ****
				line = reader.readLine();
			}
			in.close();
		} catch (Exception e1) {
			System.out.println(e1);
		}
	}

	public void write(int b) throws IOException {
		output.appendText(String.valueOf((char) b)); // ****
	}
}
