package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.bansystem.BanUtils;
import at.ltd.adds.bansystem.BanUtils.BanStatus;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandUnbann implements CommandExecuter {

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (GRankPerm.isAllowedToBan(p)) {
			if (args.size() == 2) {
				String uuid = args.get(1);
				if (Main.doesUUIDExist(uuid)) {
					BanStatus bs = BanUtils.getBanStatus(uuid);
					if (bs == BanStatus.BANNED | bs == BanStatus.TIME_BANNED) {
						SQLPlayer sql = new SQLPlayer(uuid, Main.getConnectionHandler());
						sql.load();
						sql.setBanned(false);
						sql.setTimebantime(0);
						sql.save();
						p.sendMessage(Main.getPrefix() + sql.getName() + " wurde entbannt.");
					} else {
						p.sendMessage(Main.getPrefix() + "Der Spieler hat keinen Banneintrag.");
					}

				} else {
					Cf.rsS(Cf.PLAYER_NOT_FOUND, p);
				}
			} else {
				p.sendMessage(Main.getPrefix() + "/unbann <uuid>");
			}
		} else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
