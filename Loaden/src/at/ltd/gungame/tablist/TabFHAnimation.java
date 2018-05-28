package at.ltd.gungame.tablist;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.adds.utils.time.TimeUnit;
import at.ltd.gungame.GameUtils;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.ranks.GRankUtils;
import at.ltd.gungame.round.GameRound;
import at.ltd.gungame.round.vote.RoundVote;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;

public class TabFHAnimation {

	public static HashMap<Integer, String> text = new HashMap<>();
	public static int pos = 0;
	public static int maxSize = 0;
	public static String status = "[STATUS]";
	public static String curStatus;
	public static boolean reload = false;
	private static final NumberFormat formatter = new DecimalFormat("00");

	@ConfigAble(key = "animationTicksTab", value = "10")
	public static long animationTicksTab;
	@ConfigAble(key = "votingTab", value = "§6MapVoting, &7Zeit: '&r[TIME]&7'&r")
	private static String votingTab;
	@ConfigAble(key = "roundTab", value = "&7Karte: '&a[MAP]§7' &7Zeit: '&r[TIME]&7'&r")
	private static String roundTab;
	@ConfigAble(key = "nothingTab", value = "§cNichts, bitte warte...")
	private static String nothingTab;
	@ConfigAble(key = "playerInfoTab", value = "  &6Rang:&7 [GGRANG]&7, &6Kills:&7 [KILLS], &6Coins:&7 [COINS]  ")
	public static String playerInfoTab;

	@ConfigAble(key = "animationTab", value = "&cF;&cFL;&cFLA;&cFLAR;&cFLARG;&cFLARGO>>STRING")
	public static List<String> animationTab;

	public static void start() {
		Config.loadMyClass("Tablist", TabFHAnimation.class).setReloadAble(true).setReloadHandler(() -> {
			reload = true;
			animationTab = null;
			Config.loadMyClass("Tablist", TabFHAnimation.class);
			load();
			reload = false;
		});

		load();
		TimeUnit tab = new TimeUnit(true, 50, "TabList Animation (BukkitAsync)");
		new BukkitRunnable() {

			@Override
			public void run() {
				if (reload) {
					return;
				}
				TimeCounter.start(tab);
				try {

					if (pos == maxSize) {
						pos = -1;
					}

					curStatus = getStatus();

					pos++;

					try {
						for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
							animate(p);
						}
					} catch (Exception e) {
					}

				} catch (Exception e) {
					e.printStackTrace();
					TimeCounter.stop(tab);
				} finally {
					TimeCounter.stop(tab);
				}
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 0, animationTicksTab);
	}

	public static void load() {
		maxSize = 0;
		text.clear();
		for (String s : animationTab) {
			add(s, status, playerInfoTab, "");
		}
		maxSize = text.size();
		maxSize--;
	}

	public static void add(String firstHeaderLine, String secHeaderLine, String firstFooterLine, String secFooterLine) {
		String header = firstHeaderLine;
		String footer = firstFooterLine;
		if (secHeaderLine != null)
			header = header + "§r\n" + secHeaderLine;
		if (secFooterLine != null)
			footer = footer + "§r\n" + secFooterLine;

		String finish = header + "ξ" + footer;
		text.put(maxSize, ChatColor.translateAlternateColorCodes('&', finish));
		maxSize++;
	}

	public static void animate(Player p) {
		String[] text = get(p);
		String first = text[0];
		String sec = text[1];
		sendTab(p, first, sec);
	}

	public static String getStatus() {
		String st = null;
		GameStatus gs = Main.getGameStatus();

		if (gs == GameStatus.VOTE) {
			st = new String(votingTab).replace("[TIME]", getTimeLeftVote());
		} else if (gs == GameStatus.MATCH) {
			st = new String(roundTab).replace("[TIME]", getTimeLeftRound()).replace("[MAP]", GunGame.getRoundManager().getGameRound().getGameMap().getMapName());
		} else {
			st = nothingTab;
		}

		st = ChatColor.translateAlternateColorCodes('&', st);
		return st;
	}

	public static String getTimeLeftRound() {
		GameRound gr = GunGame.getRoundManager().getGameRound();
		int min = gr.getRoundTimeLeftMin();
		int sec = gr.getRoundTimeLeftSec();

		if (min > 5) {
			return "&2" + min + "&am";
		}

		if (min > 1) {
			return "&c" + min + "&am";
		} else {
			return "&4" + sec + "&as";
		}
	}

	public static String getTimeLeftVote() {
		int sec = RoundVote.secLeft;

		if (sec > 35) {
			return "&2" + sec + "&as";
		}

		if (sec > 15) {
			return "&c" + sec + "&as";
		} else {
			return "&4" + sec + "&as";
		}
	}

	public static String[] get(Player p) {
		String s = new String(text.get(pos));
		if (s.contains("[GGRANG]")) {
			s = s.replace("[GGRANG]", GRankUtils.getRank(SQLCollection.getPlayer(p).getKills()).getName());
		}
		if (s.contains("[NAME]")) {
			s = s.replace("[NAME]", p.getName());
		}
		if (s.contains("[GGLEVEL]")) {
			s = s.replace("[GGLEVEL]", formatter.format(GamePlayer.getGamePlayer(p).getGunGameLevel()) + "");
		}
		if (s.contains("[COINS]")) {
			s = s.replace("[COINS]", SQLCollection.getPlayer(p).getCoins() + "");
		}
		if (s.contains("[KILLS]")) {
			s = s.replace("[KILLS]", SQLCollection.getPlayer(p).getKills() + "");
		}
		if (s.contains("[PING]")) {
			Integer ping = ((CraftPlayer) p).getHandle().ping;
			s = s.replace("[PING]", ping.toString());
		}
		if (s.contains("[STATUS]")) {
			s = s.replace(status, curStatus);
		}
		if (s.contains("[STATUSBAR]")) {
			s = s.replace("[STATUSBAR]", GameUtils.getPlayerStatusBar(p));
		}
		if (s.contains("[PING]")) {
			s = s.replace("[PING]", ((CraftPlayer) p).getHandle().ping + "");
		}
		return s.split("ξ");
	}

	public static void sendTab(Player p, String header, String footer) {
		IChatBaseComponent tabHeader = ChatSerializer.a("{\"text\":\"" + header + "\"}");
		IChatBaseComponent tabFooter = ChatSerializer.a("{\"text\":\"" + footer + "\"}");

		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter();

		try {
			Field field_1 = headerPacket.getClass().getDeclaredField("a");
			field_1.setAccessible(true);
			field_1.set(headerPacket, tabHeader);

			Field field_2 = headerPacket.getClass().getDeclaredField("b");
			field_2.setAccessible(true);
			field_2.set(headerPacket, tabFooter);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CraftPlayer cp = (CraftPlayer) p;
			cp.getHandle().playerConnection.sendPacket(headerPacket);
		}

	}

}
