package com.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class FilmsController {

	@RequestMapping("/")
	public String hello() {
		System.out.println("hitting spring mvc project");
	return "get-films";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String jacobWorld(@RequestParam("name") String name, Model model) {
	String message = "Hi " + name + "!";
	model.addAttribute("message", message);
	return "hello";
	}

	@RequestMapping(value = "/hi", method = RequestMethod.GET)
	public String hi(@RequestParam("name") String name, Model model) {
	String message = "Hi " + name + "!";
	model.addAttribute("message", message);
	return "hi";
	}
	
}
