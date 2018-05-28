package at.ltd.adds.utils.visual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.WorldServer;
public class ItemHologram {
	private Location location;
	private List<String> lines;
	private ItemStack head;
	private double distance_above = -0.27;
	private int entityid;
	public List<EntityArmorStand> armorstands = new ArrayList<EntityArmorStand>();

	public ItemHologram(Location loc, ItemStack head, String... lines) {
		this.location = loc;
		this.lines = Arrays.asList(lines);
		this.head = head;

	}

	public ItemHologram(Location loc, ItemStack head) {
		this.location = loc;
		this.head = head;

	}

	public List<String> getLines() {
		return lines;
	}

	public Location getLocation() {
		return location;
	}

	public void send(Player p) {
		armorstands.clear();
		double y = getLocation().getY();
		if (lines == null) {
			y = y + distance_above;
			EntityArmorStand eas = getEntityArmorStand(y);
			entityid = eas.getId();
			display(p, eas);
			PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(eas.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(head));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet2);
		} else {
			for (int i = 0; i <= lines.size() - 1; i++) {
				y = y + distance_above;
				EntityArmorStand eas = getEntityArmorStand(y);
				eas.setCustomName(lines.get(i));
				display(p, eas);
				if (i == 0) {
					entityid = eas.getId();
					PacketPlayOutEntityEquipment packet2 = new PacketPlayOutEntityEquipment(eas.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(head));
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet2);
				}

			}
		}

	}

	public void destroy(Player p) {
		for (EntityArmorStand eas : armorstands) {
			PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(eas.getId());
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
	}

	public void destroy() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			destroy(p);
		}
	}

	public void broadcast() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			send(p);
		}
	}

	public void broadcast(List<Player> players) {
		for (Player p : players) {
			send(p);
		}
	}

	public List<EntityArmorStand> getArmorstands() {
		return armorstands;
	}

	private EntityArmorStand getEntityArmorStand(double y) {
		WorldServer world = ((CraftWorld) getLocation().getWorld()).getHandle();
		EntityArmorStand eas = new EntityArmorStand(world);
		eas.setLocation(getLocation().getX(), y, getLocation().getZ(), getLocation().getYaw() - 180, 0f);
		eas.setInvisible(true);
		eas.setNoGravity(true);
		eas.setInvulnerable(true);
		if (lines != null) {
			eas.setCustomNameVisible(true);
		}
		return eas;
	}

	private void display(Player p, EntityArmorStand eas) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(eas);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		armorstands.add(eas);

	}

	public int getEntityId() {
		return entityid;
	}

}