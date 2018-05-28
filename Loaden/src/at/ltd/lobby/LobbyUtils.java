package at.ltd.lobby;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.external.Cuboid;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventConfigReload;
import at.ltd.events.custom.EventTeleportSpawn;

public class LobbyUtils {

	@ConfigAble(key = "LOBBY_WORLD_NAME", value = "GG_Lobby")
	public static String Lobby_Name_String;

	@ConfigAble(key = "CUBE_LOC_1", value = "-2008.0*1.0*1482.0*0.0*0.0*GG_Lobby")
	public static Location LOBBYCUB_1;

	@ConfigAble(key = "CUBE_LOC_2", value = "-2202.0*106.0*1236.0*0.0*0.0*GG_Lobby")
	public static Location LOBBYCUB_2;

	@ConfigAble(key = "LOBBY_SPAWN", value = "-2027.525*31.0*1374.803*-269.670*-2.1000*GG_Lobby")
	public static Location Lobby_Spawn_Location;

	public static World Lobby_World;
	public static Cuboid LOBBY_CUBE;

	public static void init() {
		Config.loadMyClass("Lobby", LobbyUtils.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("Lobby", LobbyUtils.class);
			LOBBY_CUBE = new Cuboid(LOBBYCUB_1, LOBBYCUB_2);
			Lobby_World = Bukkit.getWorld(Lobby_Name_String);
		});
		LOBBY_CUBE = new Cuboid(LOBBYCUB_1, LOBBYCUB_2);
		Lobby_World = Bukkit.getWorld(Lobby_Name_String);
		Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void on(EventConfigReload e) {
				Config.getInstance("Lobby").load();
				Config.loadMyClass("Lobby", LocUtils.class);
			}
		}, Main.getPlugin());
	}

	public static Boolean tpPlayerToLobby(Player p) {
		if (!Bukkit.isPrimaryThread()) {
			throw new IllegalAccessError("Only on the Main thread!");
		}
		EventTeleportSpawn ets = new EventTeleportSpawn(p, GamePlayer.getGamePlayer(p));
		Bukkit.getPluginManager().callEvent(ets);
		if (ets.isCancelled()) {
			return false;
		}
		p.teleport(Lobby_Spawn_Location);
		return true;
	}

	public static void tpPlayerToLobbyAsync(Player p) {
		if (AsyncThreadWorkers.isPlayerOnline(p)) {
			AsyncThreadWorkers.submitSyncWork(() -> tpPlayerToLobby(p));
		}
	}

	public static Boolean isInLobby(Player p) {
		Location loc = AsyncThreadWorkers.getPlayerLocation(p);
		if (LocUtils.sameWorld(loc, Lobby_Spawn_Location)) {
			return LOBBY_CUBE.contains(loc);
		} else {
			return false;
		}
	}

	public static boolean isLocationInLobby(Location loc) {
		return LocUtils.sameWorld(loc, Lobby_Spawn_Location) && LOBBY_CUBE.contains(loc);
	}

	public static void broadcast(final String msg) {
		getAllPlayersInLobby().forEach(p -> p.sendMessage(msg));
	}

	public static List<Player> getAllPlayersInLobby() {
		ArrayList<Player> ar = new ArrayList<>();
		AsyncThreadWorkers.getOnlinePlayers().forEach(p -> {
			if (LOBBY_CUBE.contains(AsyncThreadWorkers.getPlayerLocation(p))) {
				ar.add(p);
			}
		});
		return ar;
	}

	public static String getLobbyName() {
		return Lobby_Name_String;
	}

	public static World getLobbyWorld() {
		return Lobby_World;
	}

	public static Location getLobbySpawnLocation() {
		return Lobby_Spawn_Location;
	}

	public static boolean InitPlayerJoin(Player p) {
		return LobbyUtils.tpPlayerToLobby(p);

	}

	public static boolean isSameWorld(Player p) {
		return AsyncThreadWorkers.getPlayerLocation(p).getWorld().getName().equals(Lobby_Name_String);
	}

}
