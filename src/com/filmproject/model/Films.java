package com.filmproject.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "coreservlets")
public class Films {
	
	@XmlElementWrapper(name = "filmList")
    // XmlElement sets the name of the entities
    @XmlElement(name = "film")
	private List<Film> filmList;
	
	public Films() {
		
	}
	
	public List<Film> getFilmList() {
		return filmList;
	}
	
	public void setFilmList(List<Film> filmList) {
		this.filmList = filmList;
	}

	@Override
	public String toString() {
		return "Films [filmList=" + filmList.toString() + "]";
	}
	
}
