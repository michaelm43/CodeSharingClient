package Compile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import javafx.scene.control.TextArea;

public class Run extends OutputStream implements Runnable {
	
	private TextArea output;
	private Process p;

	public Run() {
	}

	public Run(TextArea output, Process p) {
		this.output = output;
		this.p = p;
	}

	public void run() {
		try {
			PrintStream out = new PrintStream(this);
			System.setOut(out);
			output.setText("");
			InputStream in = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line1 = reader.readLine();
			while (line1 != null) {
				System.out.print(line1 + "\n");
				line1 = reader.readLine();
			}
			// in.close();
		} catch (Exception e2) {
			System.out.println(e2 + " run");
		}
	}

	public void write(int b) throws IOException {
		output.appendText(String.valueOf((char) b));
	}
}