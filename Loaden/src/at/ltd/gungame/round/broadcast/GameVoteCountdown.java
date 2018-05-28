package at.ltd.gungame.round.broadcast;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventVoteTick;

public class GameVoteCountdown implements Listener {

	@EventHandler
	public void on(EventVoteTick e) {
		int secLeft = e.getTick();
		
		if (secLeft < 25) {
			if (secLeft > 0) {
				if (secLeft == 1) {
					for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
						Cf.rsS(Cf.ROUND_VOTE_2_COUNTDOWN, p, "[TIME]", secLeft);
						p.getOpenInventory().getCursor();
					}
				} else {
					if (secLeft == 20) {
						for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
							Cf.rsS(Cf.ROUND_VOTE_1_COUNTDOWN, p, "[TIME]", secLeft);
						}
					} else if (secLeft == 15) {
						for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
							Cf.rsS(Cf.ROUND_VOTE_1_COUNTDOWN, p, "[TIME]", secLeft);
						}
					} else if (secLeft == 10) {
						for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
							Cf.rsS(Cf.ROUND_VOTE_1_COUNTDOWN, p, "[TIME]", secLeft);
						}
					} else if (secLeft < 6) {
						for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
							Cf.rsS(Cf.ROUND_VOTE_1_COUNTDOWN, p, "[TIME]", secLeft);
						}
					}

				}

			}
		}
	}

}
