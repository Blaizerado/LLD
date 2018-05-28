package at.ltd;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import at.ltd.adds.Cf;
import at.ltd.adds.nyrofun;
import at.ltd.adds.bansystem.BanSystem;
import at.ltd.adds.game.player.character.Character;
import at.ltd.adds.game.player.healing.Healing;
import at.ltd.adds.sql.SQLConnectionHandler;
import at.ltd.adds.sql.sqlutils.SQLChecker;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.sql.sqlutils.stats.SqlStats;
import at.ltd.adds.superuser.SuperUserCache;
import at.ltd.adds.ts3.TeamSpeak;
import at.ltd.adds.utils.LibLoader;
import at.ltd.adds.utils.PlayerSettings;
import at.ltd.adds.utils.ReloadHandler;
import at.ltd.adds.utils.ScoreBoardManager;
import at.ltd.adds.utils.TPS;
import at.ltd.adds.utils.Transaction;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.inventory.InventoryHandler;
import at.ltd.adds.utils.net.web.Web;
import at.ltd.adds.utils.threading.Worker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.threading.asyncthreadworker.SyncWork;
import at.ltd.adds.utils.threading.exception.ThreadExceprionHandler;
import at.ltd.adds.utils.threading.removeable.RemoveAble;
import at.ltd.adds.utils.time.events.TimeEvents;
import at.ltd.adds.utils.visual.ActionBar;
import at.ltd.adds.vault.VaultRegister;
import at.ltd.anticheat.Anticheat;
import at.ltd.command.CommandManager;
import at.ltd.command.CommandRegistry;
import at.ltd.events.EventAsyncWrapper;
import at.ltd.events.EventCreatureSpawnEvent;
import at.ltd.events.EventDamage;
import at.ltd.events.EventJoin;
import at.ltd.events.EventLeave;
import at.ltd.events.EventMoveDeath;
import at.ltd.events.EventTNT;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.StartUpTest;
import at.ltd.gungame.lager.Lager;
import at.ltd.gungame.maps.MapManager;
import at.ltd.gungame.round.vote.RoundVote;
import at.ltd.lobby.Lobby;

/**
 * Flargo Main
 * 
 * Aufgabe: LIBLOADER!
 * 
 * @author NyroForce
 * @since 27.01.2017
 * @version 1.0.1
 */
public class Main extends MainServer {

	protected static String prefix = null;
	protected static boolean devmode = false;

	protected static HashMap<String, String> config = new HashMap<>();
	protected static Plugin plugin;
	protected static JavaPlugin javaplugin;
	protected static File dataFolder;

	protected static Transaction transaction = null;
	protected static SQLConnectionHandler connectionHandler, ch;

	@ConfigAble(key = "", value = "pw")
	public static String password;
	@ConfigAble(key = "", value = "bn")
	public static String name;

	@Override
	public void onEnable() {
		Long start = System.currentTimeMillis();
		org.spigotmc.AsyncCatcher.enabled = false;

		Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer("START"));

		// General Data
		readServerConfig();
		plugin = Bukkit.getPluginManager().getPlugin("JoooooEzGG");
		javaplugin = this;
		dataFolder = getDataFolder();
		prefix = config.get("Prefix");
		devmode = Boolean.valueOf(config.get("Devmode"));
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new TPS(), 20, 20);

		MainServer.init();
		ReloadHandler.init();

		Cf.init();

		// TREADS
		AsyncThreadWorkers.init();
		RemoveAble.init();
		ThreadExceprionHandler.init();
		// TREADS

		LibLoader.init();

		ActionBar.setUp();
		// SQL
		Config.loadMyClass("MySQL", this).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("MySQL", this);
		});
		System.out.println(prefix + "Creating DB connections....");
		connectionHandler = new SQLConnectionHandler(45, "ltd-net.eu", "3306", name, name, password);
		ch = connectionHandler;
		System.out.println(prefix + "Done.");
		SQLCollection.sqlChecker = new SQLChecker();
		SqlStats.init();
		transaction = new Transaction();
		// SQL

		// EVENTS
		registerListener(new EventTNT());
		registerListener(new EventLeave());
		registerListener(new CommandManager());
		registerListener(new RoundVote());
		registerListener(new EventDamage());
		registerListener(new EventCreatureSpawnEvent());
		registerListener(new EventJoin());
		registerListener(SQLCollection.sqlChecker);
		registerListener(new EventAsyncWrapper());
		registerListener(new EventMoveDeath());
		// EVENTS

		// UTILS
		BanSystem.init();

		MapManager.registerAllMaps();
		CommandRegistry.reg();

		ScoreBoardManager.init();
		PlayerSettings.init();
		GunGame.start();
		Lobby.start();
		Character.init();
		Healing.init();

		Web.init();

		// SSHMain.init();
		SuperUserCache.init();
		VaultRegister.init();
		Lager.init();
		Anticheat.init();
		TimeEvents.init();
		InventoryHandler.init();
		TeamSpeak.init();

		Bukkit.getWorlds().forEach(w -> w.setGameRuleValue("announceAdvancements", "false"));

		StartUpTest.test();

		// UTILS

		long ms = System.currentTimeMillis() - start;
		float sec = ms / 1000.0f;
		System.out.println("Finish! (" + String.format("%.3f", sec) + " sec)");

	}

}
