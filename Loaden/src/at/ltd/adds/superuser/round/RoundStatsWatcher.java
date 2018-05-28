package at.ltd.adds.superuser.round;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameCoinsAsync;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.gungame.guns.events.custom.EventGunShootAsync;

public class RoundStatsWatcher implements Listener {

	public static RoundData currenRound;
	public static ReentrantLock lock = new ReentrantLock();
	@EventHandler
	public void on(EventGameRoundStart e) {
		if (e.getGameMap() == null | e.getGameMap().getMapName() == null) {
			return;
		}
		int id = 0;
		if (currenRound == null) {
			SQLQuery query = Main.createSQLQuery();
			try {
				ResultSet rs = query.querySQL("SELECT * FROM `ROUND_STATS` ORDER BY `ROUND_STATS`.`ROUNDID` DESC LIMIT 1");
				if (rs.next()) {
					id = rs.getInt("ROUNDID") + 1;
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			lock.lock();
			currenRound = new RoundData(id, e.getGameMap().getMapName());
			lock.unlock();
		} else {
			lock.lock();
			currenRound.setMapName(e.getGameMap().getMapName());
			lock.unlock();
		}

	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventJoinGame e) {
		if (e.isCancelled()) {
			return;
		}
		lock.lock();
		if (currenRound != null) {
			currenRound.addPlayer(e.getPlayer());
			currenRound.setPlayerCount(currenRound.getPlayerCount() + 1);
		}
		lock.unlock();
	}

	@EventHandler
	public void on(EventGunGameCoinsAsync e) {
		lock.lock();
		if (currenRound != null) {
			currenRound.setCoinsCashOut(currenRound.getCoinsCashOut() + e.getCoins());
		}
		lock.unlock();
	}

	@EventHandler
	public void on(EventGunGameDeath e) {
		lock.lock();
		if (currenRound != null) {
			currenRound.setKills(currenRound.getKills() + 1);
		}
		lock.unlock();
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventGunShootAsync e) {
		if (e.wasCancelled()) {
			return;
		}
		lock.lock();
		if (currenRound != null) {
			currenRound.setShoots(currenRound.getShoots() + 1);
		}
		lock.unlock();
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		lock.lock();
		if (currenRound != null) {
			currenRound.setRoundend(System.currentTimeMillis());
		}
		lock.unlock();
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				lock.lock();
				if (currenRound != null) {
					if (!Main.isDevModeEnabled()) {
						currenRound.saveToDB();
					}
				}
				lock.unlock();

				int id = 0;
				SQLQuery query = Main.createSQLQuery();
				try {
					ResultSet rs = query.querySQL("SELECT * FROM `ROUND_STATS` ORDER BY `ROUND_STATS`.`ROUNDID` DESC LIMIT 1");
					if (rs.next()) {
						id = rs.getInt("ROUNDID") + 1;
					}
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				lock.lock();
				currenRound = new RoundData(id, "NO");
				lock.unlock();

			}
		});
	}

}
