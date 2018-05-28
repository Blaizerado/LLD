package at.ltd.adds.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.ltd.adds.Lg;

public class SQLQuery {

	SQLConnectionHandler sqlConnectionHandler;

	public SQLQuery(SQLConnectionHandler sqlConnectionHandler) {
		this.sqlConnectionHandler = sqlConnectionHandler;
	}

	/**
	 * Executes a SQL Query<br>
	 * 
	 * 
	 * @param query
	 *            Query to be run
	 * @return the results of the query
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public ResultSet querySQL(String query) throws SQLException, ClassNotFoundException {
		SQLConnection connection = TryToGetRawConnection();
		try {
			Statement statement = connection.getRawConnection().createStatement();
			ResultSet result = statement.executeQuery(query);
			return result;
		} finally {
			connection.setUnused();
		}

	}

	/**
	 * Executes an Update SQL Query<br>
	 * See {@link java.sql.Statement#executeUpdate(String)}<br>
	 * 
	 * @param query
	 *            Query to be run
	 * @return Result Code, see {@link java.sql.Statement#executeUpdate(String)}
	 * @throws SQLException
	 *             If the query cannot be executed
	 * @throws ClassNotFoundException
	 *             If the driver cannot be found; see {@link #openConnection()}
	 */
	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		SQLConnection connection = TryToGetRawConnection();
		try {
			Statement statement = connection.getRawConnection().createStatement();
			int result = statement.executeUpdate(query);
			return result;
		} finally {
			connection.setUnused();
		}

	}

	/**
	 * Executes an PreparedStatement<br>
	 * See {@link java.sql.PreparedStatement}<br>
	 * 
	 * @param sql
	 *            Query to be modified
	 * @throws SQLException
	 *             If the query cannot be executed
	 */
	public void executePreparedStatement(SQLPreparedStatement SQLps, String sql) throws SQLException {
		SQLConnection connection = TryToGetRawConnection();
		try {
			SQLps.execution(connection.getRawConnection().prepareStatement(sql));
		} finally {
			connection.setUnused();
		}
	}

	/**
	 * Trys to get a unused connection. In case there are no unused Connections
	 * it will create one.
	 * 
	 * @return
	 */
	public SQLConnection TryToGetRawConnection() {
		try {
			return sqlConnectionHandler.getConnection();
		} catch (SQLNoConnectionUnused e) {
			Lg.lgError("ERROR ON TryToGetRawConnection");
			e.printStackTrace();
		}
		return sqlConnectionHandler.createUnmanagedConnection();
	}

}
