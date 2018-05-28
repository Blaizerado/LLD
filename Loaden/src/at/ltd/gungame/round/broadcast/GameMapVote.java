package at.ltd.gungame.round.broadcast;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.events.custom.EventVote;

public class GameMapVote implements Listener {

	@EventHandler(priority = EventPriority.LOW)
	public void on(EventVote e) {
		if (!e.isCancelled()) {
			Player p = e.getVoter();
			for (Player pl : AsyncThreadWorkers.getOnlinePlayers()) {
				if (pl != p) {
					Cf.rsS(Cf.ROUND_VOTE_5, pl, "[VOTER]", p.getName(), "[MAP]", e.getVotedMap().getMapName());
				}
			}
		}
	}

}
