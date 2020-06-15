package HttpRequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import Logic.Project;
import Logic.User;

public class ElementRequest {	
	
	public static String IP = "192.168.1.22";
//	public static String IP = "192.168.1.229";
	public static String PORT = "8089";
	
	private String baseUrl = "http://" + IP + ":" + PORT + "/elements/";
	
	
	/*
	 * newElement(elementBoundary)
	 * create new file to application with name : fileName + useremail 
	 */
	public boolean openNewFile(User user, Project proj) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + user.getEmail());
			
			Gson gson = new Gson();	
		    String json = gson.toJson(proj); 
		    System.out.println(json);
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			DataOutputStream wr = new DataOutputStream(httpConnection.getOutputStream());
			wr.write(json.getBytes());
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("create new file responseCode : " + responseCode);
		
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
	 * getSpecipicElementUsingId()
	 * open existing file - checks if the file exist and try to open it
	 */
	public Project openExistingFile(User user,String fileName) {
		Project proj= null;
		
		System.out.println("file key = " + fileName);
		
		try {
			URL url = new URL(baseUrl + user.getEmail() + "/" + fileName);
			
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("Open existing file responseCode : " + responseCode);
		
			BufferedReader bufferReader;
			
			//creates a reader buffer
			if (responseCode >199 && responseCode<300) {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
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
			
			Gson gson = new Gson();
			proj = gson.fromJson(content.toString(), Project.class);
		
		} catch (Exception e) {
			
			System.out.println("Error Message");
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
		
		
		return proj;
	}
}
