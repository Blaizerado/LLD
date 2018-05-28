package at.ltd.adds.sql.sqlutils;

import java.net.InetSocketAddress;
import java.sql.Date;
import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import at.ltd.adds.Lg;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.ranks.GRankPerm;

public class SQLChecker implements Listener {

	public SQLChecker() {
		initScheEdit();
		initScheSave();
	}
	public AsyncWorker save;
	public AsyncWorker edit;
	public ArrayList<SQLTimeEvents> tevents = new ArrayList<>();

	public void addTimeListener(SQLTimeEvents sqlTimeEvents) {
		tevents.add(sqlTimeEvents);
	}

	private void initScheSave() {
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			try {
				save();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}, 0, 20);
	}

	public void initScheEdit() {
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			try {
				edit();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}, 0, 60);

	}

	// SAVE
	public void save() throws Exception {
		Lg.lgMSG("SAVING ALL PLAYERS WITH SQL");
		ArrayList<String> list = new ArrayList<>();
		try {
			SQLCollection.lock.lock();
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				for (SQLPlayer sql : SQLCollection.SQL_PLAYER_LIST.values()) {
					if (p.getUniqueId().toString().equals(sql.getUUID())) {
						list.add(p.getUniqueId().toString());
					}
				}
			}
		} finally {
			SQLCollection.lock.unlock();
		}

		for (String s : list) {
			SQLPlayer player = SQLCollection.getPlayer(s);
			player.save();
		}

	}
	// EDIT

	public void edit() throws Exception {
		ArrayList<Player> list = new ArrayList<>();
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			try {
				SQLCollection.lock.lock();
				for (SQLPlayer sql : SQLCollection.SQL_PLAYER_LIST.values()) {
					if (p.getUniqueId().toString().equals(sql.getUUID())) {
						list.add(p);
						break;
					}
				}
			} finally {
				SQLCollection.lock.unlock();
			}

		}

		for (Player pl : list) {
			try {
				SQLCollection.lock.lock();
				if (SQLCollection.SQL_PLAYER_LIST.containsKey(pl.getUniqueId().toString())) {
					AsyncThreadWorkers.submitSyncWork(() -> {
						SQLPlayer player = SQLCollection.getPlayer(pl);
						player.setPlaytime(player.getPlaytime() + 1);
						player.setRank(GRankPerm.getRank(pl).getPerm());
						player.setIP(getIP(pl));
						player.setLastjoin(new Date(System.currentTimeMillis()));
					});
				}
			} finally {
				SQLCollection.lock.unlock();
			}

		}
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
