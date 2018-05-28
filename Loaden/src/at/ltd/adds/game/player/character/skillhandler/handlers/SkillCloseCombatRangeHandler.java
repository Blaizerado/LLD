package at.ltd.adds.game.player.character.skillhandler.handlers;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.game.player.GamePlayerDamage;
import at.ltd.adds.game.player.character.SkillGroupManager;
import at.ltd.adds.game.player.character.skillgroups.SkillGroupNames;
import at.ltd.adds.game.player.character.skillhandler.SkillHandlerInterface;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.data.Cooldown;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.threading.removeable.RemoveAble;
import at.ltd.adds.utils.visual.Hologram;
import at.ltd.gungame.events.custom.EventGunGameDamage.DamageType;
import at.ltd.gungame.utils.melee.MeleWeapons;

public class SkillCloseCombatRangeHandler implements SkillHandlerInterface, Listener {

	public static Cooldown<Player> cooldown = new Cooldown<>();

	@Override
	public void onRegister() {
		Main.registerListener(this);
	}

	@EventHandler
	public void on(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_AIR) {
			e.setCancelled(true);
			Player p = e.getPlayer();
			float lvl = SkillGroupNames.CLOSE_COMBAT.getPlayerLvl(p);
			if (lvl == 0) {
				return;
			}
			float range = (float) (1.5 + (lvl * 0.85));
			Player target = getNearestEntityInSight(p, range);
			if (target != null && cooldown.checkMili(target, 250)) {
				Player damager = (Player) p;
				GamePlayer ggp = GamePlayer.getGamePlayer(damager);
				if (!ggp.isInRound()) {
					return;
				}
				if (damager.getInventory().getItemInHand() == null
						| damager.getInventory().getItemInHand().getType() == Material.AIR) {
					GamePlayerDamage.damagePlayer((Player) target, damager, 5.00D, DamageType.PLAYER_DAMAGE);
					e.setCancelled(true);
					Location enloc = target.getLocation();
					AsyncThreadWorkers.submitWork(new Runnable() {

						@Override
						public void run() {
							Location loc = getRandom(enloc, 0.5D);
							Hologram holo = new Hologram(loc, "§c-5");
							holo.send(damager);
							RemoveAble.addSec(() -> {
								if (damager.isOnline()) {
									holo.destroy(damager);
								}
							}, 3);
						}
					});

				} else {
					MeleWeapons.handleDamage(p, target);
				}
			}
		}
	}

	public Vector generateVector(Player p) {
		Location loc = p.getEyeLocation();
		double yaw = Math.toRadians(-loc.getYaw() - 90.0F);
		double pitch = Math.toRadians(-loc.getPitch());
		double x = Math.cos(pitch) * Math.cos(yaw);
		double y = Math.sin(pitch);
		double z = -Math.sin(yaw) * Math.cos(pitch);
		Vector dirVel = new Vector(x, y, z);
		return dirVel;
	}

	public Player getNearestEntityInSight(Player p, double range) {
		Location start = p.getEyeLocation();
		Vector dir = generateVector(p);
		for (double i = 1.501; i < range; i += 0.3) {
			dir.multiply(i);
			start.add(dir);
			if (start.getBlock().getType().isSolid()) {
				break;
			}
			List<Player> entitylist = LocUtils.getPlayersNearLocation(start, 1.7);
			if (!entitylist.isEmpty()) {
				for (Player tar : entitylist) {
					if (tar != p) {
						return tar;
					}
				}
			}
			start.subtract(dir);
			dir.normalize();
		}
		return null;

	}

	public Location getRandom(Location center, double radius) {
		World world = center.getWorld();
		int am = ThreadLocalRandom.current().nextInt(0, 100);
		double increment = (2 * Math.PI) / am;
		double y = center.getY();
		int i = ThreadLocalRandom.current().nextInt(0, 360);
		y = ThreadLocalRandom.current().nextDouble(y - 1.4D, y + 0.3D);
		double angle = i * increment;
		double x = center.getX() + (radius * Math.cos(angle));
		double z = center.getZ() + (radius * Math.sin(angle));
		return new Location(world, x, y, z);
	}

}
