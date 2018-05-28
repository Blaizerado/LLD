package at.ltd.gungame.utils.melee.trowableaxe;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerInteractAsync;
import at.ltd.gungame.utils.melee.MeleWeaponRegister;

public class EventAxe implements Listener {

	public static final String wp1 = MeleWeaponRegister.createWeaponString(ItemSerializer.deserialize("[V=002:SLIME_BALL/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]"));
	
	@EventHandler
	public void on(EventPlayerInteractAsync e) {
		Player p = e.getPlayer();
		if (GamePlayer.isInRound(p)) {
			if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				String ui = MeleWeaponRegister.createWeaponString(p.getInventory().getItemInMainHand());
				if (wp1.equals(ui)) {
					AsyncThreadWorkers.submitSyncWork(new Runnable() {

						@Override
						public void run() {
							ItemStack is = p.getInventory().getItemInMainHand().clone();
							is.setAmount(1);
							Item item = p.getWorld().dropItem(p.getEyeLocation(), is);
							item.setVelocity(p.getLocation().getDirection().multiply(2D));
							int size = e.getItem().getAmount();
							size--;
							if (size == 0) {
								e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
							} else {
								e.getPlayer().getInventory().getItemInMainHand().setAmount(size);
							}
							new AxeThread(p, 0.5, item).start();
						}
					});

				}
			}
		}

	}

}
