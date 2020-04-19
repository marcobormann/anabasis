package database;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Datasource {
	
	private Connection connection;
	
	public DataSource getDataSource2() throws NamingException{
		InitialContext initContext = new InitialContext();
		Context env = (Context)initContext.lookup("java:comp/env");
		DataSource ds = (DataSource)env.lookup("jdbc/anabasis");
		return ds;
	}

	public Datasource getDataSource() throws NamingException, ClassNotFoundException, SQLException{
		DatabaseConnection dbc = new DatabaseConnection();
		connection = dbc.getConnection();
		return this;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	
}
