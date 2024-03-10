package Utils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Film;
import model.FilmDAO;
import model.FilmList;

public class GetUtils {
	
	static FilmDAO filmdao = FilmDAO.getInstance();
	
	//define the type of get request e.g. by id, by name, all films
	public static String getRequestType(HttpServletRequest request) {
		
		String requestType = null;
		
		if(request.getParameter("id") != null)
			requestType = "id";
		
		if(request.getParameter("title") != null)
			requestType = "title";
		
		if(request.getParameter("q") != null)
			requestType = "all";
			
		return requestType;
	}
	
	public static int getPageNumber(HttpServletRequest request) {
		
		int pageNumber = Integer.parseInt(request.getParameter("pg"));
		
		return pageNumber;
	}
	
	//define the data type of request e.g json, xml
	public static String getDateType(HttpServletRequest request) {
		
		String dataType = null;
		String accept = request.getHeader("Accept");
		
		// JSon response
		if (accept.toLowerCase().contains("application/json".toLowerCase()))
			dataType = "json";

		// XML Response
		if (accept.toLowerCase().contains("application/xml".toLowerCase()))
			dataType = "xml";

		// text Response
		if (accept.toLowerCase().contains("text/plain".toLowerCase()))
			dataType = "text";
		
		return dataType;
	}
	
	public static FilmList getFilmList(HttpServletRequest request, String requestType, int pageNumber) {
		FilmList toSerialize = new FilmList();
		
		//id search (no need for page number, should return 1 value)
		if(requestType.toLowerCase().equals("id".toLowerCase())) {
			int id = Integer.parseInt(request.getParameter("id"));
			ArrayList<Film> f = filmdao.getFilmByID(id);
			toSerialize.setList(f);
			toSerialize.setNumFilms(1);
		}
		
		//title search
		if(requestType.toLowerCase().equals("title".toLowerCase())) {
			String title = request.getParameter("title");
			ArrayList<Film> f = filmdao.searchFilmByTitle(title, pageNumber);
			int count = filmdao.getSearchFilmsCount(title);
			toSerialize.setList(f);
			toSerialize.setNumFilms(count);
		}
		
		if(requestType.toLowerCase().equals("all".toLowerCase())) {
			ArrayList<Film> f = filmdao.getAllFilms(pageNumber);
			int count = filmdao.getAllFilmsCount();
			toSerialize.setList(f);
			toSerialize.setNumFilms(count);
		}
		
		return toSerialize;
	}
	
	//serialize by appropriate data type and set response headers
	public static String serializeByDataType(FilmList inputList, String dataType, HttpServletResponse response) {
		String toReturn = null;
		
		//json
		if(dataType.toLowerCase().equals("json".toLowerCase())) {
			toReturn = JsonUtils.serializeJson(inputList);
			response.setContentType("application/json");
		}
		
		//xml
		if(dataType.toLowerCase().equals("xml".toLowerCase())) {
			toReturn = XmlUtils.serializeXML(inputList);
			response.setContentType("application/xml");
		}
		
		//text
		if(dataType.toLowerCase().equals("text".toLowerCase())) {
			toReturn = TextUtils.serializeTextFilmList(inputList);
			response.setContentType("text/plain");
		}
		
		return toReturn;
	}

}
