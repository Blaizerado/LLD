package at.ltd.command.NormalCommands;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.BanTimeUtils;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandPlayerInfo implements CommandExecuter, Listener {

	public static final String INV_NAME = "§7PlayerInfo";

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isTeamMember(p)) {
			return;
		}
		p.sendMessage(Main.getPrefix() + "Beginne suche in der Datenbank...");
		if (args.size() == 2) {
			String traget = args.get(1);
			if (traget.length() == 36) {
				if (Main.doesUUIDExist(traget)) {
					SQLPlayer player = new SQLPlayer(traget, Main.getConnectionHandler());
					player.load();
					manage(p, player);
				} else {
					p.sendMessage(Main.getPrefix() + "§cDiese UUID ist der Datenbank nicht bekannt.");
				}
				return;
			}

			String uuid = Main.getUUIDFromName(traget);
			if (uuid != null) {
				SQLPlayer player = new SQLPlayer(uuid, Main.getConnectionHandler());
				player.load();
				manage(p, player);
				return;
			}

			Cf.rsS(Cf.PLAYER_NOT_FOUND, p);
		} else {
			p.sendMessage(Main.getPrefix() + "/playerinfo (<uuid> <name>)");
		}
	}

	public static void manage(Player p, SQLPlayer sql) throws Exception {
		String uuid = sql.getUUID();
		BanStatus bs = BanUtils.getBanStatus(uuid);
		Inventory inv = Bukkit.createInventory(p, 9, INV_NAME + " " + sql.getName());
		InventoryAnimation animation = new InventoryAnimation();
		animation.put(0, getGeneraleInfo(sql));
		animation.put(1, getBanItem(sql, bs));
		animation.put(2, getIPInfoItem(sql));
		animation.animate(inv, AnimationType.LEFT_TO_RIGHT, false, 2, ()->{});

		AsyncThreadWorkers.submitSyncWork(() -> p.openInventory(inv));
		p.sendMessage(Main.getPrefix() + "Fertig.");
		new FancyMessage(Main.getPrefix() + "§a*Für die UUID klicke hier. (STRG + A, STRG + C)*").suggest(sql.getUUID()).send(p);
	}

	public static ItemStack getIPInfoItem(SQLPlayer sql) {
		String ip = sql.getIP();
		return ItemUtils.generateItemStack(Material.BOOK_AND_QUILL, "§cIP-Info *Kick*", "§6IP: §7" + ip);
	}

	public static ItemStack getBanItem(SQLPlayer sql, BanStatus bs) throws Exception {
		String uuid = sql.getUUID();
		if (bs == BanStatus.BANNED) {
			String reason = BanUtils.getBanReason(uuid);
			String uuidfrombanner = BanUtils.getUUIDFromBanner(uuid);
			SQLPlayer banner = null;
			if (uuidfrombanner != null) {
				banner = new SQLPlayer(uuidfrombanner, Main.getConnectionHandler());
				banner.load();
			}
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§cPermanent Gebannt");
			if (banner != null) {
				lore.add("§6Grbannt von:§7 " + banner.getName());
			}

			if (reason == null) {
				lore.add("§6Kein Grund angegeben.");
			} else {
				lore.add("§6Grund:§7 " + reason);
			}
			lore.add("§a*fürs enbannen einfach aufs Item klicken*");
			return ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§4Bannstatus", lore);
		}
		if (bs == BanStatus.TIME_BANNED) {
			String reason = BanUtils.getBanReason(uuid);
			String uuidfrombanner = BanUtils.getUUIDFromBanner(uuid);
			SQLPlayer banner = null;
			if (uuidfrombanner != null) {
				banner = new SQLPlayer(uuidfrombanner, Main.getConnectionHandler());
				banner.load();
			}
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§eZeitlich Gebannt");
			if (banner != null) {
				lore.add("§6Grbannt von:§7 " + banner.getName());
			}
			lore.add("§6Restzeit:§7 " + BanTimeUtils.getTime(BanTimeUtils.getBanTime(uuid)));

			if (reason == null) {
				lore.add("§6Kein Grund angegeben.");
			} else {
				lore.add("§6Grund:§7 " + reason);
			}
			lore.add("§a*Fürs enbannen einfach aufs Item klicken.*");
			return ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§4Bannstatus", lore);
		}

		if (bs == BanStatus.NO_BAN) {
			return ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§4Bannstatus", "§aKein Banneintrag vorhanden.");
		}
		return null;
	}

	public static ItemStack getGeneraleInfo(SQLPlayer sql) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String lastjoin = df.format(sql.getLastjoin());
		String firstjoin = df.format(sql.getFirstjoin());

		return ItemUtils.generateItemStack(Material.NAME_TAG, "§cDaten", "§6UUID:§7 " + sql.getUUID(), "§6Name:§7 " + sql.getName(), "§6Coins:§7 " + sql.getCoins(), "§6IP:§7 " + sql.getIP(), "§6Kills:§a " + sql.getKills(), "§6Tode:§c " + sql.getDeaths(), "§6Lastjoin:§7 " + lastjoin, "§6Firstjoin:§7 " + firstjoin, "§6Spielzeit:§7 " + sql.getPlaytime() + "§6 Minuten");

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Main.registerListener(this);
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getInventory().getName().contains(INV_NAME)) {
			e.setCancelled(true);
			if (ItemUtils.getItemName(e.getCurrentItem()).contains("Bannstatus")) {
				String name = e.getInventory().getName().replace(INV_NAME + " ", "");
				AsyncThreadWorkers.submitWork(() -> {
					String uuid = Main.getUUIDFromName(name);
					BanStatus bs = BanUtils.getBanStatus(uuid);
					if (bs == BanStatus.BANNED | bs == BanStatus.TIME_BANNED) {
						AsyncThreadWorkers.submitSyncWork(() -> {
							e.getView().close();
							new FancyMessage(Main.getPrefix() + "§cMöchtest du ihn wirklich entbannen? *klick* §7[§aJA§7] ").command("/unbann " + uuid).send(e.getWhoClicked());
						});

					}

				});

			}

			if (ItemUtils.getItemName(e.getCurrentItem()).contains("§cIP-Info *Kick*")) {
				String name = e.getInventory().getName().replace(INV_NAME + " ", "");
				AsyncThreadWorkers.submitWork(() -> {
					String uuid = Main.getUUIDFromName(name);
					SQLPlayer player = new SQLPlayer(uuid, Main.getConnectionHandler());
					try {
						player.load();
					} catch (ClassNotFoundException | SQLException e1) {
						e1.printStackTrace();
					}
					String ip = player.getIP();
					AsyncThreadWorkers.submitSyncWork(() -> e.getWhoClicked().closeInventory());
					e.getWhoClicked().sendMessage(Main.getPrefix() + "§chttps://ipinfo.io/" + ip);
				});
			}
		}
	}
	@EventHandler
	public void on(InventoryDragEvent e) {
		if (e.getInventory().getName().contains(INV_NAME)) {
			e.setCancelled(true);
		}
	}

}
