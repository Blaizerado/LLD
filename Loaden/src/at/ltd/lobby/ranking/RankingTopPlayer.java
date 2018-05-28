package at.ltd.lobby.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.SkullType;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Hologram;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.lobby.LobbyUtils;
import at.ltd.lobby.ranking.stats.PlayerStats;
import at.ltd.lobby.ranking.stats.StatsManager;

public class RankingTopPlayer implements Listener {

	public static ArrayList<Location> LOCS;
	public static HashMap<Integer, PlayerStats> STATS = new HashMap<>();
	public static final ReentrantLock STATS_LOCK = new ReentrantLock();
	public static final ReentrantLock HOLOS_LOCK = new ReentrantLock();
	public static HashMap<Player, ArrayList<Hologram>> HOLOS = new HashMap<>();

	@ConfigAble(key = "LINE_1", value = "&8#&6[POS]")
	public static String LINE_1;
	@ConfigAble(key = "LINE_2", value = "&b[NAME]")
	public static String LINE_2;
	@ConfigAble(key = "LINE_3", value = "&2[KILLS]")
	public static String LINE_3;
	@ConfigAble(key = "LINE_4", value = "&4[DEATHS]")
	public static String LINE_4;

	@ConfigAble(key = "", value = "-2075.0*34.0*1388.0*0.0*0.0*GG_Lobby|-2069.0*34.0*1388.0*0.0*0.0*GG_Lobby|-2071.0*34.0*1390.0*0.0*0.0*GG_Lobby|-2073.0*34.0*1392.0*0.0*0.0*GG_Lobby|-2075.0*34.0*1394.0*0.0*0.0*GG_Lobby|-2066.0*35.0*1389.0*0.0*0.0*GG_Lobby|-2068.0*35.0*1391.0*0.0*0.0*GG_Lobby|-2070.0*35.0*1393.0*0.0*0.0*GG_Lobby|-2072.0*35.0*1395.0*0.0*0.0*GG_Lobby|-2074.0*35.0*1397.0*0.0*0.0*GG_Lobby|-2063.0*36.0*1390.0*0.0*0.0*GG_Lobby|-2065.0*36.0*1392.0*0.0*0.0*GG_Lobby|-2067.0*36.0*1394.0*0.0*0.0*GG_Lobby|-2069.0*36.0*1396.0*0.0*0.0*GG_Lobby|-2071.0*36.0*1398.0*0.0*0.0*GG_Lobby|-2073.0*36.0*1400.0*0.0*0.0*GG_Lobby|-2060.0*37.0*1391.0*0.0*0.0*GG_Lobby|-2062.0*37.0*1393.0*0.0*0.0*GG_Lobby|-2064.0*37.0*1395.0*0.0*0.0*GG_Lobby|-2066.0*37.0*1397.0*0.0*0.0*GG_Lobby|-2068.0*37.0*1399.0*0.0*0.0*GG_Lobby|-2070.0*37.0*1401.0*0.0*0.0*GG_Lobby|-2072.0*37.0*1403.0*0.0*0.0*GG_Lobby")
	public static String LOCLIST;

	public static void init() {
		Config.loadMyClass("Holoranking", RankingTopPlayer.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("Holoranking", RankingTopPlayer.class);
			translate();
			LOCS = LocUtils.locationListStringToList(LOCLIST);
			for (Location loc : LOCS) {
				loc.add(0.5, -0.55, 0.5);
			}
			LobbyUtils.getAllPlayersInLobby().forEach(p -> set(p));
		});
		translate();
		LOCS = LocUtils.locationListStringToList(LOCLIST);
		for (Location loc : LOCS) {
			loc.add(0.5, -0.55, 0.5);
		}
		Bukkit.getServer().getPluginManager().registerEvents(new RankingTopPlayer(), Main.getPlugin());
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> update(), 5, 60 * 5);
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			LobbyUtils.getAllPlayersInLobby().forEach(p -> set(p));
		}, 20, 30);
	}

	private static void translate() {
		LINE_1 = ChatColor.translateAlternateColorCodes('&', LINE_1);
		LINE_2 = ChatColor.translateAlternateColorCodes('&', LINE_2);
		LINE_3 = ChatColor.translateAlternateColorCodes('&', LINE_3);
		LINE_4 = ChatColor.translateAlternateColorCodes('&', LINE_4);
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		AsyncThreadWorkers.submitDelayedWorkSec(() -> update(), 2);
	}
	@EventHandler
	public void on(EventPlayerQuitAsync e) {
		try {
			HOLOS_LOCK.lock();
			if (HOLOS.containsKey(e.getPlayer())) {
				HOLOS.remove(e.getPlayer());
			}
		} finally {
			HOLOS_LOCK.unlock();
		}
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		set(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EventTeleport e) {
		if (e.isCancelled()) {
			return;
		}
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if (LobbyUtils.isLocationInLobby(loc)) {
			if (!LobbyUtils.isLocationInLobby(e.getLocation())) {
				remove(p);
			}
		}

		if (!LobbyUtils.isLocationInLobby(loc)) {
			if (LobbyUtils.isLocationInLobby(e.getLocation())) {
				AsyncThreadWorkers.submitDelayedWorkSec(() -> set(p), 1);

			}
		}

	}

	public static synchronized void update() {
		ArrayList<PlayerStats> stats = StatsManager.getBestPlayers(LOCLIST.length());
		try {
			STATS_LOCK.lock();
			STATS.clear();
			for (int i = 0; i < LOCLIST.length(); i++) {
				PlayerStats ps = stats.get(i);
				Location loc = LOCS.get(i);
				STATS.put(i, ps);
				if (loc != null) {
					AsyncThreadWorkers.submitSyncWork(() -> {
						if (ps != null) {
							setHead(ps.getName(), loc.clone().subtract(0.5, -0.55, 0.5));
						} else {
							setHead("Steve", loc.clone().subtract(0.5, -0.55, 0.5));
						}

					});
				}

			}
		} finally {
			STATS_LOCK.unlock();
		}

		LobbyUtils.getAllPlayersInLobby().forEach(p -> set(p));
	}

	public static synchronized void set(Player p) {
		try {
			remove(p);
			STATS_LOCK.lock();
			HOLOS_LOCK.lock();
			ArrayList<Hologram> gram = new ArrayList<>();
			STATS.keySet().forEach(pos -> {
				PlayerStats ps = STATS.get(pos);
				Hologram holo = new Hologram(LOCS.get(pos).clone(), LINE_1.replace("[POS]", (pos + 1) + ""), LINE_2.replace("[NAME]", ps.getName()), LINE_3.replace("[KILLS]", ps.getKills() + ""), LINE_4.replace("[DEATHS]", ps.getDeaths() + ""));
				holo.send(p);
				gram.add(holo);
			});
			HOLOS.put(p, gram);
		} finally {
			STATS_LOCK.unlock();
			HOLOS_LOCK.unlock();
		}

	}

	public static synchronized void remove(Player p) {
		try {
			HOLOS_LOCK.lock();
			if (HOLOS.containsKey(p)) {
				ArrayList<Hologram> holos = HOLOS.get(p);
				holos.forEach(hol -> hol.destroy(p));
				HOLOS.remove(p);
			}
		} finally {
			HOLOS_LOCK.unlock();
		}
	}
	public static void setHead(String skullOwner, Location loc) {
		Skull s = (Skull) loc.getBlock().getState();
		s.setSkullType(SkullType.PLAYER);
		s.setOwner(skullOwner);
		s.update();
	}

}
