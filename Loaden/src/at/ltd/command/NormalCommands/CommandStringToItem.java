package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandStringToItem implements CommandExecuter{

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if(GRankPerm.isTeamMember(p)){
			if (args.size() > 1) {
				String item = args.get(1);
				try {
					ItemStack is = ItemSerializer.deserialize(item);
					p.getInventory().addItem(is);
					p.sendMessage(Main.getPrefix() + "fertig!");
				} catch (Exception e) {
					p.sendMessage(Main.getPrefix() + "Gab ein leichten fehler!");
					e.printStackTrace();
				}
			}else{
				p.sendMessage(Main.getPrefix() + "Nicht richtig! /itemback <String>");
			}
		}else{
			Cf.rsS(Cf.NO_RIGHTS, p);
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
