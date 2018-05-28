package at.ltd.adds.superuser;

import java.sql.ResultSet;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;

public class SuperUserCache implements Listener {

	public static void init() {
		Main.registerListener(new SuperUserCache());
	}

	public static void login(String uuid, Player player) {
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				SQLQuery query = Main.getSQLQuery();
				try {
					query.updateSQL("INSERT INTO `SUPERUSER_CACHE` (`UUID`, `TIME`, `IP`) VALUES ('" + uuid + "', '" + System.currentTimeMillis() + "', '" + getIP(player) + "');");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public static void logout(String uuid) {
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				SQLQuery query = Main.getSQLQuery();
				try {
					ResultSet rs = query.querySQL("SELECT * FROM `SUPERUSER_CACHE` WHERE `UUID` LIKE '" + uuid + "'");
					if (rs.next()) {
						query.updateSQL("DELETE FROM `SUPERUSER_CACHE` WHERE UUID = '" + uuid + "';");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		AsyncThreadWorkers.submitDelayedWorkSec(new Runnable() {

			@Override
			public void run() {
				try {
					SQLQuery query = Main.getSQLQuery();
					String uuid = e.getPlayer().getUniqueId().toString();
					ResultSet rs = query.querySQL("SELECT * FROM `SUPERUSER_CACHE` WHERE `UUID` LIKE '" + uuid + "'");
					if (rs.next()) {
						long time = rs.getLong("TIME");
						String ip = rs.getString("IP");
						if (ip.equals(getIP(e.getPlayer()))) {
							if ((time + (24 * 60 * 60 * 1000)) > System.currentTimeMillis()) {
								if (!SuperUser.isSuperUser(uuid)) {
									Cf.rsS(Cf.SUPERUSER_AUTO_LOGIN, e.getPlayer());
									AsyncThreadWorkers.submitSyncWork(() -> e.getPlayer().setGameMode(GameMode.CREATIVE));
									synchronized (SuperUser.SU_USER) {
										SuperUser.SU_USER.add(uuid);
									}
								}
								return;
							}
						}
						query.updateSQL("DELETE FROM `SUPERUSER_CACHE` WHERE UUID = '" + uuid + "'");

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1);
	}

	public static String getIP(Player player) {
		return player.getAddress().getHostName();
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		synchronized (SuperUser.SU_USER) {
			SuperUser.SU_USER.remove(e.getPlayer().getUniqueId().toString());
		}
	}

}
