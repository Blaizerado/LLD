package at.ltd.adds.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import at.ltd.adds.utils.threading.AsyncWorker;

public class SQLConnectionHandler {

	private ArrayList<SQLConnection> connections = new ArrayList<>();
	private final int cons;
	private final String user;
	private final String database;
	private final String password;
	private final String port;
	private final String hostname;
	private boolean isRunning = true;

	public SQLConnectionHandler(int connections, String hostname, String port, String database, String username, String password) {
		this.cons = connections;
		this.hostname = hostname;
		this.port = port;
		this.database = database;
		this.user = username;
		this.password = password;
		setUp();
	}

	private void createNewConnection() {
		System.out.println("[MYSQL] Adding new Connection.");
		SQLConnection con = new SQLConnection(hostname, port, database, user, password, true);
		try {
			con.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		synchronized (this) {
			connections.add(con);
		}
	}

	private void testConnection(SQLConnection sqlcon) throws SQLException {
		Connection con = sqlcon.getRawConnection();
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM `MC` ORDER BY `KILLS` DESC LIMIT 1");
	}

	private void setUp() {
		for (int i = 1; i <= cons; i++) {
			SQLConnection con = new SQLConnection(hostname, port, database, user, password, true);
			try {
				con.openConnection();
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
			synchronized (this) {
				connections.add(con);
			}
		}

		new AsyncWorker(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						if (!isRunning) {
							break;
						}
						Thread.sleep(4500);
						ArrayList<SQLConnection> testconns = null;
						synchronized (this) {
							testconns = (ArrayList<SQLConnection>) connections.clone();
						}

						for (SQLConnection con : (ArrayList<SQLConnection>) connections.clone()) {
							if (!con.isUsed()) {
								con.setUsed();
								try {
									testConnection(con);
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("[MYSQL] Trying to fix the problem!");
									try {
										try {
											con.getRawConnection().close();
										} catch (Exception e2) {
											System.out.println("[MYSQL] Failed to close connection.");
											e2.printStackTrace();
										}
										System.out.println("[MYSQL] Trying to rebuild the connection.");
										con.openConnection();
									} catch (Exception e2) {
										e2.printStackTrace();
										System.out.println("[MYSQL] Failed to rebuild the connection. Suspending this connection!");
										synchronized (this) {
											connections.remove(con);
										}
										createNewConnection();
									}
								}
								con.setUnused();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}, true);
	}

	public SQLConnection getConnection() throws SQLNoConnectionUnused {
		synchronized (this) {
			for (SQLConnection con : connections) {
				if (!con.isUsed()) {
					con.setUsed();
					return con;
				}
			}
			throw new SQLNoConnectionUnused("All connections are used. " + cons + "/" + cons);
		}

	}

	public void stop() {
		synchronized (this) {
			isRunning = false;
		}
	}

	public SQLConnection createUnmanagedConnection() {
		SQLConnection con = new SQLConnection(hostname, port, database, user, password, false);
		try {
			con.openConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		con.setUsed();
		return con;
	}

	public SQLQuery createSQLQuery() {
		synchronized (this) {
			return new SQLQuery(this);
		}
	}

}
