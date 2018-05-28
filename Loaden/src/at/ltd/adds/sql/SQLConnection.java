package at.ltd.adds.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import at.ltd.adds.utils.threading.AsyncWorker;

public class SQLConnection {
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	private final boolean managed;
	private Connection con;
	private boolean used = false;

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
	public SQLConnection(String hostname, String port, String database, String username, String password, Boolean managed) {
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
		this.managed = managed;
		setUpWatcher();
		timestamp = System.currentTimeMillis();
	}

	public SQLConnection openConnection() throws SQLException, ClassNotFoundException {
		String connectionURL = "jdbc:mysql://" + this.hostname + ":" + this.port;
		if (database != null) {
			connectionURL = connectionURL + "/" + this.database;
		}
		connectionURL = connectionURL + "?autoReconnect=true";
		Class.forName("com.mysql.jdbc.Driver");
		con = DriverManager.getConnection(connectionURL, this.user, this.password);
		return this;
	}

	public Connection getRawConnection() {
		return con;
	}

	public String getUser() {
		return user;
	}

	public String getDatabase() {
		return database;
	}

	public String getPassword() {
		return password;
	}

	public String getPort() {
		return port;
	}

	public String getHostname() {
		return hostname;
	}

	public Boolean isUsed() {
		synchronized (this) {
			return used;
		}
	}

	private Boolean sus = false;

	public void setSuspendStateForWatcher(Boolean sus) {
		this.sus = sus;
	}

	private void setUpWatcher() {
		new AsyncWorker(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10 * 1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (sus) {
						continue;
					}
					if (!used) {
						continue;
					}

					if ((System.currentTimeMillis() - timestamp) > 20 * 1000) {
						if (!managed) {
							try {
								con.close();
							} catch (SQLException e) {
								e.printStackTrace();
								System.out.println("[MYSQL] Cant close unmanaged connection.");
							}
							break;
						} else {
							setUnused();
						}

					}
				}
			}
		}, true);
	}

	public long timestamp;

	public void setUsed() {
		synchronized (this) {
			this.used = true;
			timestamp = System.currentTimeMillis();
		}
	}

	public void setUnused() {
		synchronized (this) {
			this.used = false;
		}
	}

}
