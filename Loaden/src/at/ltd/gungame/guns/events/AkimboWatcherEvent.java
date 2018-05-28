package at.ltd.gungame.guns.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGunGameSpawn;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;

public class AkimboWatcherEvent implements Listener {

	@EventHandler
	public void on(EventGunGameSpawn e) {
		e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
	}

	@EventHandler
	public void on(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		ItemStack is = e.getPlayer().getInventory().getItem(e.getNewSlot());
		if (is != null) {
			GunInterface gun = GunUtils.getGunInterfaceByItemManaged(is);
			if (gun == null) {
				return;
			}
			if (gun.isAkimbo()) {
				ItemStack akis = new ItemStack(gun.getItem().getType());
				akis.setDurability(gun.getItem().getDurability());
				p.getInventory().setItemInOffHand(akis);
				return;
			}

		}
		p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));

	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(PlayerDropItemEvent e) {
		ItemStack is = e.getItemDrop().getItemStack();
		GunInterface gi = GunUtils.getGunInterfaceByItemManaged(is);
		if (gi != null) {
			if (gi.isAkimbo()) {
				e.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.AIR));
			}
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getInventory().getType() != InventoryType.PLAYER) {
			if (e.getSlot() == 40) {
				e.setCancelled(true);
			}
		}

	}

	public static void init() {
		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				boolean bol1 = false;
				ItemStack is = p.getInventory().getItemInMainHand();
				if (is != null && is.getType() != Material.AIR) {
					GunInterface gi = GunUtils.getGunInterfaceByItemManaged(is);
					if (gi != null) {
						if (!gi.isAkimbo()) {
							bol1 = true;
						} else {
							if (!isOffHand(p.getInventory().getItemInOffHand(), gi)) {
								bol1 = true;
							}
						}
					} else {
						bol1 = true;
					}

				} else {
					bol1 = true;
				}

				if (bol1) {
					p.getInventory().setItemInOffHand(new ItemStack(Material.AIR));

				}

			}

		}, 18, 10);

		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				if (p.getInventory().getItemInMainHand() != null) {
					ItemStack offhand = p.getInventory().getItemInOffHand();
					if (offhand == null | offhand.getType() == Material.AIR) {
						ItemStack is = p.getInventory().getItemInMainHand();
						Gun gun = GunUtils.getGunMemoryManaged(is);
						if (gun != null) {
							if (gun.getGunInterface().isAkimbo()) {
								setOffHand(gun.getGunInterface(), p);
							}
						}
					}
				}
			}
		}, 20, 5);
	}

	public static void setOffHand(GunInterface gi, Player p) {
		ItemStack akis = new ItemStack(gi.getItem().getType());
		akis.setDurability(gi.getItem().getDurability());
		AsyncThreadWorkers.submitSyncWork(() -> {
			p.getInventory().setItemInOffHand(akis);
		});

	}

	public static boolean isOffHand(ItemStack is, GunInterface gi) {
		ItemStack akis = new ItemStack(gi.getItem().getType());
		akis.setDurability(gi.getItem().getDurability());
		return is.isSimilar(akis);
	}

}
