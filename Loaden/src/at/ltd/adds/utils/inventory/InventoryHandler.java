package at.ltd.adds.utils.inventory;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.utils.data.Data2;
import at.ltd.adds.utils.threading.removeable.RemoveAble;

public class InventoryHandler implements Listener {

	private static HashMap<Data2<Inventory, ItemStack>, ItemClickHandler> HANDLER = new HashMap<>();
	private static ArrayList<Inventory> STEAL_PROTECTION = new ArrayList<>();
	
	public static void init() {
		Main.registerListener(new InventoryHandler());
	}
	
	
	public static void addClickHandler(Inventory inv, ItemStack is, ItemClickHandler handler) {
		synchronized (HANDLER) {
			Data2<Inventory, ItemStack> data = new Data2<Inventory, ItemStack>(inv, is);
			HANDLER.put(data, handler);
		}
	}
	
	public static void addProtection(Inventory inv) {
		synchronized (HANDLER) {
			STEAL_PROTECTION.add(inv);
		}
	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		Inventory clickinv = e.getClickedInventory();
		ItemStack clickeditem = e.getCurrentItem();
		synchronized (HANDLER) {
			if (STEAL_PROTECTION.contains(clickinv)) {
				e.setCancelled(true);
			}
			if (clickeditem != null) {
				for (Data2<Inventory, ItemStack> inv : HANDLER.keySet()) {
					if(inv.getSecond().equals(clickeditem)) {
						try {
							HANDLER.get(inv).on((Player)e.getWhoClicked(), clickeditem, clickinv);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}
			}
		}
	}

	private ArrayList<Data2<Inventory, ItemStack>> remove = new ArrayList<>();

	@EventHandler
	public void on(InventoryCloseEvent e) {
		Inventory closeinv = e.getInventory();
		RemoveAble.addSec(() -> {
			synchronized (HANDLER) {
				for (Data2<Inventory, ItemStack> inv : HANDLER.keySet()) {
					if (inv.getFirst().equals(closeinv)) {
						remove.add(inv);
					}
				}
				for (Data2<Inventory, ItemStack> inv : remove) {
					HANDLER.remove(inv);
				}
				if (STEAL_PROTECTION.contains(closeinv)) {
					STEAL_PROTECTION.remove(closeinv);
				}
				remove.clear();
			}
		}, 1);

		
	}

}
