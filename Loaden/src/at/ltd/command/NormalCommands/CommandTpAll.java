package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandTpAll implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (GRankPerm.isHighMember(p)) {
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (all.getName().equals(p.getName())) {
					continue;
				}
				
				Cf.rsS(Cf.TP_SUCCESS_1, all, "[TARGET]", p.getName());
				
				EventTeleport et = new EventTeleport(all, p.getLocation(), "TPALL");
				Bukkit.getPluginManager().callEvent(et);
				if(!et.isCancelled()) {
					all.teleport(p);
				}else {
					p.sendMessage(Main.getPrefix() + "Spieler: '"+all.getName()+"' konnte nicht Teleportiert werden. Reason: '" + et.getCancelReason() + "'.");
					continue;
				}
				
			}
			Cf.rsS(Cf.TP_ALL, p);
		} else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
