package at.ltd.gungame.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.level.GLevelUtils;

public class ItemDropEvent implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerDropItemEvent e) {
		if(e.isCancelled()) {
			return;
		}
		
		ItemStack is = e.getItemDrop().getItemStack();
		if (is.getItemMeta().getLore() != null) {
			if (is.getItemMeta().getLore().get(0).equals(GLevelUtils.lorText)) {
				e.setCancelled(true);
			}
			if (GunUtils.isGun(is)) {
				if (GunUtils.isGunGameItem(is)) {
					e.setCancelled(true);
				} else {
					Gun gun = GunUtils.getGunMemory(is);
					if (gun != null) {
						if (GamePlayer.isInRound(e.getPlayer())) {
							e.setCancelled(true);
						} else {
							gun.removeGunFromMemory();
							e.getItemDrop().setItemStack(gun.updateSerialNumberData(e.getItemDrop().getItemStack()));
						}
					} else {
						e.setCancelled(true);
					}

				}
			}

		}
	}


}
