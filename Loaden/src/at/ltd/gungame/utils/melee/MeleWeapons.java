package at.ltd.gungame.utils.melee;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.fac.BeforeDamageHandler;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.utils.melee.trowableaxe.EventAxe;

public class MeleWeapons implements Listener {

	public static void init() {
		MeleWeaponRegister.register();
		Bukkit.getServer().getPluginManager().registerEvents(new MeleWeapons(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new EventAxe(), Main.getPlugin());
	}

	@EventHandler
	public void onMeleDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			handleDamage((Player) e.getDamager(), (Player) e.getEntity());
		}
	}

	public static void handleDamage(Player p, Player damagee) {
		ItemStack is = p.getInventory().getItemInMainHand();
		if (is != null) {
			String ui = MeleWeaponRegister.createWeaponString(is);
			if (MeleWeaponRegister.WEAPONS.containsKey(ui)) {
				double damage = MeleWeaponRegister.WEAPONS.get(ui);
				GamePlayerDamage.damagePlayer(damagee, p, damage, DamageType.PLAYER_DAMAGE, new BeforeDamageHandler() {

					@Override
					public void handle(GamePlayer damagee, boolean dead) {
						damagee.setLastDamageWeaponName(null);
						damagee.setLastGunHit(null);
						damagee.setLastDamager(GamePlayer.getGamePlayer(p));
					}
				});

			}
		}
	}

}
