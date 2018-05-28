package at.ltd.lobby.shop.data;

import org.bukkit.inventory.ItemStack;

import at.ltd.gungame.guns.utils.GunInterface;

public class LobbyShopItem {

	// DATA
	private ItemStack ITEM_STACK;
	private int SLOT;
	private ShopItemType TYPE;
	private int COST;
	private GunInterface GUN_INTERFACE;
	private int GUN_KILLS;
	private int SHOPITEM_ID;
	private LobbyShopUnit LOBBY_SHOP_UNIT;
	private static int SHOPITEMIDPOS = 19;

	public static enum ShopItemType {
		NOTHING, ITEM, GUN, CLOSE, MONEY
	};

	public LobbyShopItem(ShopItemType type, int slot, LobbyShopUnit lsu) {
		this.TYPE = type;
		this.SLOT = slot;
		this.LOBBY_SHOP_UNIT = lsu;
		SHOPITEMIDPOS = SHOPITEMIDPOS + 1;
		LobbyShopData.ITEMS_BY_ID.put(SHOPITEMIDPOS, this);
		SHOPITEM_ID = SHOPITEMIDPOS;
	}

	public LobbyShopItem setAsGun(GunInterface gi, int kills, int cost) {
		ITEM_STACK = null;
		GUN_INTERFACE = gi;
		COST = cost;
		GUN_KILLS = kills;
		return this;
	}

	public LobbyShopItem setAsItem(ItemStack is, int cost) {
		GUN_INTERFACE = null;
		GUN_KILLS = 0;
		COST = cost;
		ITEM_STACK = is;
		return this;
	}

	public LobbyShopItem setItemStack(ItemStack is) {
		ITEM_STACK = is;
		return this;
	}

	// GET METHODS

	public ItemStack getItemStack() {
		return ITEM_STACK;
	}

	public int getSlot() {
		return SLOT;
	}

	public ShopItemType getType() {
		return TYPE;
	}

	public int getCost() {
		return COST;
	}

	public int getGunKills() {
		return GUN_KILLS;
	}

	public GunInterface getGunInterface() {
		return GUN_INTERFACE;
	}

	public int getID() {
		return SHOPITEM_ID;
	}

	public LobbyShopUnit getLobbyShopUnit() {
		return LOBBY_SHOP_UNIT;
	}

}
