package coreservlets.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name = "customer")
@XmlType(propOrder = { "id", "title", "year", "director", "stars", "review" } , factoryMethod="createInstanceJAXB")
public class Film {
   public Film(int id, String title, int year, String director, String stars,
               String review) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.stars = stars;
		this.review = review;
	}
   
   int id;
   String title;
   int year;
   String director;
   String stars;
   String review;
   
   //satisfies JAXB no-arg constructor requirement, will never be invoked
   private static Film createInstanceJAXB() { 
       return null;  
   }

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public int getYear() {
	return year;
}
public void setYear(int year) {
	this.year = year;
}
public String getDirector() {
	return director;
}
public void setDirector(String director) {
	this.director = director;
}
public String getStars() {
	return stars;
}
public void setStars(String stars) {
	this.stars = stars;
}
public String getReview() {
	return review;
}
public void setReview(String review) {
	this.review = review;
}
@Override
public String toString() {
	return id + "#" + title + "#" + year
			+ "#" + director + "#" + stars + "#"
			+ review + "\n";
	}   
}
