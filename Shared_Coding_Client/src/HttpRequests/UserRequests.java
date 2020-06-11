package HttpRequests;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

import Logic.User;
/*
import org.json.simple.*;

import org.json.*;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;*/


public class UserRequests {

//	public static String IP = "10.0.1.26";
	public static String IP = "192.168.1.229";
	public static String PORT = "8089";
	
	private String baseUrl = "http://" + IP + ":" + PORT + "/";
	
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
		
	public Boolean loginUser(User user) {
		boolean isExist = false;
		try {
			URL url = new URL(baseUrl + "/users/login/" + user.getEmail());
			
						
			///URL and parameters for the connection.
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("GET");
			httpConnection.setRequestProperty("Content-Type", "application/json");
			httpConnection.setRequestProperty("Accept", "application/json");
			
			
			Integer responseCode = httpConnection.getResponseCode();
			System.out.println("responseCode : " + responseCode);
		
			BufferedReader bufferReader;
			
			//creates a reader buffer
			if (responseCode >199 && responseCode<300) {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				isExist = true;
			}
			else {
				bufferReader = new BufferedReader(new InputStreamReader(httpConnection.getErrorStream()));
				isExist = false;
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
		return isExist;
}
	
	
	//TODO put User!
	
}
