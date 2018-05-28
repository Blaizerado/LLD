package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.gungame.guns.utils.rocket.RocketUtils;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandGiveRocket implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (GRankPerm.isTeamMember(p)) {
			ItemStack is = RocketUtils.createRocket();
			is.setAmount(64);
			p.getInventory().addItem(is);
			p.sendMessage(Main.getPrefix() + "Dir wurden 64 Racketen gegeben. " + RocketUtils.getRocketCount(p.getInventory()));
		} else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
