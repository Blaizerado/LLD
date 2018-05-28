package at.ltd.gungame.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.utils.visual.DamageIndicator;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;

public class BrawlEvent implements Listener {

	@EventHandler
	public void on(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			GamePlayer ggp = GamePlayer.getGamePlayer(damager);
			if (!ggp.isInRound()) {
				return;
			}
			if (damager.getInventory().getItemInMainHand() == null
					| damager.getInventory().getItemInMainHand().getType() == Material.AIR) {
				GamePlayerDamage.damagePlayer((Player) e.getEntity(), damager, 5.00D, DamageType.PLAYER_DAMAGE);
				e.setCancelled(true);
				DamageIndicator.show((Player) e.getEntity(), damager, 5);

			}
		}
	}

}
