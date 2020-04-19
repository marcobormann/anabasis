package datatools;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import database.GameDAO;
import database.UserDAO;

/** the class contains a vector of possibly four players for one game */
public class Match implements Serializable {
	private static final long serialVersionUID = 1991035419951162897L;
	public static final String OPEN = "open";
	public static final String RUNNING = "running";
	public static final String COMPLETE = "complete";
	public static final String ENDED = "ended";

	/** the game creator */
	private Human creator = new Human();
	/** the users participating in the match */
	private List<User> players = new Vector<User>();
	/** the matches current status */
	private String status;
	/** the matches id */
	private int id;
	/** the matches timestamp */
	private Date date;
	private String result = "";
	
	public Match(int id, Human creator) {
		this.id = id;
		this.creator = creator;
		players.add(creator);
		status = OPEN;
		setDate(new Date());
	}

	public Match() {}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Human getCreator() {
		return creator;
	}

	public void setCreator(Human c) {
		creator = c;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}	

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}        	

	public void start(){
		status = RUNNING;
	}
	
	public void end(int winnerId){
		status = ENDED;
		User winner = get(winnerId);
		GameDAO gameDAO = new GameDAO();
		int gameId = 0;
		try {
			gameId = gameDAO.enterGame(winnerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        double oldWinnerElo = winner.getRating();
        double newWinnerElo = oldWinnerElo;
        for(User loser : players){
            if(loser.getId()!=winnerId){
                double oldLoserElo = loser.getRating();
                float factor = (float) Math.pow(10, ((oldLoserElo-oldWinnerElo)/400));
                double prob = 1/(1+factor);
                newWinnerElo = (int) (newWinnerElo+(32*(1-prob)))+1;
                int newLoserElo = (int) (oldLoserElo-(32*(1-prob)))+1;
                if(newLoserElo>oldLoserElo){newLoserElo=(int)oldLoserElo;}
                loser.setRating(newLoserElo);
                int loserDifference = (int)(newLoserElo-oldLoserElo);
                try {
					gameDAO.register(gameId, loser.getId(), loserDifference);
				} catch (Exception e) {
					e.printStackTrace();
				}
                result += "* "+ loser.getName()+" ("+loserDifference+") ";
            }
        }
        winner.setRating((int)newWinnerElo);
        int winnerDifference = (int)(newWinnerElo-oldWinnerElo);
        try {
			gameDAO.register(gameId,winnerId,winnerDifference);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        result = winner.getName()+" (+"+winnerDifference+") wins over "+result;
        UserDAO userDAO = new UserDAO();
        try {
			userDAO.updateElo(players);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isOpen(){
		return status.equals(OPEN);
	}
	
	/** reads a user given the id */
	public User get(int id) {
		for (User u : players) {
			if (id == u.getId()) {
				return u;
			}
		}
		return null;
	}

	/**
	 * add a user to the match returns true when the match can start
	 */
	public void addUser(User u) {
		if(players.size() < 4){
			players.add(u);
			if (players.size() == 4) {
				status = COMPLETE;
			}
		}
	}

	/** remove a user from a match */
	public void removeUser(int id) {
		int index = 0;
		while(index<players.size()){
			User u = players.get(index);
			if(u.getId() == id) {
				players.remove(u);
			} else {
				index++;
			}
		}
	}
	
	/** method to see if a user is in this match */
	public boolean has(int id) {
		for(User u : players) {
			if (u.getId() == id) {
				return true;
			}
		}
		return false;
	}

	/** gets a user object from the vector */
	public User getUser(int i) {
		return (User) players.get(i);
	}

	/** method to allow the cloning of a match */
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {}
		return null;
	}

	public String toString(){
        String s = status+" * ";
        if(status.equals(ENDED)){return s+result;}
        int elo = 0;
        for(User u : players){
        	s=s+u.getName()+"("+u.getRating()+") * ";
        	elo=elo+u.getRating();
        }
        if(players.size()>0){
        	s=s+"avg:"+elo/players.size();
        }
        return s;
	}
	
	public boolean isComplete(){
		return status.equals(COMPLETE);
	}

    public boolean isRunning(){
    	return status.equals(RUNNING);
    }
    
    public boolean hasEnded(){
    	return status.equals(ENDED);
    }

}