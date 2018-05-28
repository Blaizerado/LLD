package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunRegister;
import at.ltd.gungame.guns.utils.GunUtils;
import net.minecraft.server.v1_12_R1.ChatMessage;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutOpenWindow;

public class CommandGuns implements CommandExecuter, Listener {

	public static HashMap<Inventory, Integer> invs = new HashMap<>();

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (SuperUser.isSuperUser(p)) {
			openInv(1, p, null);
		} else {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Main.registerListener(this);
	}

	public static void openInv(int site, Player p, Inventory inv) {

		boolean update = (inv != null);

		int freeslots = 18;

		int displaygunsfrom = (freeslots * site) - freeslots;

		int gunnumber = 0;

		if (site == 1) {
			displaygunsfrom = 0;
		}

		if (inv == null) {
			p.closeInventory();
			inv = Bukkit.createInventory(p, 27, "GUN_LIST_" + site);
			invs.put(inv, Integer.valueOf(site));
		} else {
			inv.clear();
		}

		int slot = 0;

		for (GunInterface gi : GunRegister.GUN_LIST.values()) {

			if (displaygunsfrom == gunnumber || gunnumber > displaygunsfrom) {
				if (freeslots == 0) {
					break;
				}
				inv.setItem(slot, GunUtils.createGun(gi, p, 1000).getItemStack());
				slot++;
				freeslots--;
			}

			gunnumber++;
		}

		inv.setItem(18, getIs(Material.REDSTONE_BLOCK, "§cBACK", "§7Eine Seite zurück."));
		inv.setItem(20, getIs(Material.FIREBALL, "§6CLEAR", "§7Reinigt dein Inventar."));
		inv.setItem(22, getIs(Material.BOAT, "§4Seite 1", "§7Zur ersten Seite."));
		inv.setItem(26, getIs(Material.EMERALD_BLOCK, "§aNEXT", "§7Eine Seite weiter."));

		inv.setItem(19, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(21, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(23, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(24, getIs(Material.GLASS, "§cNIX", null));
		inv.setItem(25, getIs(Material.GLASS, "§cNIX", null));

		if (!update) {
			p.openInventory(inv);
		} else {
			update(p, "GUN_LIST_" + site);
			p.updateInventory();
		}

	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getInventory().getName().startsWith("GUN_LIST_")) {
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
				if (is.getType() == Material.FIREBALL) {
					p.getInventory().clear();
					p.playSound(p.getLocation(), Sound.BLOCK_COMPARATOR_CLICK, 2, 10);
				}

				if (GunUtils.isGun(is)) {
					GunInterface gi = GunUtils.searchGunByItem(is);
					p.getInventory().addItem(GunUtils.createGun(gi, p, 5).getItemStack());
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BELL, 2, 10);
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

	@EventHandler
	public void on(InventoryCloseEvent e) {
		if (e.getInventory().getName().startsWith("GUN_LIST_")) {
			invs.remove(e.getInventory());
		}
	}

	public static void update(Player p, String title) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow packet = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, "minecraft:chest", new ChatMessage(title), p.getOpenInventory().getTopInventory().getSize());
		ep.playerConnection.sendPacket(packet);
	}

}
