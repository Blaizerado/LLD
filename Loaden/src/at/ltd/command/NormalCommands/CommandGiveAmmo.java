package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.gungame.guns.utils.bullet.BulletUtils;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandGiveAmmo implements CommandExecuter{

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if(GRankPerm.isTeamMember(p)) {
			ItemStack is = BulletUtils.createBullet();
			is.setAmount(64);
			p.getInventory().addItem(is);
			p.sendMessage(Main.getPrefix() + "Dir wurden 64 Kugeln gegeben. " + BulletUtils.getBulletCount(p.getInventory()));
		}else {
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
