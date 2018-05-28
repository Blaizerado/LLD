package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.events.custom.EventTeleport;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandTp implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 2) {
			if (!GRankPerm.isTeamMember(p)) {
				if (!p.isOp()) {
					Cf.rsS(Cf.NO_RIGHTS, p);
					return;
				}
			}
			if (Bukkit.getPlayer(args.get(1)) != null) {
				Player toTp = Bukkit.getPlayer(args.get(1));

				EventTeleport et = new EventTeleport(p, toTp.getLocation(), "TP");
				Bukkit.getPluginManager().callEvent(et);
				if (!et.isCancelled()) {
					p.teleport(toTp.getLocation());
				} else {
					p.sendMessage(Main.getPrefix() + "Tp ist derzeit nicht möglich. Reason: '" + et.getCancelReason() + "'.");
					return;
				}

				p.sendMessage(Cf.rs(Cf.TP_SUCCESS_1, "[TARGET]", toTp.getName()));
			} else {
				Cf.rsS(Cf.TP_PLAYER_NOT_FOUND, p);
			}

		} else {
			if (args.size() == 3) {
				if (!GRankPerm.isTeamMember(p)) {
					if (!p.isOp()) {
						Cf.rsS(Cf.NO_RIGHTS, p);
						return;
					}
				}

				Player from;
				Player to;
				if (Bukkit.getPlayer(args.get(1)) != null) {
					from = Bukkit.getPlayer(args.get(1));
				} else {
					Cf.rsS(Cf.TP_PLAYER_NOT_FOUND, p);
					return;
				}

				if (Bukkit.getPlayer(args.get(2)) != null) {
					to = Bukkit.getPlayer(args.get(2));
				} else {
					Cf.rsS(Cf.TP_PLAYER_NOT_FOUND, p);
					return;
				}
				p.sendMessage(Cf.rs(Cf.TP_SUCCESS_2, "[FROM]", from.getName(), "[TARGET]", to.getName()));
				EventTeleport et = new EventTeleport(from, to.getLocation(), "TP");
				Bukkit.getPluginManager().callEvent(et);
				if (!et.isCancelled()) {
					from.teleport(to.getLocation());
				} else {
					p.sendMessage(Main.getPrefix() + "Tp ist derzeit nicht möglich. Reason: '" + et.getCancelReason() + "'.");
				}

			} else {
				p.sendMessage(Main.getPrefix() + "/tp <Player> (<Player>)");
			}

		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
