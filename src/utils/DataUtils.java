package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane.ScalableIconUIResource;

import coreservlets.model.Film;
import coreservlets.model.Films;


public class DataUtils extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static void sendDataToWebpage(HttpServletRequest request, HttpServletResponse response, List<Film> films) throws ServletException, IOException {
    	response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    	
        String dataFormat = request.getParameter("dataFormat");
        
        if (dataFormat == null) {
        	dataFormat = "application/json";
        }

        String filmResultOutput = "";

        if ("application/xml".equals(dataFormat)) {   
            response.setContentType(dataFormat);
            response.setHeader("content-type", dataFormat);
            filmResultOutput = convertJavaPOJOToXML(films);
        }
        else if ("text/plain".equals(dataFormat)) {
            response.setContentType(dataFormat);
            response.setHeader("content-type", dataFormat);
            filmResultOutput = convertJavaPOJOToString(films);
            System.out.println(filmResultOutput);
        }
        else {
            response.setContentType("application/json");
            response.setHeader("content-type", "application/json");
            filmResultOutput = convertJavaPOJOToJson(films);
        }
        
        request.setAttribute("films", filmResultOutput);
        
        System.out.println("Successfully retrieved the movies in " + dataFormat + ".");
                
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/formatted-films.jsp");        															
        dispatcher.include(request, response);
    }
    
    public static void noResultsFoundInDatabase(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
    	System.out.println(errorMessage);
        response.setHeader("no-data-found-message", errorMessage);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }
    
    public static String convertJavaPOJOToXML(List<Film> filmList) 
    {
		StringWriter xmlWriter = new StringWriter();
        try
        {
        	Films films = new Films();
        	films.setFilmList(filmList);
        	
        	JAXBContext jaxbContext = JAXBContext.newInstance(Films.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            jaxbMarshaller.marshal(films, xmlWriter);
 
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlWriter.toString();
    }

	public static String convertJavaPOJOToJson(List<Film> films) {
		Gson gson = new Gson();
		return gson.toJson(films);
	}
	
	public static String convertJavaPOJOToString(List<Film> films) {
		String outputString = "";
		for (Film film : films) {
			outputString += film.toString();
		}
		return outputString;
	}

	public static Film parseJSONClientFilmData(HttpServletRequest request) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String jsonRequestData = "";
		
		if(br != null){
			jsonRequestData = br.readLine();
		}
				
		//Convert data into Map for simpler and more efficient (O(1)) access.
		HashMap<String, String> filmMap = new Gson().fromJson(jsonRequestData, HashMap.class);
	
		int filmId = 999;
		// This class is used for both update and insert film, however insert does not supply an id and thus one must be supplied.
		if ((filmMap.get("film_id")) != null) {
			filmId = Integer.parseInt(filmMap.get("film_id"));
		};
		String filmTitle = filmMap.get("title");
		String filmDirector = filmMap.get("director");
		int filmYear = Integer.parseInt(filmMap.get("year"));
		String filmStars = filmMap.get("stars");
		String filmReview = filmMap.get("review");
		
		return new Film(filmId, filmTitle, filmYear, filmDirector, filmStars, filmReview);
	}
}
