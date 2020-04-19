package database;

public class ResultElement {
	
	private int idGame, eloChange;
	private String winnerName, userName;

	public ResultElement(int idGame, String winnerName, String userName, int eloChange) {
		this.idGame = idGame;
		this.eloChange = eloChange;
		this.winnerName = winnerName;
		this.userName = userName;
	}

	public int getIdGame() {
		return idGame;
	}

	public void setIdGame(int idGame) {
		this.idGame = idGame;
	}

	public int getEloChange() {
		return eloChange;
	}

	public void setEloChange(int eloChange) {
		this.eloChange = eloChange;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	

}
