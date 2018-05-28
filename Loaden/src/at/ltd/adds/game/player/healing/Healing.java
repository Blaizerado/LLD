package at.ltd.adds.game.player.healing;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.gungame.events.custom.EventGunGameDamage;

public class Healing implements Listener {

	public static final int MIN_LAST_HIT_TIME = 3500;
	public static final int MIN_LAST_HEAL_TIME = 500;

	public static final String ID_HIT = "LAST_PLAYER_HIT";
	public static final String ID_HEAL = "LAST_PLAYER_HEALING";

	public static void init() {
		Main.registerListener(new Healing());

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
				GamePlayer gp = Main.getGunGamePlayer(p);
				double health = gp.getHealth();
				if (health < 20) {
					int TIME_DIF_HIT = (int) (System.currentTimeMillis() - (long) gp.getData(ID_HIT));
					int TIME_DIF_HEAL = (int) (System.currentTimeMillis() - (long) gp.getData(ID_HEAL));
					boolean canceled = false;
					
					if(TIME_DIF_HIT < MIN_LAST_HIT_TIME) {
						canceled = true;
					}
					
					if(TIME_DIF_HEAL < MIN_LAST_HEAL_TIME) {
						canceled = true;
					}
					
					EventPlayerHealing event;
					double dif = 20 - health;
					if (dif < 0.5) {
						event = new EventPlayerHealing(p, dif, TIME_DIF_HIT, TIME_DIF_HEAL, canceled);
					} else {
						event = new EventPlayerHealing(p, 0.5D, TIME_DIF_HIT, TIME_DIF_HEAL, canceled);
					}
					Main.callEvent(event);
					if (!event.isCanceled()) {
						health = gp.getHealth() + event.getHealing();
						if (health > 20) {
							health = 20;
						}
						gp.setHealth(health);
						gp.removeData(ID_HEAL);
						gp.putData(ID_HEAL, System.currentTimeMillis());

					}
				}
			}
		}, 0, 5);

	}

	@EventHandler
	public void on(EventGunGameDamage e) {
		if (e.getDamagee() != null) {
			GamePlayer gp = Main.getGunGamePlayer(e.getDamagee());
			gp.removeData(ID_HIT);
			gp.putData(ID_HIT, System.currentTimeMillis());
		}
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(EventPlayerJoinAsync e) {
		Main.getGunGamePlayer(e.getPlayer()).putData(ID_HEAL, System.currentTimeMillis());
		Main.getGunGamePlayer(e.getPlayer()).putData(ID_HIT, System.currentTimeMillis());
	}

}
