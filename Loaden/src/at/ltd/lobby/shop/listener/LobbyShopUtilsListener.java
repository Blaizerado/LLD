package at.ltd.lobby.shop.listener;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.TransactionStatus;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.lobby.shop.EventShopItemClickAsync;
import at.ltd.lobby.shop.data.LobbyShopData;
import at.ltd.lobby.shop.data.LobbyShopItem;
import at.ltd.lobby.shop.data.LobbyShopItem.ShopItemType;

public class LobbyShopUtilsListener implements Listener {

	@EventHandler
	public void on(EventShopItemClickAsync e) {
		Player p = e.getPlayer();
		ItemStack is = e.getClickedItem();
		if (ChatColor.stripColor(Cf.SHOP_ITEM_CLOSE_NAME).equals(getItemName(is))) {
			AsyncThreadWorkers.submitSyncWork(() -> p.closeInventory());
		}
	}

	/**
	 * Buy Item
	 * 
	 * @param e
	 */
	@EventHandler
	public void onItem(EventShopItemClickAsync e) {
		Player p = e.getPlayer();
		ItemStack is = e.getClickedItem();
		String id = ChatColor.stripColor(getLastLore(is));
		if (isNumeric(id) && !id.equals("")) {
			int itemid = Integer.valueOf(id);
			LobbyShopItem item = LobbyShopData.ITEMS_BY_ID.get(itemid);
			if (item.getType() == ShopItemType.ITEM) {
				if (invFull(p)) {
					Cf.rsS(Cf.SHOP_BUY_4, p);
					return;
				}
				TransactionStatus status = Main.getTransaction().minus("BUY_ITEM: " + item.getItemStack().getType() + ", AMMOUNT: " + item.getItemStack().getAmount(), item.getCost(), p);
				if (status == TransactionStatus.NOTENOUGHCOINS) {
					Cf.rsS(Cf.SHOP_BUY_2, p);
				} else if (status == TransactionStatus.SUCCESS) {
					ItemStack buyitem = item.getItemStack().clone();
					p.getInventory().addItem(buyitem);
					Cf.rsS(Cf.SHOP_BUY_1_ITEM, p, "[SIZE]", buyitem.getAmount(), "[ITEMNAME]", getItemName(buyitem));
				} else if (status == TransactionStatus.EXEPTION) {
					Cf.rsS(Cf.SHOP_BUY_3, p);
				}
			}
		}
	}

	/**
	 * Buy Gun
	 * 
	 * @param e
	 */
	@EventHandler
	public void onGun(EventShopItemClickAsync e) {
		Player p = e.getPlayer();
		ItemStack is = e.getClickedItem();
		String id = ChatColor.stripColor(getLastLore(is));
		if (isNumeric(id) && !id.equals("")) {
			int itemid = Integer.valueOf(id);
			LobbyShopItem item = LobbyShopData.ITEMS_BY_ID.get(itemid);
			if (item.getType() == ShopItemType.GUN) {
				if (invFull(p)) {
					Cf.rsS(Cf.SHOP_BUY_4, p);
					return;
				}
				TransactionStatus status = Main.getTransaction().minus("BUY_GUN: " + ChatColor.stripColor(item.getGunInterface().getName()) + ", KILLS:" + item.getGunKills(), item.getCost(), p);
				if (status == TransactionStatus.NOTENOUGHCOINS) {
					Cf.rsS(Cf.SHOP_BUY_2, p);
					AsyncThreadWorkers.submitSyncWork(() -> p.closeInventory());
				} else if (status == TransactionStatus.SUCCESS) {
					ItemStack buyitem = GunUtils.createGun(item.getGunInterface(), p, item.getGunKills()).getItemStack();
					p.getInventory().addItem(buyitem);
					Cf.rsS(Cf.SHOP_BUY_5_GUN, p, "[GUNKILLS]", item.getGunKills(), "[GUNNAME]", ChatColor.stripColor(item.getGunInterface().getName()));
					AsyncThreadWorkers.submitSyncWork(() -> p.closeInventory());
				} else if (status == TransactionStatus.EXEPTION) {
					Cf.rsS(Cf.SHOP_BUY_3, p);
				}
			}
		}
	}

	@EventHandler
	public void onSU(EventShopItemClickAsync e) {
		Player p = e.getPlayer();
		if (SuperUser.isSuperUser(p)) {
			p.sendMessage(Main.getPrefix() + "§7SlotID:§f " + e.getInventoryClickEvent().getSlot());
		}
	}

	// UTILS
	public static String getItemName(ItemStack is) {
		if (!is.hasItemMeta()) {
			return is.getType().name();
		}
		if (is.getItemMeta().hasDisplayName()) {
			return ChatColor.stripColor(is.getItemMeta().getDisplayName());
		}
		return is.getType().name();
	}

	public static String getLastLore(ItemStack is) {
		if (!is.hasItemMeta()) {
			return "";
		}
		if (is.getItemMeta().hasLore()) {
			List<String> lore = is.getItemMeta().getLore();
			if (lore.size() == 0) {
				return "";
			}
			return ChatColor.stripColor(lore.get(lore.size() - 1));
		}
		return "";
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public static boolean invFull(Player p) {
		return !Arrays.asList(p.getInventory().getStorageContents()).contains(null);
	}

}
