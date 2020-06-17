package Compile;

public class Shutdown implements Runnable {
	public void run() {
		try {
			Runtime.getRuntime().exec("cmd /c " + "del *.class");
			Runtime.getRuntime().exec("cmd /c " + "del *.java"); 
			Runtime.getRuntime().exec("cmd /c " + "exit");
		} catch (Exception ee) {
			System.out.println(ee);
		}
	}
}
