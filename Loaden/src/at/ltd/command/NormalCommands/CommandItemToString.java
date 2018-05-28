package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.net.web.WebData;
import at.ltd.adds.utils.serializer.ItemSerializer;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandItemToString implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isTeamMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		ItemStack is = p.getInventory().getItemInMainHand();
		if (is == null || is.getType() == Material.AIR) {
			p.sendMessage(Main.getPrefix() + "Du musst ein Item ausgerüstet haben.");
		} else {
			String item = ItemSerializer.serialize(is);
			new FancyMessage(Main.getPrefix() + "Klick!").suggest(item).send(p);
			p.sendMessage(Main.getPrefix() + "Oder: §c" + WebData.addData(item));
			System.out.println(Main.getPrefix() + "ITEM: " + item);

		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
