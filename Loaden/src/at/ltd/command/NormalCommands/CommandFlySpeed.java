package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandFlySpeed implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isTeamMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		if (args.size() == 2) {
			if (!isFloat(args.get(1))) {
				p.sendMessage(Main.getPrefix() + "/flyspeed (<Player>) 1.3,2.3,3,4,5 (Max. 10)");
				return;
			}
			float speed = Float.valueOf(Integer.parseInt(args.get(1)));
			if(speed > 10) {
				Cf.rsS(Cf.FLYSPEED_MAX_SPEED, p);
				return;
			}
			p.setFlySpeed(speed);
			Cf.rsS(Cf.FLYSPEED_SELF_CHANGE, p);
		} else if (args.size() == 3) {
			Player target = null;
			try {
				target = Bukkit.getServer().getPlayer(args.get(2));
				target.getName();
			} catch (Exception e) {
				Cf.rsS(Cf.FLYSPEED_PLAYER_NOT_FOUND, p);
				return;
			}
			if (!isFloat(args.get(1))) {
				p.sendMessage(Main.getPrefix() + "/flyspeed 1.3,2.3,3,4,5; (<Player>) (Max. 10)");
				return;
			}
			float speed = Float.valueOf(Integer.parseInt(args.get(1))); 
			if(speed > 10) {
				Cf.rsS(Cf.FLYSPEED_MAX_SPEED, p);
				return;
			}
			target.setFlySpeed(speed);
			Cf.rsS(Cf.FLYSPEED_OTHER_CHANGE_1, p);
			Cf.rsS(Cf.FLYSPEED_OTHER_CHANGE_2, target, "[PLAYER]", p.getName());
		}

	}

	public static boolean isFloat(String s) {
		try {
			Float.valueOf(Integer.parseInt(s));  
			return true;
		} catch (Exception e) {
		}
		return false;
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
