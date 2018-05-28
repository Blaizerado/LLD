package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandGM implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (args.size() == 2) {
			if (!GRankPerm.isTeamMember(p)) {
				if (!p.isOp()) {
					Cf.rsS(Cf.NO_RIGHTS, p);
					return;
				}
			}

			if (!isNumber(args.get(1))) {
				p.sendMessage(Main.getPrefix() + "/gm [0,1,2,3] (<Player>) (Nur Nummern)");
				return;
			}

			int gm = Integer.valueOf(args.get(1));
			if (gm == 0) {
				p.setGameMode(GameMode.SURVIVAL);
				Cf.rsS(Cf.GM_SUCCESS_1, p);
				return;
			}
			if (gm == 1) {
				p.setGameMode(GameMode.CREATIVE);
				Cf.rsS(Cf.GM_SUCCESS_1, p);
				return;
			}
			if (gm == 2) {
				p.setGameMode(GameMode.ADVENTURE);
				Cf.rsS(Cf.GM_SUCCESS_1, p);
				return;
			}
			if (gm == 3) {
				p.setGameMode(GameMode.SPECTATOR);
				Cf.rsS(Cf.GM_SUCCESS_1, p);
				return;
			}
			p.sendMessage(Main.getPrefix() + "/gm [0,1,2,3] (Nur von 0-3)");

		} else {
			if (args.size() == 3) {
				if (!GRankPerm.isTeamMember(p)) {
					if (!p.isOp()) {
						Cf.rsS(Cf.NO_RIGHTS, p);
						return;
					}
				}

				Player gmchange;
				if (Bukkit.getPlayer(args.get(2)) != null) {
					gmchange = Bukkit.getPlayer(args.get(2));
				} else {
					Cf.rsS(Cf.GM_PLAYER_NOT_FOUND, p);
					return;
				}
				if (!isNumber(args.get(1))) {
					p.sendMessage(Main.getPrefix() + "/gm [0,1,2,3] <Player>");
					return;
				}

				int gm = Integer.valueOf(args.get(1));
				if (gm == 0) {
					gmchange.setGameMode(GameMode.SURVIVAL);
					Cf.rsS(Cf.GM_SUCCESS_2, p);
					gmchange.sendMessage(Cf.rs(Cf.GM_SUCCESS_3, "[CHANGER]", p.getName()));
					return;
				}
				if (gm == 1) {
					gmchange.setGameMode(GameMode.CREATIVE);
					Cf.rsS(Cf.GM_SUCCESS_2, p);
					gmchange.sendMessage(Cf.rs(Cf.GM_SUCCESS_3, "[CHANGER]", p.getName()));
					return;
				}
				if (gm == 2) {
					gmchange.setGameMode(GameMode.ADVENTURE);
					Cf.rsS(Cf.GM_SUCCESS_2, p);
					gmchange.sendMessage(Cf.rs(Cf.GM_SUCCESS_3, "[CHANGER]", p.getName()));
					return;
				}
				if (gm == 3) {
					gmchange.setGameMode(GameMode.SPECTATOR);
					gmchange.sendMessage(Cf.rs(Cf.GM_SUCCESS_3, "[CHANGER]", p.getName()));
					Cf.rsS(Cf.GM_SUCCESS_2, p);
					return;
				}

				p.sendMessage(Main.getPrefix() + "/gm  [0,1,2,3] <Player> (Nur von 0-3)");
			} else {
				p.sendMessage(Main.getPrefix() + "/gm [0,1,2,3] <Player> ");
			}

		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

	public boolean isNumber(String text) {
		return NumberUtils.isNumber(text);
	}

}
