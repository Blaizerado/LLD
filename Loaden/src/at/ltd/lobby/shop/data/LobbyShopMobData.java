package at.ltd.lobby.shop.data;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.Entity;

public class LobbyShopMobData {

	private Player PLAYER;
	private int ENTITY_ID;
	private LobbyShopUnit LOBBY_SHOP_UINT;
	private Entity ENTITY;

	public LobbyShopMobData(Player player, Entity entity, LobbyShopUnit lsu) {
		this.PLAYER = player;
		this.ENTITY_ID = entity.getId();
		this.LOBBY_SHOP_UINT = lsu;
		this.ENTITY = entity;
		LobbyShopData.SHOPS_MOBS_BY_ENTITY_ID.put(ENTITY_ID, this);
		if (LobbyShopData.SHOPS_MOBS_BY_PLAYER.containsKey(player)) {
			CopyOnWriteArrayList<LobbyShopMobData> ar = LobbyShopData.SHOPS_MOBS_BY_PLAYER.get(player);
			ar.add(this);
		} else {
			CopyOnWriteArrayList<LobbyShopMobData> ar = new CopyOnWriteArrayList<>();
			ar.add(this);
			LobbyShopData.SHOPS_MOBS_BY_PLAYER.put(player, ar);
		}

	}

	// GET_METHODS
	public Player getPlayer() {
		return PLAYER;
	}

	public int getEnityID() {
		return ENTITY_ID;
	}

	public Entity getEntity() {
		return ENTITY;
	}

	public LobbyShopUnit getLobbyShopUnit() {
		return LOBBY_SHOP_UINT;
	}

}
