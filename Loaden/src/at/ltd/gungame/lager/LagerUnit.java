package at.ltd.gungame.lager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.events.custom.EventTeleport;

public class LagerUnit {

	private Player player;
	private int x;
	private static final float y = 30F;
	private static final float z = 85.42F;
	private static final World world = Bukkit.getWorld("GG_Lager");

	public LagerUnit(int x, Player p) {
		this.x = x;
		this.player = p;
	}

	public LagerUnit(Player p) {
		this.x = LagerUtils.getX(p);
		this.player = p;
	}

	public void tp() {
		Location loc = new Location(world, x + 0.5, y, z, (float) -179.4, (float) -0.150);
		EventTeleport et = new EventTeleport(player, loc, "LAGER");
		Main.callEvent(et);
		if (!et.isCancelled()) {
			player.teleport(et.getLocation());
			Cf.rsS(Cf.LAGER_TP, player);
		} else {
			Cf.rsS(Cf.LAGER_NO_TP, player);
		}
	}

}
