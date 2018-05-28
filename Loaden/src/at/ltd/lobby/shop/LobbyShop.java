package at.ltd.lobby.shop;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventConfigReload;
import at.ltd.lobby.LobbyUtils;
import at.ltd.lobby.shop.data.LobbyShopData;
import at.ltd.lobby.shop.data.LobbyShopMobData;
import at.ltd.lobby.shop.listener.LobbyShopListener;
import at.ltd.lobby.shop.listener.LobbyShopUtilsListener;
import at.ltd.lobby.shop.reflections.LobbyShopFakeMob;
import at.ltd.lobby.shop.reflections.PacketReaderEvent;

public class LobbyShop implements Listener {

	public static void init() {
		LobbyShopData.readShops();
		Main.registerListener(new LobbyShop());
		Bukkit.getServer().getPluginManager().registerEvents(new LobbyShopListener(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new PacketReaderEvent(), Main.getPlugin());
		Bukkit.getServer().getPluginManager().registerEvents(new LobbyShopUtilsListener(), Main.getPlugin());

		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			AsyncThreadWorkers.getOnlinePlayers().forEach(p -> {
				Location loc = AsyncThreadWorkers.getEntityLocation(p);
				if (!LobbyUtils.isLocationInLobby(loc)) {
					LobbyShopListener.removeData(p);
				}
				if (LobbyUtils.isLocationInLobby(loc)) {
					LobbyShopListener.addData(p);
				}
			});
		}, 15, 15);
		AsyncThreadWorkers.submitSchedulerWorkMin(() -> {
			for (Player p : LobbyUtils.getAllPlayersInLobby()) {
				LobbyShopListener.removeData(p);
				LobbyShopListener.addData(p);
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
		}, 1, 1);

	}

	@EventHandler
	public void on(EventConfigReload e) {
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			p.closeInventory();
			LobbyShopListener.removeData(p);
		}
		LobbyShopData.readShops();
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			LobbyShopListener.addData(p);
		}
	}

}
