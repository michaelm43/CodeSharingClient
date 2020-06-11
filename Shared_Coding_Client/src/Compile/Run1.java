package Compile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

import javafx.scene.control.TextArea;

public class Run1 extends OutputStream implements Runnable {
	private TextArea output;
	private Process p;

	public Run1() {
	}

	public Run1(TextArea output, Process p) {
		this.output = output;
		this.p = p;
	}

	public void run() {
		try {
			PrintStream out = new PrintStream(this);
			System.setOut(out);
			output.setText("");
			InputStream in = p.getErrorStream();
			BufferedReader reader_error = new BufferedReader(new InputStreamReader(in));
			String line = reader_error.readLine();
			while (line != null) {
				System.out.print(line + "\n");
				line = reader_error.readLine();
			}
			// in.close();
		} catch (Exception e2) {
			System.out.println(e2 + " run1");
		}
	}

	public void write(int b) throws IOException {
		output.appendText(String.valueOf((char) b));
	}
}
