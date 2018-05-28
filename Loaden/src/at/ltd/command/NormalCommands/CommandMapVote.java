package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;
import at.ltd.gungame.round.vote.RoundVote;

public class CommandMapVote implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (RoundVote.isVoting) {
			if (!RoundVote.voters.keySet().contains(p) && !RoundVote.playerUUIDs.containsKey(p.getUniqueId().toString())) {
				RoundVote.vote(p);
			} else {
				Cf.rsS(Cf.MAP_VOTE_COMMAND_2, p, "[VOTEDMAP]", RoundVote.playerUUIDs.get(p.getUniqueId().toString()).getMapName());
			}
		} else {
			p.sendMessage(Cf.rs(Cf.MAP_VOTE_COMMAND_1, p));
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
