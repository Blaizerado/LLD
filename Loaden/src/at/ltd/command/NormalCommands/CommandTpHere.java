package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandTpHere implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (GRankPerm.isTeamMember(p)) {
			if (args.size() == 2) {
				String name = args.get(1);
				if (Bukkit.getPlayer(name) == null) {
					Cf.rsS(Cf.TP_PLAYER_NOT_FOUND, p);
					return;
				}
				EventTeleport et = new EventTeleport(Bukkit.getPlayer(name), p.getLocation(), "TPHERE");
				Bukkit.getPluginManager().callEvent(et);
				if (!et.isCancelled()) {
					Bukkit.getPlayer(name).teleport(p.getLocation());
				} else {
					p.sendMessage(Main.getPrefix() + "Spieler: '" + name + "' konnte nicht Teleportiert werden. Reason: '" + et.getCancelReason() + "'.");
					return;
				}

				Cf.rsS(Cf.TP_SUCCESS_1, Bukkit.getPlayer(name), "[TARGET]", p.getName());
				Cf.rsS(Cf.TP_HERE_SUCCESS, p, "[TARGET]", Bukkit.getPlayer(name).getName());

			} else {
				p.sendMessage(Main.getPrefix() + "/tphere <name>");
			}
		} else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
