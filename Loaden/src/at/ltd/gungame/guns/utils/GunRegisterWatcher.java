package at.ltd.gungame.guns.utils;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import at.ltd.Main;
import at.ltd.adds.Lg;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.GameUtils.GameStatus;
import at.ltd.gungame.events.custom.EventGameRoundStart;
import at.ltd.gungame.events.custom.EventGameRoundStop;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventGunGameLevelSet;

public class GunRegisterWatcher implements Listener {

	public static void init() {
		tickSearcher();
	}

	public static void tickSearcher() {

		new BukkitRunnable() {
			public void run() {
				if (Main.getGameStatus() != GameStatus.MATCH) {
					ArrayList<Gun> guns = new ArrayList<>();
					for (Gun gun : Gun.getGunList().values()) {
						if (gun.getPlayer() != null) {
							if (gun.getPlayer().isOnline()) {
								boolean isininv = false;
								for (ItemStack is : gun.getPlayer().getInventory().getContents()) {
									if (is != null) {
										if (isSimilar(is, gun.getItemStack())) {
											isininv = true;
											break;
										}
									}
								}
								if (!isininv) {
									guns.add(gun);
								}
							} else {
								guns.add(gun);
							}
						} else {
							guns.add(gun);
						}

					}
					for (Gun g : guns) {
						g.removeGunFromMemory();
					}
				}

			}
		}.runTaskTimer(Main.getPlugin(), 10, 30);

		new BukkitRunnable() {
			public void run() {
				for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
					for (ItemStack is : p.getInventory().getContents()) {
						if (is != null) {
							if (GunUtils.isGun(is)) {
								try {
									if (!GunUtils.isRegistrated(is)) {
										GunUtils.loadGunIntoMemory(is, p);
									} else {
										Gun g = GunUtils.getGunMemory(is);
										if (g != null) {
											if (!g.getPlayer().equals(p)) {
												g.removeGunFromMemory();
												GunUtils.loadGunIntoMemory(is, p);
											}
										}

									}
								} catch (Exception e) {
									Lg.lgError(e.getMessage());
								}
							}
						}
					}
				}

			}
		}.runTaskTimer(Main.getPlugin(), 10, 5);

	}

	private static boolean isSimilar(ItemStack first, ItemStack second) {
		boolean similar = false;

		if (first == null || second == null) {
			return similar;
		}
		boolean sameTypeId = (first.getType() == second.getType());
		boolean sameDurability = (first.getDurability() == second.getDurability());

		if (sameTypeId && sameDurability) {
			similar = true;
		}

		return similar;

	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				for (ItemStack is : e.getPlayer().getInventory().getContents()) {
					if (is == null) {
						return;
					}
					if (GunUtils.isGun(is)) {
						GunUtils.loadGunIntoMemory(is, e.getPlayer());
					}
				}
			}
		}, 10);

	}

	@EventHandler
	public void on(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			ItemStack is = e.getItem().getItemStack();
			if (GunUtils.isGun(is)) {
				if (GunUtils.isRegistrated(is)) {
					GunUtils.getGunMemory(is).removeGunFromMemory();
				}

				GunUtils.loadGunIntoMemory(is, p);
				e.getItem().setItemStack(is);
			}

		}
	}

	@EventHandler
	public void on(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (GamePlayer.isInRound(p)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		for (ItemStack is : e.getPlayer().getInventory().getContents()) {
			if (is == null) {
				return;
			}
			if (GunUtils.isGun(is)) {
				if (GunUtils.isRegistrated(is)) {
					Gun gun = GunUtils.getGunMemory(is);
					gun.updateSerialNumberData(is);
					gun.removeGunFromMemory();
				}
			}

		}
	}

	@EventHandler
	public void on(ItemDespawnEvent e) {
		ItemStack is = e.getEntity().getItemStack();
		Gun gun = GunUtils.getGunMemoryManaged(is);
		if (gun != null) {
			GunUtils.getGunMemory(is).removeGunFromMemory();
		}
	}

	@EventHandler
	public void on(EventGunGameDeath e) {
		if (e.getKiller() != null) {
			for (Gun gun : Gun.getGunList().values()) {
				if (e.getKiller().equals(gun.getPlayer())) {
					gun.updateSerialNumberData();
				}
			}
		}
	}

	@EventHandler
	public void on2(EventGameRoundStop e) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				for (Gun gun : Gun.getGunList().values()) {
					gun.updateSerialNumberData();
				}
			}
		});

	}

	@EventHandler
	public void on2(EventGameRoundStart e) {
		for (Gun gun : Gun.getGunList().values()) {
			gun.refillGun();
			gun.updateSerialNumberData();
		}
	}

	@EventHandler
	public void on(InventoryOpenEvent e) {
		if (e.getPlayer() instanceof Player) {
			Player p = (Player) e.getPlayer();
			for (ItemStack is : p.getInventory().getContents()) {
				if (is != null) {
					Gun gun = GunUtils.getGunMemoryManaged(is);
					if (gun != null) {
						gun.setItemStack(is);
						gun.updateSerialNumberData(is);
					}
				}
			}
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (e.getClickedInventory() != null && e.getClickedInventory().getType() != null) {
			if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
				ItemStack is = e.getCurrentItem();
				if (is != null) {
					Gun gun = GunUtils.getGunMemoryManaged(is);
					if (gun != null) {
						gun.setItemStack(is);
						e.setCurrentItem(gun.updateSerialNumberData(is));
					}
				}
			}
		}

	}

	@EventHandler
	public void on1(EventGunGameLevelSet e) {
		for (ItemStack is : e.getPlayer().getInventory().getContents()) {
			if (is == null) {
				return;
			}
			if (GunUtils.isGun(is)) {
				if (GunUtils.isGunGameItem(is)) {
					if (GunUtils.isRegistrated(is)) {
						GunUtils.getGunMemory(is).removeGunFromMemory();
					}
				}

			}

		}
	}

	@EventHandler
	public void on2(EventGunGameLevelSet e) {
		ArrayList<ItemStack> list = new ArrayList<>();
		for (ItemStack is : e.getPlayer().getInventory().getContents()) {
			if (is != null) {
				if (GunUtils.isGun(is)) {
					if (GunUtils.isGunGameItem(is)) {
						Gun gun = GunUtils.getGunMemory(is);
						if (gun != null) {
							GunUtils.getGunMemory(is).removeGunFromMemory();
							list.add(is);
						}
					}
				}
			}
		}

		for (ItemStack is : list) {
			e.getPlayer().getInventory().remove(is);
		}
	}

	@EventHandler
	public void on(EventGameRoundStop e) {
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			ArrayList<ItemStack> list = new ArrayList<>();
			for (ItemStack is : p.getInventory().getContents()) {
				if (is != null) {
					if (GunUtils.isGun(is)) {
						Gun gun = GunUtils.getGunMemory(is);
						if (gun != null) {
							if (gun.isGunGameItem()) {
								GunUtils.getGunMemory(is).removeGunFromMemory();
								list.add(is);
							} else {
								if (gun.isGunGameItem()) {
									gun.refillGun();
								}
								gun.updateSerialNumberData(is);
							}
						}

					}
				}
			}

			for (ItemStack is : list) {
				p.getInventory().removeItem(is);
			}
		}

	}

}
