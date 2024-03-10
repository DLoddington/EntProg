package controller;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utils.*;
import model.*;

@WebServlet("/filmapi")
public class FilmAPI extends HttpServlet {
	
	static FilmDAO filmdao = FilmDAO.getInstance();
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException {
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		String requestType = GetUtils.getRequestType(request);
		String dataType = GetUtils.getDateType(request);
		int pageNumber = GetUtils.getPageNumber(request);
		FilmList toSerialize = GetUtils.getFilmList(request, requestType, pageNumber);
		String serialized = GetUtils.serializeByDataType(toSerialize, dataType, response);
		PrintWriter out = response.getWriter();		
		out.write(serialized);
		out.close();
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		//get the type of request e.g. application/json
		String requestType = request.getHeader("Accept");
		
		//get response body
		String dataToParse = PutPostDeleteUtils.getRequestObject(request);	
		
		//get Film to insert from response body & set response content type
		Film toInsert = PutPostDeleteUtils.getFilmFromRequestObject(dataToParse, requestType, response);
		
		//execute insert
		boolean isComplete = filmdao.insertFilm(toInsert);
		
		//generate completion message
		String message;
		if(isComplete)
			message = "Film added succesfully";
		else 
			message = "Failed to add film to database";
		
		Message messageToReturn = new Message(message);	
		
		String toReturn = PutPostDeleteUtils.getReturnMessageSerialized(messageToReturn, requestType, response);
		
		//send completion message
		PrintWriter out = response.getWriter();
		out.write(toReturn);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		//get the type of request e.g. application/json
		String requestType = request.getHeader("Accept");
		
		//get response body
		String dataToParse = PutPostDeleteUtils.getRequestObject(request);	
		
		//get Film to insert from response body & set response content type
		Film toUpdate = PutPostDeleteUtils.getFilmFromRequestObject(dataToParse, requestType, response);
		
		//execute update
		boolean isComplete = filmdao.updateFilm(toUpdate);
		
		//generate completion message
		String message;
		if(isComplete)
			message = "Film updated succesfully";
		else 
			message = "Failed to update film to database";
		
		Message messageToReturn = new Message(message);
		
		String toReturn = PutPostDeleteUtils.getReturnMessageSerialized(messageToReturn, requestType, response);
		
		//send completion message
		PrintWriter out = response.getWriter();
		out.write(toReturn);
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		//get the type of request e.g. application/json
		String requestType = request.getHeader("Accept");
		
		//get response body
		String dataToParse = PutPostDeleteUtils.getRequestObject(request);	
		
		//get Film to insert from response body & set response content type
		Film toDelete = PutPostDeleteUtils.getFilmFromRequestObject(dataToParse, requestType, response);
		
		//execute update
		boolean isComplete = filmdao.deleteFilm(toDelete);
		
		//generate completion message
		String message;
		if(isComplete)
			message = "Film deleted succesfully";
		else 
			message = "Failed to delete film from database";
		
		Message messageToReturn = new Message(message);
		
		String toReturn = PutPostDeleteUtils.getReturnMessageSerialized(messageToReturn, requestType, response);
		
		//send completion message
		PrintWriter out = response.getWriter();
		out.write(toReturn);
	}

}
