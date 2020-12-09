package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

import coreservlets.model.Film;
import coreservlets.model.Films;


public class DataUtils {
    
    public static void sendDataToWebpage(HttpServletRequest request, HttpServletResponse response, List<Film> films) throws ServletException, IOException {
    	response.setHeader("Cache-control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    	
        String dataFormat = request.getParameter("dataFormat");
        
        if (dataFormat == null) {
        	dataFormat = "application/json";
        }

        String filmResultOutput = "";

        if ("text/xml".equals(dataFormat)) {   
            response.setContentType(dataFormat);
            filmResultOutput = convertJavaPOJOToXML(films);
        }
        else if ("text/plain".equals(dataFormat)) {
            response.setContentType(dataFormat);
            filmResultOutput = convertJavaPOJOToString(films);
        }
        else {
            response.setContentType("application/json");
            filmResultOutput = convertJavaPOJOToJson(films);
        }
        
        request.setAttribute("films", filmResultOutput);
        
        System.out.println(filmResultOutput);
        
        //delete the string below and use outputPage path once method is working.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/formatted-films.jsp");        															
        dispatcher.include(request, response);
    }
    
    public void noResultsFoundInDatabase(HttpServletRequest request, HttpServletResponse response, String errorReason) throws ServletException, IOException {
        String errorMessage = "No data found " + errorReason;
        String dataFormat = request.getParameter("dataFormat");

        if ("text/xml".equals(dataFormat)) {   
            response.setContentType(dataFormat);
        }
        else if ("text/plain".equals(dataFormat)) {
            response.setContentType(dataFormat);
        }
        else {
            response.setContentType(dataFormat);
        }
        
    	String filePath = "/Users/jacobedwards/Desktop/Academic/Portfolio_ /Final Year/Enterprise Programming/eclipse-workspace/DynamicWebProjectMySQLFilmsEclipse/WebContent/WEB-INF/results/no-data-found.jsp";
        PrintWriter pw = new PrintWriter(filePath);
        //clear contents of file before writing to it
        pw.print("");
        
        pw.write(errorMessage);
        pw.close();
        
        //delete the string below and use outputPage path once method is working.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/results/no-data-found.jsp");
        															
        dispatcher.include(request, response);
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
	
		int filmId = Integer.parseInt(filmMap.get("film_id"));
		String filmTitle = filmMap.get("title");
		String filmDirector = filmMap.get("director");
		int filmYear = Integer.parseInt(filmMap.get("year"));
		String filmStars = filmMap.get("stars");
		String filmReview = filmMap.get("review");
		
		return new Film(filmId, filmTitle, filmYear, filmDirector, filmStars, filmReview);
	}
}
