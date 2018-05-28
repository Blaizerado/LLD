package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.bansystem.events.custom.EventBan;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.command.CommandManager;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandBan implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!hasPerm(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		if (args.size() > 2 | args.size() == 2) {
			String arg1 = args.get(1);
			if (arg1.length() == 36) {
				if (BanUtils.doesUUIDExist(arg1)) {
					String uuid = arg1;
					EventBan eb = new EventBan(p, uuid, BanUtils.getNameFromUUID(uuid), BanStatus.BANNED, 0L, false);
					Main.callEvent(eb);
					if (eb.isCancelled()) {
						Cf.rsS(Cf.BAN_CANCEL, p);
						return;
					}
					if (args.size() == 2) {
						Main.banPlayer(uuid);
					} else {
						Main.banPlayer(uuid, CommandManager.getLastArgs(args, 2));
					}
					Cf.rsS(Cf.BAN_SUCCESS, p);
					return;
				} else {
					p.sendMessage(Main.getPrefix() + "§cDiese UUID ist der Datenbank nicht bekannt.");
					return;
				}
			}
			Player target = Bukkit.getPlayer(args.get(1));
			if (target == null) {
				Cf.rsS(Cf.PLAYER_NOT_FOUND, p);
				return;
			}
			EventBan eb = new EventBan(p, target.getUniqueId().toString(), target.getName(), BanStatus.BANNED, 0L, false);
			Main.callEvent(eb);
			if (eb.isCancelled()) {
				Cf.rsS(Cf.BAN_CANCEL, p);
				return;
			}

			if (args.size() == 2) {
				Main.banPlayer(target);
			} else {
				Main.banPlayer(target, CommandManager.getLastArgs(args, 2));
			}
			Cf.rsS(Cf.BAN_SUCCESS, p);
		} else {
			p.sendMessage(Main.getPrefix() + "/ban <Player> (<Reason>)");
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

	public boolean hasPerm(Player p) {
		return GRankPerm.isAllowedToBan(p);
	}

}
