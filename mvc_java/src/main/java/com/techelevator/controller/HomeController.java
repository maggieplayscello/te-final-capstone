package com.techelevator.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.techelevator.model.Course;
import com.techelevator.model.courseDAO;



@Controller
public class HomeController {
	
	@Autowired
	private courseDAO courseDao;

	@RequestMapping(path="/")
	public String displayHomePage() {
		return "home";
	}
	
	@RequestMapping(path="/users/{currentUser}/")
	public String displayHomePageUserLoggedIn() {
		return "home";
	}
	
	@RequestMapping(path="/dashboard")
	public String displayDashboard() {
		return "dashboard";
	}
	
	@RequestMapping(path= {"/courseSearch", "/users/{currentUser}/courseSearch"})
	public String displayCourse(@PathVariable(required = false) String currentUser, ModelMap map) {
		List<Course> course = courseDao.getAllCourses();
		map.put("allCourses", course);
		return "courseSearch";
	}
	
	
	@RequestMapping(path="/courseSearchResults")
	public String displayCourseSearch(@RequestParam(required = false) String searchName, @RequestParam(required = false) String searchCity, ModelMap map) {
		List<Course> course = courseDao.searchCourses(searchName, searchCity, map);
		map.put("allCourses", course);
		return "courseSearch";
	}
		
	@RequestMapping(path="/users/{currentUser}/addCourse", method = RequestMethod.GET)
	public String displayAddCourse(@PathVariable("currentUser") String currentUser, ModelMap map) {
		if (!map.containsAttribute("course")) {
			map.put("course", new Course());
		}
		return "addCourse";
	}
	
	@RequestMapping (path = "/users/{currentUser}/addCourse", method = RequestMethod.POST)
	public String submitCourse(@Valid @ModelAttribute Course course, @PathVariable("currentUser") String currentUser, BindingResult result, RedirectAttributes flash) {
		
		flash.addFlashAttribute("course", course);
		
		if (result.hasErrors()) {
			flash.addFlashAttribute(BindingResult.MODEL_KEY_PREFIX + "course", result);
			return "redirect:/addCourse";
		}
		courseDao.addCourseToDatabase(course);
		return "redirect:/addCourseConfirmation";
	}
	
	@RequestMapping (path = "/users/{currentUser}/addCourseConfirmation", method = RequestMethod.GET)
	public String courseConfirmation( @PathVariable("currentUser") String currentUser){
		return "addCourseConfirmation";
	}
	
	
}
	