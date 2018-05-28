package at.ltd.lobby.shop.data;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.ItemUtils;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.InventoryAnimation;
import at.ltd.adds.utils.visual.InventoryAnimation.AnimationType;
import at.ltd.lobby.shop.data.LobbyShopItem.ShopItemType;
import at.ltd.lobby.shop.reflections.LobbyShopFakeMob;
import at.ltd.lobby.shop.reflections.LobbyShopMobs;

public class LobbyShopUnit {

	private Location SHOP_LOC;
	private int INV_SIZE;
	private String SHOP_NAME;
	private String SHOPMOB_NAME;
	private LobbyShopMobs ENTITY_TYPE;
	private int SHOP_ID;
	private static int SHOPIDPOS = 0;

	public LobbyShopUnit(Location shoploc, int invsize, String shopname, String shopmobname, LobbyShopMobs entity) {
		SHOP_ID = SHOPIDPOS++;
		this.SHOP_LOC = shoploc;
		this.INV_SIZE = invsize;
		this.SHOP_NAME = ChatColor.translateAlternateColorCodes('&', shopname);
		this.SHOPMOB_NAME = ChatColor.translateAlternateColorCodes('&', shopmobname);
		this.ENTITY_TYPE = entity;
		LobbyShopData.SHOPS.put(SHOP_ID, this);
		LobbyShopData.INV_NAMES.add(SHOP_NAME);
	}

	public void open(Player p) {
		Inventory inv = Bukkit.createInventory(p, INV_SIZE, SHOP_NAME);
		CopyOnWriteArrayList<LobbyShopItem> ITEMS = LobbyShopData.getItems(this);
		InventoryAnimation animation = new InventoryAnimation();
		for (LobbyShopItem item : ITEMS) {
			animation.put(item.getSlot(), convert(item, p));
		}
		AsyncThreadWorkers.submitSyncWork(() -> {
			p.openInventory(inv);
			animation.animate(inv, AnimationType.DOWN_TO_UP, true, 1, null);
		});
	}

	public ItemStack convert(LobbyShopItem lsi, Player p) {
		ItemStack is = null;
		if (lsi.getType() == ShopItemType.GUN) {
			is = createItemStack(lsi.getGunInterface().getItem(), lsi.getGunInterface().getName(), "§7Kosten:§f " + lsi.getCost(), "§7Kills:§f " + lsi.getGunKills(), "§8" + lsi.getID());
		}
		if (lsi.getType() == ShopItemType.ITEM) {
			is = createItemStack(lsi.getItemStack(), null, "§7Kosten:§f " + lsi.getCost(), "§8" + lsi.getID());
		}
		if (lsi.getType() == ShopItemType.NOTHING) {
			if (lsi.getItemStack() != null) {
				ItemStack itemStack = lsi.getItemStack();
				if (ItemUtils.hasItemName(itemStack)) {
					is = createItemStack(lsi.getItemStack(), ItemUtils.getItemName(itemStack), Cf.rs(Cf.SHOP_ITEM_NOTHING_LORE, p));
				} else {
					is = createItemStack(lsi.getItemStack(), Cf.rs(Cf.SHOP_ITEM_NOTHING_NAME, p), Cf.rs(Cf.SHOP_ITEM_NOTHING_LORE, p));
				}
			} else {
				is = createItemStack(ItemSerializer.deserialize("[V=002:STAINED_GLASS_PANE/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]"), Cf.rs(Cf.SHOP_ITEM_NOTHING_NAME, p), Cf.rs(Cf.SHOP_ITEM_NOTHING_LORE, p));
			}
		}
		if (lsi.getType() == ShopItemType.CLOSE) {
			if (lsi.getItemStack() != null) {
				is = createItemStack(lsi.getItemStack(), Cf.rs(Cf.SHOP_ITEM_CLOSE_NAME, p), Cf.rs(Cf.SHOP_ITEM_CLOSE_LORE, p));
			} else {
				is = createItemStack(ItemSerializer.deserialize("[V=002:CONCRETE/³1/³14/³/³/³/³/³/³/³/³/³/³/³/³/³/³]"), Cf.rs(Cf.SHOP_ITEM_CLOSE_NAME, p), Cf.rs(Cf.SHOP_ITEM_CLOSE_LORE, p));
			}
		}

		if (lsi.getType() == ShopItemType.MONEY) {
			if (lsi.getItemStack() != null) {
				is = createItemStack(lsi.getItemStack(), Cf.rs(Cf.SHOP_ITEM_MONEY_NAME, p), Cf.rs(Cf.SHOP_ITEM_MONEY_LORE, p));
			} else {
				is = createItemStack(ItemSerializer.deserialize("[V=002:GOLD_NUGGET/³1/³0/³/³/³/³/³/³/³/³/³/³/³/³/³/³]"), Cf.rs(Cf.SHOP_ITEM_MONEY_NAME, p), Cf.rs(Cf.SHOP_ITEM_MONEY_LORE, p));
			}
		}
		is = ItemUtils.removeAttributes(is);
		return is;
	}

	public ItemStack createItemStack(ItemStack mat, String name, String... lore) {
		ItemStack is = new ItemStack(mat);
		ItemMeta im = is.getItemMeta();
		if (name == null) {
			if (mat.getItemMeta().hasDisplayName()) {
				im.setDisplayName(mat.getItemMeta().getDisplayName());
			}
		} else {
			im.setDisplayName(name);
		}
		if (lore.length != 0) {
			ArrayList<String> ar = new ArrayList<>();
			for (String s : lore) {
				ar.add(s);
			}
			im.setLore(ar);
		}
		is.setItemMeta(im);
		return is;
	}

	public void addShopItem(LobbyShopItem lsi) {
		if (LobbyShopData.SHOP_ITEMS.containsKey(this)) {
			LobbyShopData.SHOP_ITEMS.get(this).add(lsi);
		} else {
			CopyOnWriteArrayList<LobbyShopItem> ar = new CopyOnWriteArrayList<>();
			ar.add(lsi);
			LobbyShopData.SHOP_ITEMS.put(this, ar);
		}
	}

	public void sendMobToPlayer(Player p) {
		new LobbyShopMobData(p, LobbyShopFakeMob.spawn(ENTITY_TYPE, SHOP_LOC, SHOPMOB_NAME, p), this);
	}

	// GET_METHODS
	public Location getShopLocation() {
		return SHOP_LOC;
	}

	public int getInventorySize() {
		return INV_SIZE;
	}

	public String getShopName() {
		return SHOP_NAME;
	}

	public String getShopMobName() {
		return SHOPMOB_NAME;
	}

	public LobbyShopMobs getEntityType() {
		return ENTITY_TYPE;
	}

	public int getShopID() {
		return SHOP_ID;
	}

}
