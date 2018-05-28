package at.ltd.adds.utils.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ItemClickHandler {

	
	public void on(Player p, ItemStack is, Inventory inv);
	
}
