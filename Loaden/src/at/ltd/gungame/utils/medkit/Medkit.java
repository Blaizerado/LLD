package at.ltd.gungame.utils.medkit;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;

public class Medkit implements Listener {

	public static Material MEDKIT = Material.APPLE;

	public static void init() {
		Main.registerListener(new Medkit());
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if (e.getItem() != null) {
			if (e.getItem().getType() == MEDKIT) {
				if (!GamePlayer.isInRound(e.getPlayer())) {
					return;
				}
				if (e.getPlayer().getFireTicks() != -20) {
					return;
				}
				int size = e.getItem().getAmount();
				size--;
				if (size == 0) {
					e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
				} else {
					e.getPlayer().getInventory().getItemInMainHand().setAmount(size);
				}
				giveHealing(e.getPlayer());
			}
		}
	}

	public void giveHealing(Player p) {
		PotionEffect healing = new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 2);
		p.addPotionEffect(healing, true);
	}

}
