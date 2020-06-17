 package HttpRequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import Logic.Action;
import Logic.Project;
import Logic.User;

public class ActionRequest {
	
	public static String IP = "192.168.1.22";
//	public static String IP = "192.168.1.229";
	public static String PORT = "8089";
	
	private String baseUrl = "http://" + IP + ":" + PORT + "/";
	
	
	public boolean addNewUser(User user, Project proj, String email) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("add-new-user", proj.getCreator(), proj.getName(), user.getEmail());
			
			action.getProperties().put("newUser", email);
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
	
	public boolean lockLines(User user, Project proj, int start, int count) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("lock", proj.getCreator(), proj.getName(), user.getEmail());
			
			action.getProperties().put("start", start);
			action.getProperties().put("count", count);
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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

	public boolean unlockLines(User user, Project proj, int length) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("unlock", proj.getCreator(), proj.getName(), user.getEmail());
			
			action.getProperties().put("length", length);
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
	
	public Project editCode(User user, Project proj,String txt) {
		boolean isRegistered = false;
		Project newProj = null;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("edit-code", proj.getCreator(), proj.getName(), user.getEmail());
			
			action.getProperties().put("code", txt);
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
			
			if(isRegistered) {
				newProj = gson.fromJson(content.toString(), Project.class);
			}
			
			System.out.println(content.toString());
			
		
		} catch (Exception e) {
			
			System.out.println("Error Message");
			System.out.println(e.getClass().getSimpleName());
			System.out.println(e.getMessage());
		}
		return newProj;
	}

	public boolean deleteProject(User user, Project proj) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("delete", proj.getCreator(), proj.getName(), user.getEmail());
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
	
	public boolean loginProject(User user, Project proj) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("login", proj.getCreator(), proj.getName(), user.getEmail());
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
	
	public boolean logoutProject(User user, Project proj) {
		boolean isRegistered = false;
		try {
			
			URL url = new URL(baseUrl + "actions");
			
			Action action = new Action("logout", proj.getCreator(), proj.getName(), user.getEmail());
			
			Gson gson = new Gson();	
		    String json = gson.toJson(action); 
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
			System.out.println("responseCode : " + responseCode);
		
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
}