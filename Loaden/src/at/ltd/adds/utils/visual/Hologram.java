package at.ltd.adds.utils.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
public class Hologram {
	private Location location;
	private List<String> lines;
	private ItemStack head;
	private double distance_above = -0.27;
	private List<EntityArmorStand> armorstands = new ArrayList<EntityArmorStand>();

	public Hologram(Location loc, String... lines) {
		this.location = loc;
		this.lines = Arrays.asList(lines);
	}
	public Hologram(Location loc, ItemStack head, String... lines) {
		this.location = loc;
		this.lines = Arrays.asList(lines);
		this.head = head;

	}
	public Hologram(Location loc, List<String> lines) {
		this.location = loc;
		this.lines = (ArrayList<String>) lines;
	}

	public List<String> getLines() {
		return lines;
	}

	public Location getLocation() {
		return location;
	}

	public void send(Player p) {
		double y = getLocation().getY();
		for (int i = 0; i <= lines.size() - 1; i++) {
			y = y + distance_above;
			EntityArmorStand eas = getEntityArmorStand(y);
			eas.setCustomName(lines.get(i));
			display(p, eas);
			if (i == 0) {
				PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(eas.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(head));
				((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet2);
			}
			armorstands.add(eas);
		}
	}

	public void destroy(Player p) {
		for (EntityArmorStand eas : armorstands) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(eas.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void destroy() {
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			destroy(p);
		}
	}

	public void broadcast() {
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			send(p);
		}
	}

	public void broadcast(List<Player> players) {
		for (Player p : players) {
			send(p);
		}
	}

	private EntityArmorStand getEntityArmorStand(double y) {
		WorldServer world = ((CraftWorld) getLocation().getWorld()).getHandle();
		EntityArmorStand eas = new EntityArmorStand(world);
		eas.setLocation(getLocation().getX(), y, getLocation().getZ(), 0f, 0f);
		eas.setInvisible(true);
		eas.setNoGravity(true);
		eas.setInvulnerable(true);
		eas.setCustomNameVisible(true);
		return eas;
	}

	public List<EntityArmorStand> getArmorstands() {
		return armorstands;
	}
	private void display(Player p, EntityArmorStand eas) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(eas);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);

	}
}