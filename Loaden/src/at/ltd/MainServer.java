package at.ltd;

import java.io.File;
import java.net.URL;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import at.ltd.adds.bansystem.BanSystem;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.SQLConnectionHandler;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.sql.sqlutils.stats.round.RoundData;
import at.ltd.adds.sql.sqlutils.stats.round.RoundStatsWatcher;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.ReloadHandler;
import at.ltd.adds.utils.Transaction;
import at.ltd.adds.utils.TransactionStatus;
import at.ltd.adds.utils.config.ConfigManager;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.gungame.GameUtils;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.round.GameRound;
import at.ltd.gungame.round.RoundManager;

/**
 * 
 * @author NyroForce
 * @since 16.12.2017
 * @version 1.0.1
 */
public class MainServer extends JavaPlugin {

	private static final SecureRandom random = new SecureRandom();

	protected static void init() {

	}

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	public static void readServerConfig() {
		Main.config.clear();
		String configLoc = "plugins/LTD/Config.yml";
		File f = new File(configLoc);
		if (!f.exists()) {
			try {
				URL url = new URL("https://ltd-net.eu/data/rd/them/LtdConfig.yml");
				@SuppressWarnings("resource")
				String s = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A").next();
				ConfigManager.setRAW(s, configLoc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		HashMap<String, String> dd = ConfigManager.readConfig(configLoc);
		for (String s : dd.keySet()) {
			String value = dd.get(s);
			Main.config.put(s, ChatColor.translateAlternateColorCodes('&', value));
		}
	}

	public static void broadcastMessageGlobal(String msg) {
		AsyncThreadWorkers.getOnlinePlayers().forEach(p -> p.sendMessage(msg));
	}

	public static void broadcastMessageRound(String msg) {
		getPlayersInRound().forEach(p -> p.sendMessage(msg));
	}

	public static List<Player> getPlayersInRound() {
		ArrayList<Player> list = new ArrayList<>();
		GamePlayer.getGunGamePlayersInRound().forEach(p -> {
			list.add(p.getPlayer());

		});
		return list;
	}

	public static TransactionStatus depositPlayer(Player p, int coins) {
		return Main.transaction.add("SYSTEM", coins, p);
	}

	public static TransactionStatus withdrawPlayer(Player p, int coins) {
		return Main.transaction.minus("SYSTEM", coins, p);
	}

	public static int getCoinsFromPlayer(Player p) {
		return SQLCollection.getPlayer(p.getPlayer()).getCoins();
	}

	public static GamePlayer getGunGamePlayer(Player p) {
		return GamePlayer.getGamePlayer(p);
	}

	public static SQLQuery getSQLQuery() {
		return Main.ch.createSQLQuery();
	}

	public static void submitAsyncWork(Runnable run) {
		AsyncThreadWorkers.submitWork(run);
	}

	public static Listener registerListener(Listener listener) {
		Bukkit.getServer().getPluginManager().registerEvents(listener, Main.plugin);
		return listener;
	}

	public static Listener registerListener(Class<?> listenerclass) {
		Listener listener = null;
		try {
			listener = (Listener) listenerclass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		Bukkit.getServer().getPluginManager().registerEvents(listener, Main.plugin);
		return listener;
	}

	
	public static void callEvent(Event event) {
		Bukkit.getPluginManager().callEvent(event);
	}

	public static GunInterface getGunById(int id) {
		return GunUtils.getGunInterfaceByID(id);
	}

	public static Gun getGunByItem(ItemStack is) {
		return GunUtils.getGunMemoryManaged(is);
	}

	public static RoundData getRoundData() {
		return RoundStatsWatcher.currenRound;
	}

	public static Gun createGun(GunInterface gi, Player p, int kills) {
		return GunUtils.createGun(gi, p, kills);
	}

	public static ItemStack createAmmo(int ammound) {
		return BulletUtils.createBullet(ammound);
	}

	public static String getPrefix() {
		return Main.prefix;
	}

	public static boolean isDevModeEnabled() {
		return Main.devmode;
	}

	public static Plugin getPlugin() {
		return Main.plugin;
	}

	public static Transaction getTransaction() {
		return Main.transaction;
	}

	public static SQLConnectionHandler getConnectionHandler() {
		return Main.connectionHandler;
	}

	public static HashMap<String, String> getServerConfig() {
		return Main.config;
	}

	public static File getServerDataFolder() {
		return Main.dataFolder;
	}

	public static JavaPlugin getJavaPlugin() {
		return Main.javaplugin;
	}

	public static void setPreifx(String prefix) {
		Main.prefix = prefix;
	}

	public static boolean hasSuperUser(Player p) {
		return SuperUser.isSuperUser(p);
	}

	public static boolean hasBuildMode(Player p) {
		return CommandBuildMode.canBuild(p);
	}

	public static boolean isBanned(String uuid) {
		if (Bukkit.isPrimaryThread()) {
			throw new IllegalAccessError("Only on a async thread!");
		}
		BanStatus bs = BanUtils.getBanStatus(uuid);
		return !bs.equals(BanStatus.NO_BAN);

	}

	public static void banPlayer(Player p) {
		BanSystem.banPlayer(p, null);
	}

	public static void banPlayer(Player p, String reason) {
		BanSystem.banPlayer(p, reason);
	}

	public static void banPlayer(String uuid) {
		BanSystem.banPlayer(uuid, null);
	}

	public static void banPlayer(String uuid, String reason) {
		BanSystem.banPlayer(uuid, reason);
	}

	public static void timeBanPlayer(Player p, long time) {
		BanSystem.tempBanPlayer(p, null, time);
	}

	public static void timeBanPlayer(Player p, long time, String reason) {
		BanSystem.tempBanPlayer(p, reason, time);
	}

	public static boolean isInRound(Player p) {
		return GamePlayer.isInRound(p);
	}

	public static boolean isMainThread() {
		return Bukkit.isPrimaryThread();
	}

	public static boolean isOnlineUUID(String uuid) {
		return AsyncThreadWorkers.isPlayerOnline(uuid);
	}

	public static boolean isOnlineUUID(UUID uuid) {
		return AsyncThreadWorkers.isPlayerOnline(uuid.toString());
	}

	public static boolean isOnlineName(String name) {
		return Bukkit.getPlayer(name) != null;
	}

	public static SQLPlayer getSQLPlayer(Player p) {
		return SQLCollection.getPlayer(p);
	}

	public static SQLQuery createSQLQuery() {
		return Main.ch.createSQLQuery();
	}

	public static GameStatus getGameStatus() {
		return GameUtils.getServerStatus();
	}

	public static GameRound getGameRound() {
		return GunGame.getRoundManager().getGameRound();
	}

	public static RoundManager getRoundManager() {
		return GunGame.getRoundManager();
	}

	public static boolean doesUUIDExist(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getNameFromUUID(String uuid) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE `UUID` LIKE '" + uuid + "'");
			if (rs.next()) {
				return rs.getString("NAME");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getUUIDFromName(String name) {
		SQLQuery query = Main.getSQLQuery();
		try {
			ResultSet rs = query.querySQL("SELECT * FROM `MC` WHERE LOWER(`NAME`) LIKE LOWER('" + name + "')");
			if (rs.next()) {
				return rs.getString("UUID");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void addReloadHandler(Runnable run) {
		ReloadHandler.addReloadHandler(run);
	}

	public static void removeReloadHandler(Runnable run) {
		ReloadHandler.removeReloadHandler(run);
	}

}
