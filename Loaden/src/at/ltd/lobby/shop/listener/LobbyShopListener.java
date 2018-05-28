package at.ltd.lobby.shop.listener;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.events.custom.EventPlayerMoveAsync;
import at.ltd.events.custom.EventPlayerQuitAsync;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.events.custom.EventJoinGame;
import at.ltd.lobby.LobbyUtils;
import at.ltd.lobby.shop.EventShopItemClickAsync;
import at.ltd.lobby.shop.data.LobbyShopData;
import at.ltd.lobby.shop.data.LobbyShopMobData;
import at.ltd.lobby.shop.reflections.EventPlayerKlickOnEntity;
import at.ltd.lobby.shop.reflections.LobbyShopFakeMob;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;

public class LobbyShopListener implements Listener {

	@EventHandler
	public void on(EventPlayerKlickOnEntity e) {
		Player player = e.getPlayer();
		int id = e.getEntityID();
		if (LobbyShopData.SHOPS_MOBS_BY_ENTITY_ID.containsKey(id)) {
			LobbyShopData.SHOPS_MOBS_BY_ENTITY_ID.get(id).getLobbyShopUnit().open(player);
		}
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		AsyncThreadWorkers.submitDelayedWorkSec(() -> addData(e.getPlayer()), 1);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EventJoinGame e) {
		if (!e.isCancelled()) {
			AsyncThreadWorkers.submitWork(() -> removeData(e.getPlayer()));
		}
	}

	@EventHandler
	public void on(EventPlayerQuitAsync e) {
		removeData(e.getPlayer());
	}

	@EventHandler
	public void on(EventPlayerMoveAsync e) {
		Player p = e.getPlayer();
		if (!LobbyShopData.SHOPS_MOBS_BY_PLAYER.containsKey(p)) {
			return;
		}
		CopyOnWriteArrayList<LobbyShopMobData> lsmdl = LobbyShopData.SHOPS_MOBS_BY_PLAYER.get(p);
		Location playerloc = p.getLocation();
		for (LobbyShopMobData lsmd : lsmdl) {
			Location shopLoc = lsmd.getLobbyShopUnit().getShopLocation();
			if (LocUtils.sameWorld(playerloc, shopLoc)) {
				if (LocUtils.distanceLocation(shopLoc, playerloc) < 25) {
					LobbyShopFakeMob.headMob(p, lsmd);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void on(EventTeleport e) {
		if (e.isCancelled()) {
			return;
		}
		Player p = e.getPlayer();
		Location loc = p.getLocation();
		if (LobbyUtils.isLocationInLobby(loc)) {
			if (!LobbyUtils.isLocationInLobby(e.getLocation())) {
				AsyncThreadWorkers.submitWork(() -> removeData(p));
			}
		}

		if (!LobbyUtils.isLocationInLobby(loc)) {
			if (LobbyUtils.isLocationInLobby(e.getLocation())) {
				AsyncThreadWorkers.submitDelayedWorkSec(() -> addData(p), 1);
			}
		}

	}

	@EventHandler
	public void on(InventoryClickEvent e) {
		if (LobbyShopData.INV_NAMES.contains(e.getInventory().getName())) {
			e.setCancelled(true);
			if (e.getCurrentItem() != null) {
				AsyncThreadWorkers.submitWork(() -> {
					EventShopItemClickAsync event = new EventShopItemClickAsync(e);
					Bukkit.getPluginManager().callEvent(event);
				});

			}
		}
	}

	public static void addData(Player p) {
		if (LobbyShopData.SHOPS_MOBS_BY_PLAYER.containsKey(p)) {
			return;
		}
		LobbyShopData.SHOPS.values().forEach(lsu -> lsu.sendMobToPlayer(p));
	}

	public static void removeData(Player p) {
		if (!LobbyShopData.SHOPS_MOBS_BY_PLAYER.containsKey(p)) {
			return;
		}
		LobbyShopData.SHOPS_MOBS_BY_PLAYER.get(p).forEach(data -> {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(data.getEnityID());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
			LobbyShopData.SHOPS_MOBS_BY_ENTITY_ID.remove(data.getEnityID());
		});
		LobbyShopData.SHOPS_MOBS_BY_PLAYER.remove(p);
	}

}
