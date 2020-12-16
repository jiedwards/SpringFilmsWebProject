package com.filmproject.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.validator.NotEmpty;


@XmlRootElement(name = "film")
@XmlType(propOrder = { "id", "title", "year", "director", "stars", "review" })
@Entity
@Table(name="films")
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
   
   private Film() {
   }
   
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name="id")
   int id;
   
   @NotEmpty(message = "Title cannot be empty")
   @Column(name="title")
   String title;
   
   @NotEmpty(message = "Year cannot be empty")
   @Column(name="year")
   int year;
   
   @NotEmpty(message = "Director cannot be empty")
   @Column(name="director")
   String director;
   
   @NotEmpty(message = "Stars cannot be empty")
   @Column(name="stars")
   String stars;
   
   @NotEmpty(message = "Review cannot be empty")
   @Column(name="review")
   String review;

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

// Method created in model instead of utils for simplicity.
public boolean isEmpty() {

	if (title.isEmpty() || title == null) {
		return true;
	} else if (director.isEmpty() || director == null) {
		return true;
	} else if (stars.isEmpty() || stars == null) {
		return true;
	} else if (review.isEmpty() || review == null) {
		return true;
	} else if (year == 0) {
		return true;
	}

    return false;
}

}
