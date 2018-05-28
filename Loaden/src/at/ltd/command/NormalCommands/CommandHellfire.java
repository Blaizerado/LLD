package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.gungame.utils.hellfire.HellfireFirePlan;
import at.ltd.gungame.utils.hellfire.HellfireRocket;

public class CommandHellfire implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isHighMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		try {
			if (args.size() == 3) {
				int höhe = Integer.valueOf(args.get(1));
				int distanz = Integer.valueOf(args.get(2));

				Location loc = p.getLocation();
				loc.setY(loc.getY() - 1);
				Block b = loc.getBlock();
				Location to = b.getLocation();
				Location from = p.getTargetBlock(null, distanz).getLocation();
				HellfireRocket gr = new HellfireRocket(from, to, höhe, null);
				p.sendMessage(Main.getPrefix() + HellfireFirePlan.build(from, to, höhe));
				new FancyMessage(Main.getPrefix() + "Klick!").suggest(HellfireFirePlan.build(from, to, höhe)).send(p);
				gr.fire();
			} else if (args.size() == 2) {
				String plan = args.get(1);
				HellfireFirePlan.fireRocket(plan);
				p.sendMessage(Main.getPrefix() + "Fire in the air!");
			} else {
				p.sendMessage(Main.getPrefix() + "/hellfire <höhe> <distanz> | /hellfire <plan>");
			}
		} catch (Exception e) {
			e.printStackTrace();
			p.sendMessage(Main.getPrefix() + "Error");
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
