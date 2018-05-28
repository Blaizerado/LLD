package at.ltd.gungame.guns.events;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.events.custom.EventPlayerInteractAsync;
import at.ltd.gungame.guns.events.custom.EventMousePressTick;
import at.ltd.gungame.guns.utils.GunUtils;

public class MouseKlickEvent implements Listener {

	public static ConcurrentHashMap<Player, Long> time = new ConcurrentHashMap<>();

	@EventHandler
	public void on(EventPlayerInteractAsync e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			return;
		}
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			return;
		}
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Material mat = e.getClickedBlock().getType();
			if (mat == Material.CHEST) {
				return;
			} else if (mat == Material.ENDER_CHEST) {
				return;
			} else if (mat == Material.ENCHANTMENT_TABLE) {
				return;
			} else if (mat == Material.BREWING_STAND) {
				return;
			} else if (mat == Material.ANVIL) {
				return;
			} else if (mat == Material.WORKBENCH) {
				return;
			}

		}
		if (e.getAction() == Action.PHYSICAL) {
			return;
		}

		final Player p = e.getPlayer();
		final ItemStack is = p.getInventory().getItemInMainHand();
		if (is != null) {
			if (GunUtils.isGun(is)) {
				set(p);
			}
		}

	}

	public static void set(Player p) {
		Long l = System.currentTimeMillis();
		if (time.containsKey(p)) {
			time.replace(p, l);
		} else {
			time.put(p, l);
		}
	}

	public static void startTrigger() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				ArrayList<Player> ar = new ArrayList<>();
				for (Player p : time.keySet()) {
					Long l = time.get(p);
					if ((System.currentTimeMillis() - l) > 190) {
						ar.add(p);
					} else {
						EventMousePressTick eventMousePressTick = new EventMousePressTick(p);
						Bukkit.getPluginManager().callEvent(eventMousePressTick);
					}
				}

				for (Player p : ar) {
					time.remove(p);
				}

			}
		}, 0, 0);
	}

	@EventHandler
	public void on(PlayerQuitEvent e) {
		if (time.containsKey(e.getPlayer())) {
			time.remove(e.getPlayer());
		}
	}

}
