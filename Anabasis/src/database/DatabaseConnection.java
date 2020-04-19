package database;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnection {

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		Class.forName("org.mariadb.jdbc.Driver");
		connection = DriverManager.getConnection(
					"jdbc:mariadb://localhost:3306/anabasis", "marco", "1NfYcYk7");
		return connection;
	}
}
