package HttpRequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import Logic.User;

public class UserRequests {

	public static String IP ;//= "192.168.1.22";
//	public static String IP = "192.168.1.229";
	public static String PORT ;
	
	private String baseUrl = "http://" + IP + ":" + PORT + "/";
	
	/*
	 * newUser()
	 * register a new user to the app
	 */
	
	public UserRequests() {
		super();
	}
	
	public UserRequests(String ip, String port) {
		super();
		UserRequests.IP = ip;
		UserRequests.PORT = port;
	}
	
	
	public boolean registerUser(User user) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "users");
			
			Gson gson = new Gson();
		    String json = gson.toJson(user); 
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
			wr.write(json.getBytes());
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("register responseCode : " + responseCode);
		
			BufferedReader bufferReader;
			
			//creates a reader buffer
			if (responseCode >199 && responseCode<300) {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				isRegistered = true;
			}
			else {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
				isRegistered = false;
			}
			
			//To recive the response
			StringBuilder content = new StringBuilder();
			String line;
			while((line = bufferReader.readLine()) != null) 
				content.append(line).append("\n");
			bufferReader.close();
			
			System.out.println(content.toString());
			
		
		} catch (Exception e) {
			
			System.out.println("Error Message");
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
		return isRegistered;
	}
		
	/*
	 * getUser()
	 * login a user to the app
	 */
	public User loginUser(User user) {
		User tempUser = null;
		boolean isExist = false;
		try {
			URL url = new URL(baseUrl + "/users/login/" + user.getEmail() + "/" + user.getPassword());
			
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("login responseCode : " + responseCode);
		
			BufferedReader bufferReader;
			
			//creates a reader buffer
			if (responseCode >199 && responseCode<300) {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				isExist = true;
			}
			else {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
			}
			
			//To recive the response
			StringBuilder content = new StringBuilder();
			String line;
			while((line = bufferReader.readLine()) != null) 
				content.append(line).append("\n");
			bufferReader.close();
			
			System.out.println(content.toString());
			
			if(isExist) {
				Gson gson = new Gson();
				tempUser = gson.fromJson(content.toString(), User.class);
			}
		} catch (Exception e) {
			
			System.out.println("Error Message");
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
		
		return tempUser;
}
	
	
	/*
	 * updateUser()
	 * update password/add to project List
	 */
	public boolean editUser(User user) {
		boolean isUpdated = false;
		try {
			URL url = new URL(baseUrl + "/users/" + user.getEmail());
			
			Gson gson = new Gson();
			String json = gson.toJson(user);
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("PUT");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			
			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
			wr.write(json.getBytes());
			
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("update user responseCode : " + responseCode);
			
		
			
			//creates a reader buffer
			if (responseCode >199 && responseCode<300) {
				//bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				isUpdated = true;
			}
			else {
				isUpdated = false;
			}
			
			//To recive the response
			StringBuilder content = new StringBuilder();
			//while((line = bufferReader.readLine()) != null) 
				//content.append(line).append("\n");
			//bufferReader.close();
			
			System.out.println(content.toString());
		
		} catch (Exception e) {
			
			System.out.println("Error Message");
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
		
		
		
		return isUpdated;
	}
	
}
