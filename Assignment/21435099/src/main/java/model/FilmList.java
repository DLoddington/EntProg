package model;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)

@XmlRootElement(namespace = "model")
public class FilmList {
	
	@XmlElement(name = "numFilms")
	private int numFilms;
	
	@XmlElementWrapper(name = "films")
	@XmlElement(name = "film")
	private ArrayList<Film> films;
	
	public FilmList(){
		super();
		this.films = new ArrayList<Film>();
		this.numFilms = 0;
	}
	
	public FilmList(ArrayList<Film> films, int numFilms){
		super();
		this.films = films;
		this.numFilms = numFilms;
	}
	
	public void setList(ArrayList<Film> inputList) {
		this.films = inputList;
	}
	
	public ArrayList<Film> getList(){
		return films;
	}
	
	public int getNumFilms() {
		return numFilms;
	}
	
	public void setNumFilms(int numFilms) {
		this.numFilms = numFilms;
	}

}
