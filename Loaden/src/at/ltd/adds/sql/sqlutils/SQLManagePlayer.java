package at.ltd.adds.sql.sqlutils;

import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import at.ltd.adds.sql.SQLCreatePlayer;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.gungame.ranks.GRankPerm;

public class SQLManagePlayer {

	public static boolean addPlayer(String uuid, String name) {
		SQLQuery query = new SQLQuery(SQLCollection.getConnectionHandler());
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE UUID LIKE '" + uuid + "'");
			if (rs.next()) {
				SQLPlayer player = new SQLPlayer(uuid, SQLCollection.getConnectionHandler());
				SQLCollection.lock.lock();
				try {
					SQLCollection.SQL_PLAYER_LIST.put(uuid, player);
				} finally {
					SQLCollection.lock.unlock();
				}
				player.load();
				player.setName(name);
				player.setLastupdate(new Timestamp(System.currentTimeMillis()));
				return true;
			} else {
				SQLCreatePlayer cp = new SQLCreatePlayer(SQLCollection.getConnectionHandler());
				cp.setUUID(uuid);
				cp.setNAME(name);
				cp.setIP("NEWJOIN");
				cp.setRANK(GRankPerm.Player.getPerm());
				cp.createPlayer();

				SQLPlayer player = new SQLPlayer(uuid, SQLCollection.getConnectionHandler());
				SQLCollection.lock.lock();
				try {
					SQLCollection.SQL_PLAYER_LIST.put(uuid, player);
				} finally {
					SQLCollection.lock.unlock();
				}
				player.load();
				player.setName(uuid);
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		AsyncThreadWorkers.submitSyncWork(new Runnable() {

			@Override
			public void run() {
				Bukkit.broadcastMessage("[SQLManagePlayer] ERROR ON JOIN UUID: " + uuid + " NAME: " + name);
			}
		});

		return false;

	}

	public static void removePlayer(Player p) {
		String uuid = p.getUniqueId().toString();
		AsyncThreadWorkers.submitWork(new Runnable() {

			@Override
			public void run() {
				EventPlayerQuitAsync event = new EventPlayerQuitAsync(p);
				Bukkit.getPluginManager().callEvent(event);
				try {
					Thread.sleep(500);
					SQLCollection.getPlayer(p).save();
				} catch (Exception e) {
					e.printStackTrace();
				}
				SQLCollection.lock.lock();
				try {
					SQLCollection.SQL_PLAYER_LIST.remove(p.getUniqueId().toString());
				} finally {
					SQLCollection.lock.unlock();
				}

			}
		});
	}

	public static String getIP(Player p) {
		InetSocketAddress IPAdressPlayer = p.getAddress();
		String sfullip = IPAdressPlayer.toString();
		String[] fullip;
		String[] ipandport;
		fullip = sfullip.split("/");
		String sIpandPort = fullip[1];
		ipandport = sIpandPort.split(":");
		String sIp = ipandport[0];
		return sIp;
	}

}
