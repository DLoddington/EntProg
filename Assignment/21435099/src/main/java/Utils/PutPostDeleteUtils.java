package Utils;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Film;

public class PutPostDeleteUtils {
	
	public static String getRequestObject(HttpServletRequest request) throws IOException {
		StringBuilder sBuilder = new StringBuilder();
	    BufferedReader bReader = request.getReader();
	    String next;
	    while ((next = bReader.readLine()) != null) {
	        sBuilder.append(next);
	    }
		String dataToParse = sBuilder.toString();
		bReader.close();
		return dataToParse;
	}
	
	public static Film getFilmFromRequestObject(String dataToParse, String requestType, HttpServletResponse response) {
		Film toUpdate;
		
		//JSON request
		if(requestType.toLowerCase().contains("json".toLowerCase())) {
			response.setContentType("application/json");
			toUpdate = JsonUtils.deserializeJson(dataToParse);
		}
		
		//XML request
		else if(requestType.toLowerCase().contains("xml".toLowerCase())) {
			response.setContentType("application/xml");
			toUpdate = XmlUtils.desearializeXML(dataToParse);
		}
		
		//text request
		else {
			response.setContentType("text/plain");
			toUpdate = TextUtils.desearializeText(dataToParse);
		}
		
		return toUpdate;
	}
	
	public static String getReturnMessageSerialized(Message messageToParse, String requestType, HttpServletResponse response) {
		String toReturn;
		
		//JSON request
		if(requestType.toLowerCase().contains("json".toLowerCase())) {
			response.setContentType("application/json");
			toReturn = JsonUtils.serializeJson(messageToParse);
		}
		
		//XML request
		else if(requestType.toLowerCase().contains("xml".toLowerCase())) {
			response.setContentType("application/xml");
			toReturn = XmlUtils.serializeXMLMessage(messageToParse);
		}
		
		//text request
		else {
			response.setContentType("text/plain");
			toReturn = messageToParse.getMessage();
		}
		
		return toReturn;
	}

}
