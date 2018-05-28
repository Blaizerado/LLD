package at.ltd.lobby;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.Cube;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.command.NormalCommands.CommandBuildMode;
import at.ltd.events.custom.EventPlayerMoveAsync;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.round.vote.RoundVote;

public class PortalJoin implements Listener {

	public static Location loc1;
	public static Location loc2;

	public static Cube cube1;
	public static Cube cube2;

	public static void start() {
		PortalJoin.loc1 = LocUtils.locationByString("-2029.1767813025647*30.200000047683716*1304.4107666806299*-180.26697*2.2500594*GG_Lobby");
		PortalJoin.loc2 = LocUtils.locationByString("-2106.463*48.751*1381.401*-0.3799*0.129*GG_Lobby");
		Bukkit.getServer().getPluginManager().registerEvents(new PortalJoin(), Main.getPlugin());
		cube1 = new Cube(LocUtils.locationByString("-2033.0*26.0*1304.0*0.0*0.0*GG_Lobby"), LocUtils.locationByString("-2027.0*31.0*1301.0*0.0*0.0*GG_Lobby"), true);
		cube2 = new Cube(LocUtils.locationByString("-2109.0*53.0*1380.0*0.0*0.0*GG_Lobby"), LocUtils.locationByString("-2104.0*46.0*1384.0*0.0*0.0*GG_Lobby"), true);
	}

	@EventHandler
	public void on(EventPlayerMoveAsync e) {
		Location loc = AsyncThreadWorkers.getPlayerLocation(e.getPlayer());
		if (cube1.isLocationInCube(loc)) {
			if (cube1.cooldown(e.getPlayer(), 1)) {
				Player p = e.getPlayer();
				if (!CommandBuildMode.canBuild(p)) {
					if (GunGame.isRound()) {
						AsyncThreadWorkers.submitSyncWork(() -> GamePlayer.getGamePlayer(p).joinGunGame(false));
					} else {
						AsyncThreadWorkers.submitSyncWork(() -> {
							p.teleport(loc1);
							Vector v = p.getLocation().getDirection().multiply(-1D).setY(0.5D);
							p.setVelocity(v);
						});

					}
					vote(p);
				}
			}
		} else {
			if (cube2.isLocationInCube(loc)) {
				if (cube2.cooldown(e.getPlayer(), 1)) {
					Player p = e.getPlayer();
					if (!CommandBuildMode.canBuild(p)) {
						if (GunGame.isRound()) {
							AsyncThreadWorkers.submitSyncWork(() -> GamePlayer.getGamePlayer(p).joinGunGame(false));
						} else {
							AsyncThreadWorkers.submitSyncWork(() -> {
								p.teleport(loc2);
								Vector v = p.getLocation().getDirection().multiply(-1D).setY(0.2D);
								p.setVelocity(v);
							});

						}
						vote(p);
					}
				}
			}
		}
	}

	public static void vote(Player p) {
		if (RoundVote.isVoting()) {
			if (!RoundVote.voters.keySet().contains(p) && !RoundVote.playerUUIDs.containsKey(p.getUniqueId().toString())) {

				Cf.rsS(Cf.PORTAL_JOIN_VOTE, p);
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {

					@Override
					public void run() {
						RoundVote.vote(p);
					}
				}, 17);
				return;
			}
		}
		Cf.rsS(Cf.JOIN_GAME_NOT_STARTED, p);
	}
}
