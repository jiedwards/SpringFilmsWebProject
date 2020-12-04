package coreservlets.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "coreservlets")
@XmlType(factoryMethod="createInstanceJAXB")
public class Films {
	
	@XmlElementWrapper(name = "filmList")
    // XmlElement sets the name of the entities
    @XmlElement(name = "film")
	private List<Film> filmList;
	
//	satisfies JAXB no-arg constructor requirement, will never be invoked
	private static Films createInstanceJAXB() { 
	      return null;  
		}
	
	public List<Film> getFilmList() {
		return filmList;
	}
	
	public void setFilmList(List<Film> filmList) {
		this.filmList = filmList;
	}


	

}
