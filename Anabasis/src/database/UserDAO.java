package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import javax.naming.NamingException;

import datatools.Account;
import datatools.Engine;
import datatools.Human;
import datatools.User;

public class UserDAO {
	
	public User getUser(Account account) throws SQLException, NamingException, ClassNotFoundException{
		Connection connection = new Datasource().getDataSource().getConnection();
		User user = null;
		try {
			String sql = "SELECT user.id, username, elo "
					+ "FROM anabasis.user, anabasis.identity "
					+ "WHERE user.id=identity.id "
					+ "AND usertype='1' "
					+ "AND username = ? "
					+ "AND password = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				statement.setString(1, account.getName());
				statement.setString(2, account.getPassword());
				ResultSet result = statement.executeQuery();
				try {
					if(result.next()){
						int id = result.getInt(1);
						String username = result.getString(2);
						int elo = result.getInt(3);
						user = new Human(id,username,elo);
						System.out.println("User found:"+username);
					}
				} finally {result.close();}
			} finally {statement.close();}
		} catch(Exception e){
			System.out.println(account.getName()+" tried to login");
			System.out.println(e.getStackTrace());
		} finally {
			closeConnection(connection);
		}
		return user;
	}


	private void closeConnection(Connection connection) throws SQLException {
		if(!connection.isClosed()){
		connection.close();
		}
	}
	
	
	public List<User> getUsers(int type) throws SQLException, NamingException, ClassNotFoundException {
		List<User> users = new Vector<User>();
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sql = "SELECT id, username, elo "
					+ "FROM anabasis.user " + "WHERE usertype=? ORDER BY elo DESC";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				statement.setInt(1, type);
				ResultSet result = statement.executeQuery();
				try {
					while (result.next()) {
						int id = result.getInt(1);
						String username = result.getString(2);
						int elo = result.getInt(3);
						if(type==1){
							Human human = new Human(id, username, elo);
							users.add(human);
						} else {
							Engine engine = Engine.construct(id, username, elo);
							users.add(engine);
						}
					}
				} finally {result.close();}
			} finally {statement.close();}
		} finally {closeConnection(connection);}
		return users;
	}

	public int getId(String name) throws SQLException, NamingException, ClassNotFoundException {
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sql = "SELECT id " + "FROM anabasis.user "
					+ "WHERE username = ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				statement.setString(1, name);
				ResultSet result = statement.executeQuery();
				try {
					if (result.next()) {
						return result.getInt(1);
					}
				} finally {result.close();}
			} finally {statement.close();}
		} finally {closeConnection(connection);}
		return 0;
	}

	public int createUser(User user, String password) throws SQLException, NamingException, ClassNotFoundException {
		Connection connection = new Datasource().getDataSource().getConnection();
		int id = 0;
		try {
			String sqlUser = "INSERT INTO anabasis.user(username,elo,usertype) VALUES(?,?,1)";
			PreparedStatement statementUser = connection.prepareStatement(sqlUser);
			try {
				statementUser.setString(1, user.getName());
				statementUser.setInt(2, user.getRating());
				statementUser.executeUpdate();
			} finally {statementUser.close();}
			id = getId(user.getName());
			String sqlIdentity = "INSERT INTO anabasis.identity(id,password) VALUES(?,?)";
			PreparedStatement statementIdentity = connection.prepareStatement(sqlIdentity);
			try {
				statementIdentity.setInt(1, id);
				statementIdentity.setString(2, password);
				statementIdentity.executeUpdate();
			} finally {statementIdentity.close();}
		} catch(Exception e){
			System.out.println(user+" tried to create an account");
			System.out.println(e.getStackTrace());
		} finally {closeConnection(connection);}
		return id;
	}

	public List<User> getTopList() throws SQLException, NamingException, ClassNotFoundException {
		List<User> users = new Vector<User>();
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			String sql = "SELECT id, username, elo, usertype FROM anabasis.user ORDER BY elo DESC, id ASC LIMIT 10";
			PreparedStatement statement = connection.prepareStatement(sql);
			try {
				ResultSet result = statement.executeQuery();
				try {
					while (result.next()) {
						int id = result.getInt(1);
						String username = result.getString(2);
						int elo = result.getInt(3);
						int type = result.getInt(4);
						if(type==1){
							Human human = new Human(id, username, elo);
							users.add(human);
						} else {
							Engine engine = Engine.construct(id, username, elo);
							users.add(engine);
						}
					}
				} finally {result.close();}
			} finally {statement.close();}
		} finally {closeConnection(connection);}
		return users;
	}
	
	public void updateElo(List<User> users) throws SQLException, NamingException, ClassNotFoundException{
		Connection connection = new Datasource().getDataSource().getConnection();
		try {
			for(User user : users){
				String sqlUser = "UPDATE anabasis.user SET elo=? WHERE id=?";
				PreparedStatement statementUser = connection.prepareStatement(sqlUser);
				try {
					statementUser.setInt(1, user.getRating());
					statementUser.setInt(2, user.getId());
					statementUser.executeUpdate();
				} finally {statementUser.close();}
			}
		} finally {closeConnection(connection);}
	}

}
