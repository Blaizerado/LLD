package at.ltd.lobby.infoitem;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.inventory.InventoryHandler;
import at.ltd.adds.utils.inventory.ItemClickHandler;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;
import at.ltd.lobby.infoitem.invs.MenuItemSelectSkillGroup;
import at.ltd.lobby.infoitem.invs.MenuItemSettings;
import at.ltd.lobby.infoitem.invs.MenuItemSkill;

public class LobbyMenu {

	@ConfigAble(key = "invname", value = "&cMenü")
	private static String name;

	private static ArrayList<LobbyMenuItem> ITEM = new ArrayList<>();

	public static void open(Player p) {
		Inventory inv = Bukkit.createInventory(p, 9, Cf.replaceColorCodes(name));
		InventoryAnimation animation = new InventoryAnimation();
		InventoryHandler.addProtection(inv);
		int i = 0;
		for (LobbyMenuItem item : ITEM) {
			ItemStack is = ItemUtils.generateItemStack(item.getMaterial(), item.getName(), convertLore(item.getLore()));
			InventoryHandler.addClickHandler(inv, is, new ItemClickHandler() {

				@Override
				public void on(Player p, ItemStack is, Inventory inv) {
					p.closeInventory();
					item.onClick(p);
				}
			});
			animation.put(i, is);
			i++;
		}
		
		ItemStack close = ItemUtils.generateItemStack(Material.REDSTONE_BLOCK, "§cClose");
		InventoryHandler.addClickHandler(inv, close, new ItemClickHandler() {

			@Override
			public void on(Player p, ItemStack is, Inventory inv) {
				p.closeInventory();
			}
		});
		animation.put(inv.getSize() - 1, close);
		
		p.openInventory(inv);
		animation.animate(inv, AnimationType.LEFT_TO_RIGHT, false, 1, null);

	}

	public static void add(LobbyMenuItem item) {
		ITEM.add(item);
		item.onRegister();
	}
	
	public static void init() {
		Config.loadMyClass("LobbyMenuSelectionInventory", LobbyMenu.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("LobbyMenuSelectionInventory", LobbyMenu.class);
		});
		
		add(new MenuItemSkill());
		add(new MenuItemSelectSkillGroup());
		add(new MenuItemSettings());
	}

	public static List<String> convertLore(String lore) {
		ArrayList<String> ar = new ArrayList<>();
		for (String str : lore.split("<newline>")) {
			ar.add(Cf.rs(str));
		}
		return ar;
	}

}
