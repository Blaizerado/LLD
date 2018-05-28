package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandClearChat implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isTeamMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		for (int i = 0; i < 20; i++) {
			Main.broadcastMessageGlobal("");
		}
		Cf.rsBC(Cf.CLEAR_CHAT, "[PLAYER]", p.getName());
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
