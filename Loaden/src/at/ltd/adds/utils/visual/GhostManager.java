package at.ltd.adds.utils.visual;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import at.ltd.Main;

public class GhostManager implements Listener {

	public GhostManager() {
		Main.registerListener(this);
	}

	private ArrayList<Player> ghost = new ArrayList<>();

	@EventHandler
	public void on(PlayerQuitEvent e) {
		if (isGhost(e.getPlayer())) {
			remove(e.getPlayer());
		}
	}

	@EventHandler
	public void on(PlayerJoinEvent e) {
		if (e.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
			e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	public void add(Player player) {
		if (!isGhost(player)) {
			ghost.add(player);
			player.setCollidable(false);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999999, 15));
		}
	}

	public void remove(Player player) {
		if (isGhost(player)) {
			ghost.remove(player);
			player.setCollidable(true);
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	public boolean isGhost(Player player) {
		return ghost.contains(player);
	}

}
