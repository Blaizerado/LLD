package at.ltd.events;

import java.net.InetSocketAddress;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.events.custom.EventPlayerPreJoinAsync;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.SQLQuery;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.sql.sqlutils.SQLManagePlayer;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventNewJoin;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.level.GLevelUtils;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.lobby.LobbyUtils;

public class EventJoin implements Listener {

	public static ArrayList<String> newjoiner = new ArrayList<>();
	public static final String downloadlink = "https://ltd-net.eu/data/mc/tex/tex-0-0-9.zip";

	@EventHandler(priority = EventPriority.HIGH)
	public void on(PlayerJoinEvent e) {
		final Player p = e.getPlayer();
		SQLPlayer sqlp = SQLCollection.getPlayer(p);
		sqlp.setName(p.getName());
		sqlp.setIP(SQLManagePlayer.getIP(p));
		sqlp.setRank(GRankPerm.getRank(p).getPerm());
		sqlp.setLastjoin(new Date(System.currentTimeMillis()));

		new GamePlayer(p);
		LobbyUtils.InitPlayerJoin(p);

		AsyncThreadWorkers.submitDelayedWorkSec(() -> {
			if (AsyncThreadWorkers.isPlayerOnline(p)) {
				Cf.rsS(Cf.TEX_DOWNLOAD, p, "[LINK]", downloadlink);
			}
		}, 13);

		boolean isnewjoiner = newjoiner.contains(p.getUniqueId().toString());
		if (isnewjoiner) {
			newjoiner.remove(p.getUniqueId().toString());
			AsyncThreadWorkers.submitWork(new Runnable() {

				@Override
				public void run() {
					SQLQuery query = Main.getSQLQuery();
					try {
						sqlp.save();
						ResultSet rs = query.querySQL("SELECT COUNT(UUID) FROM MC;");
						rs.next();
						NumberFormat nf = new DecimalFormat("000000");
						int i = rs.getInt("COUNT(UUID)");
						Cf.rsBC(Cf.JOIN_NEW_MESSAGE, "[DBID]", nf.format(i), "[PLAYER]", p.getName());
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
					}
				}
			});
			EventNewJoin enj = new EventNewJoin(p);
			Main.callEvent(enj);
		} else {
			Cf.rsBC(Cf.JOIN_MESSAGE, "[NAME]", p.getName());
		}
		e.setJoinMessage(null);

		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
			msd(p);
			p.setResourcePack(downloadlink);
			p.setLevel(0);
			p.setFlySpeed(0.2f);
			p.setHealth(20.00);
			p.setHealthScale(20.00);
			p.setGameMode(GameMode.ADVENTURE);
			p.setCollidable(true);
			p.setAllowFlight(false);
			p.setBedSpawnLocation(LobbyUtils.getLobbySpawnLocation());
			GLevelUtils.removeItems(p);

			AttributeInstance attribute = p.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
			attribute.setBaseValue(24D);
			AsyncThreadWorkers.submitWork(() -> p.saveData());
			LobbyUtils.tpPlayerToLobby(p);
			
		}, 5);

	}

	@EventHandler
	public void on(EventPlayerPreJoinAsync e) {
		boolean b = SQLManagePlayer.addPlayer(e.getUUID().toString(), "JOINING");
		if (!b) {
			newjoiner.add(e.getUUID().toString());
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onUpdate(PlayerJoinEvent e) {
		try {
			AsyncThreadWorkers.forceUpdate();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}

	@EventHandler
	public void on(EventNewJoin e) {
		Player p = e.getPlayer();
		AsyncThreadWorkers.submitWork(() -> {
			SQLQuery query = Main.createSQLQuery();
			try {
				query.updateSQL("INSERT INTO `IP` (`IP`, `UUID`, `MCNAME`) VALUES ('" + getIP(p) + "', '" + p.getUniqueId().toString() + "', '" + p.getCustomName() + "');");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (SQLException e1) {
				e1.printStackTrace();
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

	public static void msd(Player p) {
		for (int i = 1; i <= 10; i++) {
			p.sendMessage(" ");
		}
		GameStatus gs = Main.getGameStatus();
		p.sendMessage("§7╔═════════════════════════════════╗");
		p.sendMessage(" ");
		p.sendMessage("   §7 Willkommen, §e" + p.getName() + "§7!");
		p.sendMessage("   §7 Es sind gerade §e" + AsyncThreadWorkers.getOnlinePlayers().size() + " §7Spieler online.");
		p.sendMessage(" ");
		if (gs == GameStatus.MATCH) {
			new FancyMessage("   §7 Es läuft gerade eine Runde möchtest du beitreten? §2[KLICK]").command("/join").send(p);
		} else if (gs == GameStatus.NOTHING) {
			p.sendMessage("   §7 Es läuft gerade keine Runde oder Mapvoting bitte warte ein    bisschen.");
		} else if (gs == GameStatus.VOTE) {
			new FancyMessage("   §7 Es läuft gerade ein Mapvoting möchtest du voten? §2[KLICK]").command("/vote").send(p);
		}
		p.sendMessage("   §7➢ §cNächstes Event: §e?");
		p.sendMessage(" ");
		p.sendMessage(" §7╚═════════════════════════════════╝");
	}

}
