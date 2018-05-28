package at.ltd.lobby.infoitem;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import at.ltd.Main;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerInteractAsync;
import at.ltd.events.custom.EventTeleport;
import at.ltd.events.custom.EventTeleportSpawn;
import at.ltd.lobby.LobbyUtils;

public class LobbyInfoItem implements Listener {

	private static String itemname = "§6§lFlargo-Spielmenü";

	public static void init() {
		Main.registerListener(new LobbyInfoItem());
		AsyncThreadWorkers.submitSchedulerWorkSec(() ->{
			for(Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				if(LobbyUtils.isInLobby(p)) {
					placeFirstItem(p);
				}else {
					removeFirstItem(p);
				}
			}
		}, 2, 2);
		LobbyMenu.init();
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () ->{
			placeFirstItem(e.getPlayer());
		}, 21);
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		removeFirstItem(e.getPlayer());
	}

	@EventHandler
	public void on(EventTeleport e) {
		if (!LobbyUtils.isLocationInLobby(e.getLocation())) {
			removeFirstItem(e.getPlayer());
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (isInvItem(e.getCurrentItem())) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(PlayerDropItemEvent e) {
		if (itemname.equals(ItemUtils.getItemName(e.getItemDrop().getItemStack()))) {
			e.setCancelled(true);
		}
		
	}

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventTeleportSpawn e) {
		if (!e.isCancelled()) {
			placeFirstItem(e.getPlayer());
		}
	}

	@EventHandler
	public void on(EventPlayerInteractAsync e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_AIR | e.getAction() == Action.RIGHT_CLICK_AIR) {
			if (LobbyUtils.isInLobby(p)) {
				if (isInvItem(e.getItem())) {
					AsyncThreadWorkers.submitSyncWork(() -> LobbyMenu.open(p));
				}
			}
		}

	}

	public static void removeFirstItem(Player p) {
		PlayerInventory inv = p.getInventory();
		ItemStack is = inv.getItem(0);
		if (is != null) {
			if (isInvItem(is)) {
				inv.remove(is);
			}
		}
	}

	public static void placeFirstItem(Player p) {
		PlayerInventory inv = p.getInventory();
		ItemStack is = inv.getItem(0);
		if(Main.isInRound(p)) {
			return;
		}
		if (is != null) {
			if (isInvItem(is)) {
				return;
			}
			if (invFull(p)) {
				p.sendMessage(Main.getPrefix()
						+ "Leider war dein Inventar voll weswegen wir das erste Item entfernen mussten.");
			} else {
				inv.remove(is);
				inv.setItem(0, ItemUtils.generateItemStack(Material.NETHER_STAR, itemname));
				inv.addItem(is);
			}
		}else {
			inv.setItem(0, ItemUtils.generateItemStack(Material.NETHER_STAR, itemname));
		}
	}

	public static boolean isInvItem(ItemStack is) {
		return itemname.equals(ItemUtils.getItemName(is));
	}

	public static boolean invFull(Player p) {
		return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
	}

}
