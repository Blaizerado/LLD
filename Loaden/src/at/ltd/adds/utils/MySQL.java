package at.ltd.adds.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;

	/**
	 * Creates a new MySQL instance for a specific database
	 *
	 * @param hostname
	 *            Name of the host
	 * @param port
	 *            Port number
	 * @param database
	 *            Database name
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 */
	public MySQL(String hostname, String port, String database, String username, String password) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
	}

	public Connection openConnection() throws SQLException, ClassNotFoundException {
		String connectionURL = "jdbc:mysql://" + this.hostname + ":" + this.port;
		if (database != null) {
			connectionURL = connectionURL + "/" + this.database;
		}
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = DriverManager.getConnection(connectionURL, this.user, this.password);
		return connection;
	}
}