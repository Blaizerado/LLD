package at.ltd.events.custom;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EventPlayerInteractAsync extends Event {

	private final static HandlerList handlers = new HandlerList();

	private Player player;
	private Action action;
	private Block clickedBlock;
	private ItemStack item;
	public EventPlayerInteractAsync(PlayerInteractEvent e) {
		this.player = e.getPlayer();
		this.action = e.getAction();
		clickedBlock = e.getClickedBlock();
		item = e.getItem();
	}

	public Player getPlayer() {
		return player;
	}

	public Action getAction() {
		return action;
	}

	public Block getClickedBlock() {
		return clickedBlock;
	}

	public ItemStack getItem() {
		return item;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
