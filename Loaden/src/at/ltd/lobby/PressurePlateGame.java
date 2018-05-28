package at.ltd.lobby;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.Cube;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.visual.Particles;
import at.ltd.adds.utils.visual.Title;
import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;

public class PressurePlateGame implements Listener {

	@ConfigAble(key = "END_CUBE_1", value = "-2099.0*25.0*1309.0*0.0*0.0*GG_Lobby")
	public static Location END_CUBE_1;
	@ConfigAble(key = "END_CUBE_2", value = "-2134.0*39.0*1293.0*0.0*0.0*GG_Lobby")
	public static Location END_CUBE_2;
	@ConfigAble(key = "SPAWN", value = "-2077.520066887212*32.0*1345.4772964109586*-225.12573*-1.3499202*GG_Lobby")
	public static Location spawn;

	@ConfigAble(key = "COINS", value = "10")
	public static Integer coins;

	@ConfigAble(key = "HEAD_LOC", value = "-2085.0*33.0*1346.0*0.0*0.0*GG_Lobby")
	public static Location head;
	@ConfigAble(key = "SIGN_LOC", value = "-2084.0*32.0*1346.0*0.0*0.0*GG_Lobby")
	public static Location sign;

	@ConfigAble(key = "REWARD_WAIT_MIN", value = "15")
	public static int mintowait;

	public static HashMap<String, Long> cooldown = new HashMap<>();
	public static Cube cube;

	public static void init() {
		Config.loadMyClass("LobbyPressurePlateGame", PressurePlateGame.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("LobbyPressurePlateGame", PressurePlateGame.class);
		});
		Bukkit.getServer().getPluginManager().registerEvents(new PressurePlateGame(), Main.getPlugin());
		cube = new Cube(END_CUBE_1, END_CUBE_2, true);
	}

	@EventHandler
	public void juI(PlayerInteractEvent ev) {
		if (ev.getAction().equals(Action.PHYSICAL)) {
			Player p = ev.getPlayer();
			if (p.getWorld().getName().equals(LobbyUtils.Lobby_Name_String)) {
				if (ev.getClickedBlock().getType() == Material.STONE_PLATE) {
					if (ev.getClickedBlock().getWorld().getBlockAt(ev.getClickedBlock().getLocation().subtract(0, 1, 0)).getType() == Material.REDSTONE_BLOCK) {
						Title.sendFullTitle(p, 10, 20, 10, Cf.rs(Cf.PLATE_NOPE, p), "");
						Particles.TNT_Explosion(p.getLocation());
						p.teleport(spawn);

					}
				}

				if (ev.getClickedBlock().getType() == Material.WOOD_PLATE) {
					if (ev.getClickedBlock().getWorld().getBlockAt(ev.getClickedBlock().getLocation().subtract(0, 1, 0)).getType() == Material.STAINED_CLAY) {
						if (getRandomBoolean()) {
							Title.sendFullTitle(p, 10, 20, 10, Cf.rs(Cf.PLATE_NOPE, p), "");
							Particles.TNT_Explosion(p.getLocation());
							p.teleport(spawn);
						}

					}
				}
			}

		}
	}

	@EventHandler
	public void on(EventPlayerMoveBlockEventAsync e) {
		Player p = e.getPlayer();
		if (LocUtils.sameWorld(e.getTo(), END_CUBE_1) && cube.isLocationInCube(e.getTo())) {
			if (CommandBuildMode.canBuild(p)) {
				return;
			}
			if (e.getBlockStandingOn().getData() == DyeColor.LIME.getWoolData()) {
				AsyncThreadWorkers.submitSyncWork(() -> {
					p.teleport(spawn);
					setHead(p.getName(), head);
					setSign(p.getName(), sign);
					if (!check(p)) {
						return;
					}
					Cf.rsS(Cf.PLATE_FINISH, p, "[ADDCOINS]", coins);
					Main.getTransaction().add("PRESSURE", coins, p);
				});
			}

		}
	}

	public static boolean getRandomBoolean() {
		return Math.random() < 0.5;
	}

	/*
	 * 0x2: On a wall, facing north 0x3: On a wall, facing south 0x4: On a wall,
	 * facing east 0x5: On a wall, facing west
	 */
	public static void setHead(String skullOwner, Location loc) {
		Particles.FIRE_DIRECT(loc);
		Skull s = (Skull) loc.getBlock().getState();
		s.setSkullType(SkullType.PLAYER);
		s.setOwner(skullOwner);
		s.update();
	}

	public static void setSign(String name, Location loc) {
		loc.getBlock().setType(Material.AIR);
		loc.getBlock().setTypeIdAndData(Material.WALL_SIGN.getId(), (byte) 5, true);
		Sign s = (Sign) loc.getBlock().getState();
		s.setLine(0, "§a§l" + name);
		s.setLine(1, "§0§lhat es als");
		s.setLine(2, "§0§lletztes ");
		s.setLine(3, "§0§lgeschafft.");
		s.update();
	}

	public static synchronized boolean check(Player p) {
		if (cooldown.containsKey(p.getUniqueId().toString())) {
			long last = cooldown.get(p.getUniqueId().toString());
			if ((System.currentTimeMillis() - last) > TimeUnit.MINUTES.toMillis(mintowait)) {
				cooldown.remove(p.getUniqueId().toString());
				cooldown.put(p.getPlayer().getUniqueId().toString(), System.currentTimeMillis());
				return true;
			} else {
				long millis = TimeUnit.MINUTES.toMillis(mintowait) - (System.currentTimeMillis() - last);
				String hms = String.format("%d Minuten, %d Sekunden", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
				Cf.rsS(Cf.PLATE_COOLDOWN, p, "[TIME]", hms);
				return false;
			}
		} else {
			cooldown.put(p.getPlayer().getUniqueId().toString(), System.currentTimeMillis());
			return true;
		}

	}

}
