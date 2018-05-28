package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.Lg;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.LocUtils;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.GameUtils;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.gungame.maps.MapManager;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.lobby.LobbyUtils;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;

public class CommandMaps implements CommandExecuter, Listener {

	public static HashMap<Inventory, Integer> invs = new HashMap<>();
	public static HashMap<Player, GameMap> visitor = new HashMap<>();
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		GamePlayer ggp = new GamePlayer(p);
		if (ggp.isInRound()) {
			Cf.rsS(Cf.MAPS_IN_ROUND, p);
			return;
		}
		openInv(1, p, null);

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Main.registerListener(this);
	}

	public static void openInv(int site, Player p, Inventory inv) {

		boolean update = (inv != null);

		int freeslots = 18;

		int displaymapsfrom = (freeslots * site) - freeslots;

		int mapnummber = 0;

		if (site == 1) {
			displaymapsfrom = 0;
		}

		if (inv == null) {
			p.closeInventory();
			inv = Bukkit.createInventory(p, 27, "MAP_LIST_" + site);
			invs.put(inv, Integer.valueOf(site));
		} else {
			inv.clear();
		}

		int slot = 0;

		for (GameMap gi : MapManager.MAPS) {

			if (displaymapsfrom == mapnummber || mapnummber > displaymapsfrom) {
				if (freeslots == 0) {
					break;
				}
				inv.setItem(slot, getIs(gi.getIcon(), "Map: " + gi.getMapName(), null));
				slot++;
				freeslots--;
			}

			mapnummber++;
		}

		inv.setItem(18, getIs(Material.REDSTONE_BLOCK, "§cBACK", "§7Eine Seite zurück."));
		inv.setItem(22, getIs(Material.BOAT, "§4Seite 1", "§7Zur ersten Seite."));
		inv.setItem(26, getIs(Material.EMERALD_BLOCK, "§aNEXT", "§7Eine Seite weiter."));

		inv.setItem(19, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(20, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(21, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(23, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(24, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(25, getIs(Material.GLASS, "§cNIX", null));

		if (!update) {
			p.openInventory(inv);
		} else {
			update(p, "MAP_LIST_" + site);
			p.updateInventory();
		}

	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getInventory().getName().startsWith("MAP_LIST_")) {
			int id = invs.get(e.getInventory());
			Player p = (Player) e.getWhoClicked();
			ItemStack is = e.getCurrentItem();
			if (e.getRawSlot() > 26) {
				return;
			}
			if (is != null) {
				if (is.getType() == Material.REDSTONE_BLOCK) {
					if (id == 1) {
						e.setCancelled(true);
						return;
					} else {
						id--;
						invs.replace(e.getInventory(), Integer.valueOf(id));
						openInv(id, p, e.getInventory());
						p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 2, 10);
					}
				}

				if (is.getType() == Material.EMERALD_BLOCK) {
					id++;
					invs.replace(e.getInventory(), Integer.valueOf(id));
					openInv(id, p, e.getInventory());
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 2, 10);
				}
				if (is.getType() == Material.BOAT) {
					id = 1;
					invs.replace(e.getInventory(), Integer.valueOf(id));
					openInv(id, p, e.getInventory());
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 2, 10);
				}
				if (is.getType() == Material.AIR) {
					e.setCancelled(true);
					return;
				}

				if (is.hasItemMeta()) {
					if (is.getItemMeta().hasDisplayName()) {
						if (is.getItemMeta().getDisplayName().contains("Map:")) {
							String mapname = ChatColor.stripColor(is.getItemMeta().getDisplayName().replace("Map: ", ""));
							GameMap finalgm = MapManager.getMapByName(mapname);

							if (finalgm == null) {
								Lg.lgError("NO MAP CommandMaps MAPNAME: " + mapname);
								p.sendMessage(Main.getPrefix() + "ERROR");
							}

							if (GameUtils.getServerStatus() == GameStatus.MATCH) {
								if (finalgm == GameUtils.getMap()) {
									Cf.rsS(Cf.MAPS_MAP_IS_ROUND, p);
									e.setCancelled(true);
									return;
								}
							}

							Location loc = LocUtils.getLocWithoutPlayers(finalgm.getSpawns(), 20);
							EventTeleport teleport = new EventTeleport(p, loc, "MAPS");
							Bukkit.getPluginManager().callEvent(teleport);
							if (!teleport.isCancelled()) {
								if (visitor.containsKey(p)) {
									visitor.remove(p);
								}
								p.setGameMode(GameMode.ADVENTURE);
								p.setAllowFlight(true);
								p.setFlying(true);
								p.teleport(loc);
								visitor.put(p, finalgm);
								Cf.rsS(Cf.MAPS_TP_MSG, p);
								p.closeInventory();
							} else {
								Cf.rsS(Cf.MAPS_NO_TELEPORT, p);
								e.setCancelled(true);
								return;
							}
						}
					}
				}

			}
			e.setCancelled(true);

		}

	}

	public static ItemStack getIs(Material material, String name, String... lore) {
		ItemStack is = new ItemStack(material);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		if (lore != null) {
			im.setLore(Arrays.asList(lore));
		}
		is.setItemMeta(im);
		return is;

	}

	public static ItemStack getIs(ItemStack isold, String name, String... lore) {
		ItemStack is = isold.clone();
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		if (lore != null) {
			im.setLore(Arrays.asList(lore));
		} else {
			im.setLore(new ArrayList<String>());
		}
		is.setItemMeta(im);
		return is;

	}

	@EventHandler
	public void on(InventoryCloseEvent e) {
		if (e.getInventory().getName().startsWith("MAP_LIST_")) {
			if (invs.containsKey(e.getInventory())) {
				invs.remove(e.getInventory());
			}
		}
	}

	@EventHandler
	public void on(EventTeleport e) {
		if (!e.getReason().equals("MAPS")) {
			if (visitor.containsKey(e.getPlayer())) {
				visitor.remove(e.getPlayer());
				Player p = e.getPlayer();
				p.setAllowFlight(false);
				p.setFlying(false);
			}
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		if (visitor.containsKey(e.getPlayer())) {
			visitor.remove(e.getPlayer());
			Player p = e.getPlayer();
			p.setAllowFlight(false);
			p.setFlying(false);
		}
	}

	@EventHandler
	public void on(EventJoinGame e) {
		if (visitor.containsKey(e.getPlayer())) {
			visitor.remove(e.getPlayer());
			Player p = e.getPlayer();
			p.setAllowFlight(false);
			p.setFlying(false);
		}
	}

	@EventHandler
	public void on(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (visitor.containsKey(p)) {
			if (!e.getTo().getWorld().getName().equals(visitor.get(p).getSpawns().get(1).getWorld().getName())) {
				Cf.rsS(Cf.MAPS_RUN_AWAY, p);
				LobbyUtils.tpPlayerToLobby(p);
				p.setAllowFlight(false);
				p.setFlying(false);
				visitor.remove(e.getPlayer());
			}
		}
	}

	@EventHandler
	public void on(EventGameRoundStart e) {
		ArrayList<Player> gamers = e.getPlayers();
		ArrayList<Player> remove = e.getPlayers();
		for (Player p : visitor.keySet()) {
			GamePlayer ggp = GamePlayer.getGamePlayer(p);
			if (!gamers.contains(ggp.getPlayer())) {
				if (visitor.get(p) == e.getGameMap()) {
					LobbyUtils.tpPlayerToLobby(p);
					p.setAllowFlight(false);
					p.setFlying(false);
					remove.add(p);
					return;
				}
			}
		}
		for (Player p : remove) {
			visitor.remove(p);
		}

	}

	public static void update(Player p, String title) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage(title), p.getOpenInventory().getTopInventory().getSize());
		ep.playerConnection.sendPacket(packet);
	}

}
