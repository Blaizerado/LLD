package at.ltd.lobby;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerJoinAsync;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.events.custom.EventJoinGame;

public class LobbyNightVision implements Listener {

	public static void init() {
		Bukkit.getPluginManager().registerEvents(new LobbyNightVision(), Main.getPlugin());
	}

	@EventHandler
	public void on(EventPlayerJoinAsync e) {
		sendAddPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE - 100, 10));
	}

	@EventHandler
	public void in(EventTeleport e) {
		AsyncThreadWorkers.submitWork(() -> {
			if (!LobbyUtils.isLocationInLobby(e.getLocation())) {
				sendRemovePotionEffect(e.getPlayer(), PotionEffectType.NIGHT_VISION);
			} else {
				AsyncThreadWorkers.submitDelayedWorkSec(() -> {
					sendAddPotionEffect(e.getPlayer(), new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE - 100, 10));
				}, 1);

			}
		});
	}

	@EventHandler
	public void on(EventJoinGame e) {
		AsyncThreadWorkers.submitWork(() -> {
			if (!LobbyUtils.isLocationInLobby(AsyncThreadWorkers.getEntityLocation(e.getPlayer()))) {
				sendRemovePotionEffect(e.getPlayer(), PotionEffectType.NIGHT_VISION);
			}
		});
	}

	public static void sendAddPotionEffect(Player p, PotionEffect effect) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EFFECT);
		int effectID = effect.getType().getId();
		int amplifier = effect.getAmplifier();
		int duration = effect.getDuration();
		int entityID = p.getEntityId();
		packet.getIntegers().write(0, entityID);
		packet.getBytes().write(0, (byte) effectID);
		packet.getBytes().write(1, (byte) amplifier);
		packet.getIntegers().write(1, duration);
		packet.getBytes().write(2, (byte) 0);
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet", e);
		}
	}

	public static void sendRemovePotionEffect(Player p, PotionEffectType type) {
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.REMOVE_ENTITY_EFFECT);
		int effectID = type.getId();
		packet.getIntegers().write(0, effectID);
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot send packet", e);
		}
	}

}
