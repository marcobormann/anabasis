package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

public class GameDAO {

	public int enterGame(int winnerId) throws SQLException, NamingException, ClassNotFoundException {
		Connection connection = new Datasource().getDataSource().getConnection();
		int id = 0;
		try {
			String sqlGame = "INSERT INTO anabasis.game(winner_id) VALUES(?)";
			PreparedStatement statementGame = connection.prepareStatement(sqlGame);
			try {
				statementGame.setInt(1, winnerId);
				statementGame.executeUpdate();
			} catch(Exception e){
				System.out.println(winnerId+" has won");
				e.printStackTrace();
			} finally {statementGame.close();}
			id = getLastId();
		} catch(Exception e){
		} finally {closeConnection(connection);}
		return id;
	}

	private int getLastId() throws SQLException, NamingException, ClassNotFoundException {
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sql = "SELECT MAX(idgame) FROM anabasis.game";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				ResultSet result = statement.executeQuery();
				try {
					if (result.next()) {
						return result.getInt(1);
					}
				} finally {result.close();}
			} finally {statement.close();}
		} catch(Exception e){
		} finally {
			closeConnection(connection);
		}
		return 0;
	}

	public void register(int gameId, int userId, int difference) 
			throws SQLException, NamingException, ClassNotFoundException {
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sqlUser = "INSERT INTO anabasis.participants"
					+ "(game_idgame,user_id,elo_change) VALUES(?,?,?)";
			PreparedStatement statementUser = connection.prepareStatement(sqlUser);
			try {
				statementUser.setInt(1, gameId);
				statementUser.setInt(2, userId);
				statementUser.setInt(3, difference);
				statementUser.executeUpdate();
			} finally {statementUser.close();}
		} catch(Exception e){
		} finally {closeConnection(connection);}
	}

	private void closeConnection(Connection connection) throws SQLException {
		if(!connection.isClosed()){
		connection.close();
		}
	}
	
	public List<String> getLastResults() throws SQLException, NamingException, ClassNotFoundException {
		List<ResultElement> rawResults = new Vector<ResultElement>();
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sql = "SELECT g.idgame, w.username, u.username, p.elo_change "
					+ "FROM anabasis.game g, anabasis.participants p, anabasis.user w, anabasis.user u "
					+ "WHERE g.idgame = p.game_idgame "
					+ "AND w.id = winner_id "
					+ "AND u.id = user_id "
					+ "ORDER BY idgame DESC LIMIT 120";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				ResultSet result = statement.executeQuery();
				try {
					while (result.next()) {
						ResultElement element = new ResultElement(result.getInt(1), 
								result.getString(2), result.getString(3), result.getInt(4));
						rawResults.add(element);
					}
				} finally {result.close();}
			} finally {statement.close();}
		} finally {closeConnection(connection);}
		List<String> results = calculateResultTexts(rawResults);
		return results;
	}

	private List<String> calculateResultTexts(List<ResultElement> rawResults) {
		List<String> results = new Vector<String>();
		int index = 0;
		int oldGameId = 0;
		String resultText = "";
		while(index<rawResults.size()){
			ResultElement element = rawResults.get(index);
			if(oldGameId!=element.getIdGame()){
				if(resultText.length()>0){results.add(resultText);}
				resultText = "wins over ";
				oldGameId = element.getIdGame();
			}
			String add="";
			if(element.getWinnerName().equals(element.getUserName())){add="+";}
			String userText = element.getUserName()+" ("+add+element.getEloChange()+") ";
			if(element.getWinnerName().equals(element.getUserName())){
				resultText = userText + resultText;
			} else {
				resultText = resultText + userText;
			}
			index++;
		}
		results.add(resultText);
		return results;
	}

}
