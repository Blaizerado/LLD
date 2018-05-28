package at.ltd.gungame.guns.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.data.Cooldown;
import at.ltd.gungame.guns.events.custom.EventGunShoot;

public class GunUserHandlerEvent implements Listener {

	public static int needlevel = 3;
	private static Cooldown<Player> cooldown = new Cooldown<>();

	@EventHandler
	public void on(EventGunShoot e) {
		if (!e.getGun().isGunGameItem()) {
			Player p = e.getPlayer();
			GamePlayer ggp = GamePlayer.getGamePlayer(p);
			if (ggp.isInRound()) {
				if (ggp.getGunGameLevel() < needlevel) {
					e.setCancelled(true);
					if (cooldown.checkSec(p, 2)) {
						Cf.rsS(Cf.CUSTOM_GUN_BLOCK, p, "[LEVEL]", "" + needlevel);
					}

				}
			}
		}
	}

}
