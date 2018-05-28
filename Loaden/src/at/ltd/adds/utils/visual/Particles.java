package at.ltd.adds.utils.visual;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public class Particles {

	EnumParticle particletype;
	boolean longdistance;
	Location location;
	float offsetx;
	float offsety;
	float offsetz;
	float speed;
	int amount;

	public Particles(EnumParticle particletype, Location location, boolean longdistance, float offsetx, float offsety, float offsetz, float speed, int amount) {
		this.particletype = particletype;
		this.location = location;
		this.longdistance = longdistance;
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.offsetz = offsetz;
		this.speed = speed;
		this.amount = amount;
	}

	public synchronized void sendAll() {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.offsetx, this.offsety, this.offsetz, this.speed, this.amount, 0);

		for (Player player : AsyncThreadWorkers.getOnlinePlayers()) {
			if (LocUtils.sameWorld(player.getLocation(), location)) {
				((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
			}
		}
	}

	public synchronized void sendPlayer(Player player) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float) this.location.getX(), (float) this.location.getY(), (float) this.location.getZ(), this.offsetx, this.offsety, this.offsetz, this.speed, this.amount);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

	public static void TNT_Explosion(Location loc) {

		Particles particles = new Particles(EnumParticle.EXPLOSION_NORMAL, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 5);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.SMOKE_NORMAL, loc.add(0, 3.5, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
		particles2.sendAll();

	}

	public static void ROCKET_Explosion(Location loc) {
		Particles particles = new Particles(EnumParticle.EXPLOSION_HUGE, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.SMOKE_NORMAL, loc.add(0, 3.5, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
		particles2.sendAll();

	}

	public static void GRANADE_Explosion(Location loc) {
		Particles particles = new Particles(EnumParticle.EXPLOSION_HUGE, loc.add(0, 0.4, 0), true, 1.2f, 1.2f, 1.2f, 0, 5);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.SMOKE_NORMAL, loc.add(0, 3.5, 0), true, 1.2f, 1.2f, 1.2f, 0, 5);
		particles2.sendAll();

	}
	public static void GRANADE_Tick(Location loc) {
		Particles particles = new Particles(EnumParticle.SMOKE_NORMAL, loc, true, 0.1F, 0.1F, 0.1F, 0, 10);
		particles.sendAll();

	}

	public static void HELLFIRE_Explosion(Location loc) {
		Particles particles = new Particles(EnumParticle.EXPLOSION_HUGE, loc.add(0, 0.4, 0), true, 1.2f, 1.2f, 1.2f, 0, 10);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.SMOKE_NORMAL, loc.add(0, 3.5, 0), true, 1.2f, 1.2f, 1.2f, 0, 20);
		particles2.sendAll();

	}

	public synchronized static void HELLFIRE_CICLE(Location loc) {
		Particles particles = new Particles(EnumParticle.LAVA, loc.subtract(0, 2.4, 0), true, 0.2f, 0.2f, 0.2f, 0, 1);
		particles.sendAll();

	}
	public static void HELLFIRE_TRACE(Location loc) {
		Particles particles = new Particles(EnumParticle.FIREWORKS_SPARK, loc, true, 0.01F, 0.01F, 0.01F, 0, 1);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.SMOKE_NORMAL, loc, true, 0.01F, 0.01F, 0.01F, 0, 1);
		particles2.sendAll();
	}

	public static void FIRE_DIRECT(Location loc) {
		Particles particles = new Particles(EnumParticle.LAVA, loc, true, 0.25f, 0.25f, 0.25f, 0, 25);
		particles.sendAll();
	}

	public static void BULLET_REMOVE(Location loc, Player p) {
		Particles particles = new Particles(EnumParticle.SMOKE_NORMAL, loc, true, 0.1F, 0.1F, 0.1F, 0, 10);
		particles.sendPlayer(p);
	}
	public static void BULLET_TRACE(Location loc) {
		Particles particles = new Particles(EnumParticle.SMOKE_NORMAL, loc, true, 0.01F, 0.01F, 0.01F, 0, 1);
		particles.sendAll();
	}
	public static void ROCKET_TRACE(Location loc) {
		Particles particles = new Particles(EnumParticle.FIREWORKS_SPARK, loc, true, 0.01F, 0.01F, 0.01F, 0, 1);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.CLOUD, loc, true, 0.01F, 0.01F, 0.01F, 0, 1);
		particles2.sendAll();
	}

	// OLD

	public synchronized static void Chest(Location loc) {

		Particles particles = new Particles(EnumParticle.DRIP_LAVA, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 75);
		particles.sendAll();

	}

	public synchronized static void Arrow(Location loc) {
		Particles particles = new Particles(EnumParticle.LAVA, loc.add(0, 0.1, 0), true, 0.05f, 0.05f, 0.5f, 0, 1);
		particles.sendAll();

	}

	public synchronized static void sendPlayer(Location loc, Player p) {
		if (loc.getBlock().getType() == Material.AIR) {
			Particles particles = new Particles(EnumParticle.LAVA, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 75);
			particles.sendPlayer(p);
			Particles particles2 = new Particles(EnumParticle.CLOUD.SMOKE_LARGE, loc.add(0, 3.5, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
			particles2.sendPlayer(p);
		}

	}

	public static void jump(Location loc) {

		Particles particles = new Particles(EnumParticle.CLOUD, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 75);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.CLOUD.SMOKE_LARGE, loc.add(0, 3.5, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
		particles2.sendAll();

	}

	public static void PAT_AXT(Location loc) {

		Particles particles2 = new Particles(EnumParticle.LAVA, loc.add(0, 0.1, 0), true, 0.75f, 0.75f, 0.75f, 0, 4);
		particles2.sendAll();

	}
	public synchronized static void pat(Location loc) {

		Particles particles = new Particles(EnumParticle.LAVA, loc.add(0, 0.4, 0), true, 0.75f, 0.75f, 0.75f, 0, 75);
		particles.sendAll();
		Particles particles2 = new Particles(EnumParticle.CLOUD.SMOKE_LARGE, loc.add(0, 3.5, 0), true, 0.75f, 0.75f, 0.75f, 0, 10);
		particles2.sendAll();

	}

}