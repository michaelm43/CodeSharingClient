package Compile;

import java.io.OutputStream;
import java.io.PrintStream;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;

public class Run2 implements Runnable {
	private String input = "";
	private TextArea output;
	private Process p;
	private PrintStream ps;
	private OutputStream oo;

	public Run2() {
	}

	public Run2(TextArea output, Process p) {
		this.output = output;
		this.p = p;
	}

	public void run() {
		try {
			output.setText("");
			output.setOnKeyReleased(new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					try {
						String chr = e.getCharacter();
						oo = p.getOutputStream();
						ps = new PrintStream(oo);
						if (e.getCode().equals(KeyCode.ENTER)) {
							ps.println(input);
							input = "";
							oo.flush();
						} else if (e.getCode().equals(KeyCode.BACK_SPACE)) {
							int len = input.length();
							char ch[] = new char[len - 1];
							for (int i = 0; i < ch.length; i++)
								ch[i] = input.charAt(i);
							input = new String(ch);
						} else if (chr.charAt(0) >= 32 && chr.charAt(0) <= 126) { // if its a compatible char
							//if (chr.charAt(0)  >= 37 && chr.charAt(0)  <= 40)
								//input = input;
							//else
								input = input + chr;
						} else {
							input = input; // if its not, don't add it to input
						}
					} catch (Exception e12) {
						System.out.println("Input is not allowed !!");
					}
				}
			});
			ps = new PrintStream(p.getOutputStream());
		} catch (Exception e2) {
			System.out.println(e2 + " run2");
		}
	}

}
