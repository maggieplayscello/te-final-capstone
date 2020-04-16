package com.techelevator.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.techelevator.model.Course.Course;
import com.techelevator.model.Course.CourseDAO;
import com.techelevator.model.League.League;
import com.techelevator.model.League.LeagueDAO;
import com.techelevator.model.Score.ScoreDAO;
import com.techelevator.model.Team.Team;
import com.techelevator.model.Team.TeamDAO;
import com.techelevator.model.TeeTime.TeeTime;
import com.techelevator.model.TeeTime.TeeTimeDAO;
import com.techelevator.model.User.User;
import com.techelevator.model.User.UserDAO;

@Controller
public class MyLeaguesController {

	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private CourseDAO courseDao;
	
	@Autowired
	private LeagueDAO leagueDao;
	
	@Autowired
	private ScoreDAO scoreDao;
	
	@Autowired
	private TeeTimeDAO teeTimeDao;
	
	@Autowired
	private TeamDAO teamDao;
	
	@RequestMapping(path="/users/{currentUser}/myLeagues", method=RequestMethod.GET)
	public String loadMyLeaguesPage(@PathVariable("currentUser") String currentUser, 
			@RequestParam(required = false) String leagueName, ModelMap map){
		List<Team> teams = teamDao.getTeamsByLeagueId(leagueDao.getLeagueIdByLeagueName(leagueName));
		
		map.put("leagueName", leagueName);
		map.put("teams", teams);
		List<Course> course = courseDao.getAllCourses();
		map.put("allCourses", course);
		List<League> league = leagueDao.getAllLeaguesByUserId(userDao.getIdByUserName(currentUser));
		map.put("allLeagues", league);
		List<League> ownedLeagues = leagueDao.getAllLeaguesByOwner(userDao.getIdByUserName(currentUser));
		map.put("ownedLeagues", ownedLeagues);
		List<User> user = userDao.getAllUsers();
		map.put("allUsers", user);
		return "myLeagues";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addLeague", method = RequestMethod.GET)
	public String showAddLeaguePage(@PathVariable("currentUser") String currentUser, ModelMap map) {
		List<User> user = userDao.getAllUsers();
		map.put("allUsers", user);
		return "createLeague";
	}
	
	@RequestMapping(path= "/users/{currentUser}/addLeague", method = RequestMethod.POST)
	public String processAddLeagueForm(@PathVariable("currentUser") String currentUser,
			@RequestParam String name, @RequestParam List<String> users) {
		League myLeague = new League();
		myLeague.setName(name);
		myLeague.setOwner(currentUser);
		leagueDao.saveLeague(myLeague);
		String role = userDao.getRoleByUserName(currentUser);
		if (role.contentEquals("Golfer")) {
			userDao.updateRole(currentUser, "League Admin");
		}
		for (int x = 0; x < users.size(); x++) {
			leagueDao.addUserToLeague(users.get(x), name);
		}
		return "redirect:/users/{currentUser}/myLeagues";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addPlayers", method = RequestMethod.POST)
	public String processAddPlayersToLeague(@PathVariable("currentUser") String currentUser, 
			@RequestParam List<String> users, @RequestParam(required = false) String leagueNames) {
		for (int x = 0; x < users.size(); x++) {
			leagueDao.addUserToLeague(users.get(x), leagueNames);
		}
		return "redirect:/users/{currentUser}/myLeagues";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addMatch", method = RequestMethod.POST)
	public String processAddMatchForm(@PathVariable("currentUser") String currentUser, 
			@RequestParam int leagueId, @RequestParam String courseName, @RequestParam String date, @RequestParam int numGolfers, HttpSession session) {
		TeeTime newTeeTime = new TeeTime();
		int courseId = courseDao.getCourseIdByCourseName(courseName);
		int playerId = userDao.getIdByUserName(currentUser);
		if(date != "") {
			int month = Integer.parseInt(date.substring(0, 2));
			int day = Integer.parseInt(date.substring(3, 5));
			int year = Integer.parseInt(date.substring(6, 10));
			LocalDate myDate = LocalDate.of(year, month, day);
			LocalDateTime theDate = LocalDateTime.of(myDate, LocalTime.of(0, 0));
			newTeeTime.setTime(theDate);
		newTeeTime.setLeagueId(leagueId);
		newTeeTime.setNumGolfers(numGolfers);
		newTeeTime.setCourseId(courseId);
		teeTimeDao.saveTeeTime(newTeeTime, playerId);		
	}
		List <Team> teamsInLeague = teamDao.getTeamsByLeagueId(leagueId);
		
		session.setAttribute("teamsInLeague", teamsInLeague);
		return "redirect:/users/{currentUser}/addNewMatch";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addNewMatch", method = RequestMethod.GET)
	public String getAddNewMatch(@PathVariable("currentUser") String currentUser, HttpSession session) {
		session.getAttribute("teamsInLeague");
		
		return "addNewMatch";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addNewMatch", method = RequestMethod.POST)
	public String processAddNewMatch (@PathVariable("currentUser") String currentUser, @RequestParam int team1Id, @RequestParam int team2Id) {
		List<User> team1Golfers = new ArrayList<>();
		List<User> team2Golfers = new ArrayList<>();

		
		return "redirect:/users/{currentUser}/addNewMatchPlayers";
	}
	
	@RequestMapping(path = "/users/{currentUser}/addNewMatchPlayers", method = RequestMethod.GET)
	public String getNewMatchPlayers(@PathVariable("currentUser") String currentUser) {
		return "addNewMatchPlayers";
	}
}
