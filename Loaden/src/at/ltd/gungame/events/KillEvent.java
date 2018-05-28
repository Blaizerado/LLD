package at.ltd.gungame.events;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import at.ltd.Main;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventGunGameDamage;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventPlayerProtectionStart;
import at.ltd.gungame.level.GLevelUtils;
import at.ltd.lobby.LobbyUtils;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;

public class KillEvent implements Listener {

	@EventHandler
	public void on(PlayerRespawnEvent e) {
		LobbyUtils.tpPlayerToLobby(e.getPlayer());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void on(EventGunGameDamage e) {
		if (e.isCancelled()) {
			return;
		}
		Player p = e.getDamagee();
		AsyncThreadWorkers.submitSyncWork(() -> {
			Entity entity = ((CraftPlayer) p).getHandle();
			PacketPlayOutEntityStatus status = new PacketPlayOutEntityStatus(entity, (byte) 2);
			for (Player sendto : Bukkit.getOnlinePlayers()) {
				((CraftPlayer) sendto).getHandle().playerConnection.sendPacket(status);
			}
		});

	}

	@EventHandler
	public void onCount(EventGunGameDeath e) {
		Player death = e.getKilled();
		Player killer = e.getKiller();
		if (death != null) {
			SQLPlayer player = SQLCollection.getPlayer(death);
			player.setDeaths(player.getDeaths() + 1);

			GLevelUtils.removeItems(death);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
				GLevelUtils.setInventory(death, GamePlayer.getGamePlayer(death).getGunGameLevel());
			}, 1);

		}
		if (killer != null) {
			SQLPlayer player = SQLCollection.getPlayer(killer);
			player.setKills(player.getKills() + 1);
		}
	}

	@EventHandler
	public void on(EventPlayerProtectionStart e) {

	}

	@EventHandler
	public void on(EventGunGameDeath e) {
		if (e.getKiller() != null) {
			e.getKiller().playSound(e.getKiller().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
		}
		e.getKilled().playSound(e.getKilled().getLocation(), Sound.ITEM_FLINTANDSTEEL_USE, 10, 1);
	}

}
