package at.ltd.lobby.shop;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EventShopItemClickAsync extends Event {

	private final static HandlerList handlers = new HandlerList();
	private Player player;
	private ItemStack clickedItem;
	private InventoryClickEvent event;
	public EventShopItemClickAsync(InventoryClickEvent e) {
		this.player = (Player) e.getWhoClicked();
		clickedItem = e.getCurrentItem();
		event = e;
	}

	public Player getPlayer() {
		return player;
	}

	public ItemStack getClickedItem() {
		return clickedItem;
	}

	public InventoryClickEvent getInventoryClickEvent() {
		return event;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;

	}

}
