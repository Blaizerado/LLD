package at.ltd.adds.sql;

public class SQLNoConnectionUnused extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 914687200806677821L;

	public SQLNoConnectionUnused() {}
	public SQLNoConnectionUnused(String message) {
		super(message);
	}

	public SQLNoConnectionUnused(Throwable cause) {
		super(cause);
	}

	public SQLNoConnectionUnused(String message, Throwable cause) {
		super(message, cause);
	}
}
