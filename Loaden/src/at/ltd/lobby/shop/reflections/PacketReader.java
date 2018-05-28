package at.ltd.lobby.shop.reflections;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_12_R1.Packet;

public class PacketReader {

	private Player player;
	private Channel channel;
	public static HashMap<Player, PacketReader> READERS = new HashMap<>();

	public PacketReader(Player player) {
		this.player = player;
	}

	public void inject() {
		CraftPlayer player = (CraftPlayer) this.player;
		channel = player.getHandle().playerConnection.networkManager.channel;
		channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
			@Override
			protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) throws Exception {
				arg2.add(packet);
				readPackets(packet);
			}
		});
		READERS.put(player, this);
	}

	public void uninject() {
		READERS.remove(player);
		if (channel.pipeline().get("PacketInjector") != null) {
			try {
				channel.pipeline().remove("PacketInjector");
			} catch (Exception e) {
				System.out.println("[LTD] PacketInjector remove error!");
				e.printStackTrace();
			}
			
		}
	}

	public Player getPlayer() {
		return player;
	}

	private void readPackets(Packet<?> packet) {
		if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
			int id = (Integer) getValue(packet, "a");
			AsyncThreadWorkers.submitWork(new Runnable() {

				@Override
				public void run() {
					EventPlayerKlickOnEntity event = new EventPlayerKlickOnEntity(player, id);
					try {
						Bukkit.getPluginManager().callEvent(event);
					} catch (Exception e) {
					}

				}
			});
		}

	}

	public Object getValue(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
		}
		return null;
	}

}
