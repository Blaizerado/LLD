package at.ltd.lobby.shop.reflections;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.ltd.adds.utils.LocUtils;
import at.ltd.lobby.shop.data.LobbyShopMobData;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityLiving;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.World;

public class LobbyShopFakeMob {

	public static Entity spawn(LobbyShopMobs ENTITY_TYPE, Location SHOP_LOC, String SHOPMOB_NAME, Player p) {
		PacketPlayOutSpawnEntityLiving create = null;
		EntityLiving entity = null;

		try {
			World world = ((CraftWorld) SHOP_LOC.getWorld()).getHandle();
			entity = (EntityLiving) ReflectionUtil.getNmsClass(ENTITY_TYPE.getNmsClass()).getConstructor(ReflectionUtil.getNmsClass("World")).newInstance(ReflectionUtil.getWorldServer(SHOP_LOC.getWorld()));

			entity.setLocation(SHOP_LOC.getX(), SHOP_LOC.getY(), SHOP_LOC.getZ(), SHOP_LOC.getYaw(), SHOP_LOC.getPitch());
			entity.spawnIn(world);
			entity.setCustomName(SHOPMOB_NAME);
			entity.setCustomNameVisible(true);
			entity.setNoGravity(true);
			create = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (create != null) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(create);
		}
		return entity;
	}

	public static void headMob(Player p, LobbyShopMobData lsmd) {
		try {
			Location loc = LocUtils.lookAt(lsmd.getLobbyShopUnit().getShopLocation(), p.getLocation());
			ReflectionUtil.sendPacket(p, new PacketPlayOutEntityHeadRotation(lsmd.getEntity(), toAngle(loc.getYaw())));

			ReflectionUtil.sendPacket(p, new PacketPlayOutEntityLook(lsmd.getEnityID(), toAngle(loc.getYaw()), toAngle(loc.getPitch()), true));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int floor(double var0) {
		int var2 = (int) var0;
		return var0 < (double) var2 ? var2 - 1 : var2;
	}

	public static byte toAngle(float v) {
		return (byte) ((int) (v * 256.0F / 360.0F));
	}

	public static double toDelta(double v) {
		return ((v * 32) * 128);
	}

}
