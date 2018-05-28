package at.ltd.gungame.round;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGunGameDeath;
import at.ltd.gungame.events.custom.EventGunGameWinner;
import at.ltd.gungame.maps.maputils.GameMap;
import at.ltd.gungame.round.vote.RoundVote;

public class RoundManager implements Listener {

	private GameMap CURRENT_GAMEMAP = null;
	private GameRound CURRENT_GAMEROUND;
	private long ROUND_START_TIME;

	@ConfigAble(key = "", value = "51")
	private static int GG_LEVEL_STOP;

	public void start() {
		Config.loadMyClass("RoundEnd", RoundManager.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("RoundEnd", RoundManager.class);
		});

		Main.registerListener(this);
		launchGame();
		check();
		DisplayStats.init();

	}

	
	@EventHandler
	public void on(EventGunGameDeath e) {
		if(e.getKiller() != null) {
			GamePlayer gp = GamePlayer.getGamePlayer(e.getKiller());
			int lvl = gp.getGunGameLevel();
			if (lvl == GG_LEVEL_STOP - 1 | lvl > GG_LEVEL_STOP - 1) {
				if (gp.isInRound()) {
					Cf.rsBC(Cf.GAME_WON, "[NAME]", gp.getPlayer().getName());
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), () -> {
						GunGame.getRoundManager().getGameRound().stop();
					}, 2);

					EventGunGameWinner event = new EventGunGameWinner(gp.getPlayer(), gp);
					Main.callEvent(event);
				}
			}
		}

	}

	public GameMap getGameMap() {
		return CURRENT_GAMEMAP;
	}

	public void setGameMap(GameMap gm) {
		this.CURRENT_GAMEMAP = gm;
	}

	public void launchGame() {
		if (CURRENT_GAMEROUND != null && CURRENT_GAMEROUND.getPlayersInRound() != null) {
			RoundVote.setPlayersLastRound((ArrayList<GamePlayer>) CURRENT_GAMEROUND.getPlayersInRound());
		}

		CURRENT_GAMEROUND = new GameRound();
		RoundVote.vote(this, new Runnable() {

			@Override
			public void run() {
				CURRENT_GAMEROUND.start(CURRENT_GAMEMAP);
				ROUND_START_TIME = System.currentTimeMillis();
			}
		});
	}

	public void check() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@Override
			public void run() {
				if (!RoundVote.isVoting) {
					if (!GunGame.isRound()) {
						if (!RoundVote.isWaitVotingTime()) {
							CURRENT_GAMEROUND.cancelSchedulers();
							launchGame();
						}

					}
				}
			}
		}, 0, 20);
	}

	public GameRound getGameRound() {
		return CURRENT_GAMEROUND;
	}

	public long getGameRoundStartTime() {
		return ROUND_START_TIME;
	}

	public void setGameRoundStartTime(long time) {
		ROUND_START_TIME = time;
	}

}
