package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.JsonMSG.FancyMessage;
import at.ltd.adds.utils.net.web.WebData;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;
import at.ltd.gungame.ranks.GRankPerm;
import at.ltd.lobby.shop.LobbyShopItemSerializer;

public class CommandShopItem implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isHighMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		if (args.size() == 2) {
			int coins = Integer.valueOf(args.get(1));
			if (p.getInventory().getItemInMainHand() != null) {
				ItemStack is = p.getInventory().getItemInMainHand();
				String ser = LobbyShopItemSerializer.toString(is, coins);
				new FancyMessage(Main.getPrefix() + "Klick!").suggest(ser).send(p);
				p.sendMessage(Main.getPrefix() + "Oder: " + WebData.addData(ser));
				System.out.println(ser);

			} else {
				p.sendMessage(Main.getPrefix() + "Nimm ein item in die Hand");
			}

		} else if (args.size() == 3) {
			int coins = Integer.valueOf(args.get(1));
			int kills = Integer.valueOf(args.get(2));
			ItemStack is = p.getInventory().getItemInMainHand();
			GunInterface gi = GunUtils.getGunInterfaceByItemManaged(is);
			if (gi != null) {
				String ser = LobbyShopItemSerializer.toString(gi, coins, kills);
				new FancyMessage(Main.getPrefix() + "Klick!").suggest(ser).send(p);
				System.out.println(ser);
			} else {
				p.sendMessage(Main.getPrefix() + "Es wurde keine Waffe gefunden!");
			}

		} else {
			p.sendMessage(Main.getPrefix() + "/shopitem <coins(Kosten)> (<gun kills>)");
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
