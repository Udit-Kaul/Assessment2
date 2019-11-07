package assessment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GitHubAssessment {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String inline = "";
		
		//Taking input for the 2 parameters
		  System.out.println("Enter search term-->"); 
		  String searchString=br.readLine(); 
		  System.out.println("Enter release Tag-->");
		  String releaseTag=br.readLine();
		 

		//Getting the repositories matching the search string, sorted by the highest number of stars first
		  URL url = new URL("https://api.github.com/search/repositories?q="+searchString+"&sort=stars&order=desc");
		  HttpURLConnection conn =(HttpURLConnection)url.openConnection();
		  conn.setRequestMethod("GET"); conn.connect(); 
		  
		  Scanner sc; 
		  int responsecode=conn.getResponseCode(); 
		  if(responsecode!=200) 
			  throw new RuntimeException("HttpResponseCode:-->"+responsecode); 
		  else 
			  sc = new Scanner(url.openStream()); 
		  while(sc.hasNext()) { 
			  	inline+=sc.nextLine(); 
		}
			  
		  JsonParser jsonParser=new JsonParser(); JsonObject
		  jsonObj=(JsonObject)jsonParser.parse(inline); JsonArray
		  jsonArray=(JsonArray)jsonObj.get("items");
		  System.out.println("-->"+jsonArray.get(0));
		  
		  jsonObj=(JsonObject)jsonArray.get(0);
		  System.out.println("Details of the top result-->");
		  System.out.println("Name-->"+jsonObj.get("name"));
		  System.out.println("Stars-->"+jsonObj.get("watchers_count"));
		  conn.disconnect();
		  
		 
		  //Getting the releases for the particular repository
		inline = "";
		
		url = new URL("https://api.github.com/repos/"+jsonObj.get("full_name").getAsString()+"/tags");
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		
		responsecode = conn.getResponseCode();
		if (responsecode != 200)
			throw new RuntimeException("HttpResponseCode:-->" + responsecode);
		else
			sc = new Scanner(url.openStream());
		while (sc.hasNext()) {
			inline += sc.nextLine();
		}
		
		sc.close();
		
		 
		jsonArray=(JsonArray)jsonParser.parse(inline);
		jsonObj=(JsonObject) jsonArray.get(0);
		
		if(!jsonObj.get("name").toString().contains(releaseTag)) {
			System.out.println("The release tag refers to an older release or a release which does not exist");
		}
		else
			System.out.println("The release tag refers the latest release");
	
	}
}
