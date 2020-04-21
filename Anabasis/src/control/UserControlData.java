package control;

import java.util.List;
import java.util.Vector;

import database.GameDAO;
import database.UserDAO;
import datatools.Engine;
import datatools.Human;
import datatools.Match;
import datatools.User;

public class UserControlData {
	
	private List<User> engineList;
	private List<User> topList;
	private List<Match> matchList;
	private List<String> resultList;
	private int nextMatchId = 1;
	private String selectedEngine;
	private String selectedMatch;
	
	public UserControlData() {
		updateEngineList();
		updateTopList();
		updateResultList();
		matchList = new Vector<Match>();
	}
	
	public String getSelectedEngine() {
		return selectedEngine;
	}

	public void setSelectedEngine(String selectedEngine) {
		this.selectedEngine = selectedEngine;
	}

	public String getSelectedMatch() {
		return selectedMatch;
	}

	public void setSelectedMatch(String selectedMatch) {
		this.selectedMatch = selectedMatch;
	}

	public List<String> getResultList() {
		return resultList;
	}

	public List<User> getEngineList() {
		return engineList;
	}

	public List<User> getTopList() {
		return topList;
	}

	public List<Match> getMatchList() {
		return matchList;
	}

	public void updateResultList() {
		GameDAO dao = new GameDAO();
		try {
			resultList = dao.getLastResults();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTopList() {
		UserDAO dao = new UserDAO();
		try {
			topList = dao.getTopList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateEngineList() {
		UserDAO dao = new UserDAO();
		try {
			engineList = dao.getUsers(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String removeEngine(User user, int matchId, int engineId) {
		if (matchId == 0) {
			return "you have to select a match!";
		}
		if (engineId == 0) {
			return "you have to select an engine!";
		}
		Match match = getMatch(matchId);
		if (match == null) {
			return "try again, something went wrong!";
		}
		if (!(match.getCreator().getId() == user.getId())) {
			return "you did not create this match!";
		}
		if (!match.isOpen()) {
			return "this match is not open anymore!";
		}
		if (!match.has(engineId)) {
			return "this engine is not in that game!";
		}
		match.removeUser(engineId);
		return null;
	}

	public String addEngine(User user, int matchId, int engineId) {
		if (matchId == 0) {
			return "you have to select a match!";
		}
		if (engineId == 0) {
			return "you have to select an engine!";
		}
		if (alreadyPlaying(engineId)) {
			return "the engine is already used!";
		}
		Match match = getMatch(matchId);
		if (match == null) {
			return "try again, something went wrong!";
		}
		if (!(match.getCreator().getId() == user.getId())) {
			return "you did not create this match!";
		}
		if (!match.isOpen()) {
			return "this match is not open anymore!";
		}
		User e = getNewEngine(engineId);
		if (e == null) {
			return "try again, something went wrong!";
		}
		match.addUser(e);
		return null;
	}
	public String leaveGame(User user, int matchId) {
		if (matchId == 0) {
			return "please select a match to leave!";
		}
		Match match = getMatch(matchId);
		if (match == null) {
			return "try again, something went wrong!";
		}
		if (!match.has(user.getId())) {
			return "your are not in this game!";
		}
		if (!match.isOpen()) {
			return "this match is not open anymore!";
		}
		match.removeUser(user.getId());
		return null;
	}

	public String joinGame(User user, int matchId) {
		if (matchId == 0) {
			return "please select a match to join!";
		}
		if (alreadyPlaying(user.getId())) {
			return "you are already in another game!";
		}
		Match match = getMatch(matchId);
		if (match == null) {
			return "try again, something went wrong!";
		}
		if (!match.isOpen()) {
			return "this match is not open to be joined!";
		}
		match.addUser(user);
		return null;
	}

	public String createGame(User user) {
		if (alreadyPlaying(user.getId())) {
			return "you are already in another game!";
		}
		Match match = new Match(nextMatchId, (Human) user);
		matchList.add(match);
		nextMatchId++;
		return null;
	}

	private boolean alreadyPlaying(int id) {
		for (Match m : matchList) {
			if (m.has(id)) {
				return true;
			}
		}
		return false;
	}

	private Match getMatch(int matchId) {
		for (Match m : matchList) {
			if (m.getId() == matchId) {
				return m;
			}
		}
		return null;
	}

	private User getNewEngine(int engineId) {
		Engine e = null;
		for (User u : engineList) {
			if (u.getId() == engineId) {
				e = Engine.construct(u.getId(), u.getName(), u.getRating());
			}
		}
		return e;
	}

	public void updateMatchEnds() {
		int index = 0;
		while (index < matchList.size()) {
			Match match = matchList.get(index);
			if (match.hasEnded()) {
				matchList.remove(index);
				updateEngineList();
				updateTopList();
			} else {
				index++;
			}
		}
	}


}
