package at.ltd.events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import at.ltd.Main;
import at.ltd.adds.utils.threading.SingelThreadWorkingLoop;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerInteractAsync;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.events.custom.EventPlayerMoveAsync;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;

public class EventAsyncWrapper implements Listener {

	public static SingelThreadWorkingLoop MOVE_LOOP = new SingelThreadWorkingLoop(5);
	public static SingelThreadWorkingLoop INTERACT_LOOP = new SingelThreadWorkingLoop(5);
	
	@EventHandler
	public void on(PlayerInteractEvent e) {
		AsyncThreadWorkers.submitWork(() -> Bukkit.getPluginManager().callEvent(new EventPlayerInteractAsync(e)));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerJoinEvent e) {
		AsyncThreadWorkers.submitWork(() -> Bukkit.getPluginManager().callEvent(new EventPlayerJoinAsync(e.getPlayer())));
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void on(PlayerMoveEvent e) {
		final Location from = e.getFrom();
		final Location to = e.getTo();
		final Block block = to.getBlock().getRelative(BlockFace.DOWN);
		final Player player = e.getPlayer();
		AsyncThreadWorkers.submitWork(() -> {
			EventPlayerMoveAsync eventmove = new EventPlayerMoveAsync(from, to, block, player);
			Main.callEvent(eventmove);
			if (to.getBlockX() == from.getBlockX() && to.getBlockY() == from.getBlockY() && to.getBlockZ() == from.getBlockZ()) {
				return;
			}
			EventPlayerMoveBlockEventAsync event = new EventPlayerMoveBlockEventAsync(from, to, block, player);
			Main.callEvent(event);
		});

	}
}
