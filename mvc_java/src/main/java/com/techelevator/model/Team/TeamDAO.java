package com.techelevator.model.Team;

import java.util.List;

public interface TeamDAO {
	
	public List<Team> getAllTeams();
	
	public Team getTeamByTeamId(int teamId);
	
	public List<Team> getTeamsByLeagueId(int leagueId);
	
	public List<Team> getTeamsByUserId(int id);

}
