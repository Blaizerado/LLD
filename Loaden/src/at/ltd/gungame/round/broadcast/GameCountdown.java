package at.ltd.gungame.round.broadcast;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.adds.game.player.GamePlayer;
import at.ltd.gungame.GunGame;
import at.ltd.gungame.events.custom.EventGameTick;

public class GameCountdown implements Listener {

	@EventHandler
	public void on(EventGameTick e) {
		int secLeft = e.getTick();
		if (secLeft < 16) {
			if (secLeft > 0) {
				if (secLeft == 1) {
					for (GamePlayer ggp : GunGame.getRoundManager().getGameRound().getPlayersInRound()) {
						Cf.rsS(Cf.ROUND_END_5_COUNTDOWN, ggp.getPlayer(), "[TIME]", secLeft);
					}
				} else {
					if (secLeft == 20) {
						for (GamePlayer ggp : GunGame.getRoundManager().getGameRound().getPlayersInRound()) {
							Cf.rsS(Cf.ROUND_END_4_COUNTDOWN, ggp.getPlayer(), "[TIME]", secLeft);
						}
					} else if (secLeft == 15) {
						for (GamePlayer ggp : GunGame.getRoundManager().getGameRound().getPlayersInRound()) {
							Cf.rsS(Cf.ROUND_END_4_COUNTDOWN, ggp.getPlayer(), "[TIME]", secLeft);
						}
					} else if (secLeft == 10) {
						for (GamePlayer ggp : GunGame.getRoundManager().getGameRound().getPlayersInRound()) {
							Cf.rsS(Cf.ROUND_END_4_COUNTDOWN, ggp.getPlayer(), "[TIME]", secLeft);
						}
					} else if (secLeft < 6) {
						for (GamePlayer ggp : GunGame.getRoundManager().getGameRound().getPlayersInRound()) {
							Cf.rsS(Cf.ROUND_END_4_COUNTDOWN, ggp.getPlayer(), "[TIME]", secLeft);
						}
					}

				}

			}
		}
	}

}
