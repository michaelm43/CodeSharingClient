package Logic;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class User {

	private String email;
	private String password;
	private List<String> projects;
	
	
	public User(String email,String password) {
		super();
		if(isValidEmail(email)&&isValidPassword(password)) {
			this.email = email;
			this.password = password;
		}
		projects = new LinkedList<String>();
	}


	public String getEmail() {
		return email;
	}

	public boolean setEmail(String email) {
		if(isValidEmail(email)) {
			this.email = email;
			return true;
		}
		return false;
	}

	public String getPassword() {
		return password;
	}

	public boolean setPassword(String password) {
		if(isValidPassword(password)) {
			this.password = password;
			return true;
		}
		return false;
	}

	public List<String> getProjectList() {
		return projects;
	}

	
	public void addProject(String proj) {
		projects.add(proj);
	}

	public boolean removeProject(String proj) {
		return projects.remove(proj);
	}
	
	
	public String toString() {
		return "[u-" + email + ",p-" + password +"]";
	}
	
	//check comit
	
	/* 
	 * check that the email is from the pattern: email@gmail.com
	 */
	public static boolean isValidEmail(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
	
	
	/*
	 * check that the password is from the pattern:
	 * 		 Minimum eight characters, at least one letter and one number
	 */
	public static boolean isValidPassword(String psw) {
		String pswRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,12}$";
		Pattern pat = Pattern.compile(pswRegex);
		if(psw == null)
			return false;
		return pat.matcher(psw).matches();
	}
}
