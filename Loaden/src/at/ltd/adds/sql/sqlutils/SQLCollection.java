package at.ltd.adds.sql.sqlutils;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.sql.SQLConnectionHandler;
import at.ltd.adds.sql.SQLPlayer;

public class SQLCollection {

	public static final HashMap<String, SQLPlayer> SQL_PLAYER_LIST = new HashMap<String, SQLPlayer>();
	public static final ReentrantLock lock = new ReentrantLock();
	public static SQLChecker sqlChecker;

	public static SQLPlayer getPlayer(Player p) {
		lock.lock();
		try {
			return SQL_PLAYER_LIST.get(p.getUniqueId().toString());
		} finally {
			lock.unlock();
		}
	}

	public static SQLPlayer getPlayer(String uuid) {
		lock.lock();
		try {
			return SQL_PLAYER_LIST.get(uuid);
		} finally {
			lock.unlock();
		}
	}
	public static SQLConnectionHandler getConnectionHandler() {
		return Main.getConnectionHandler();
	}

}
