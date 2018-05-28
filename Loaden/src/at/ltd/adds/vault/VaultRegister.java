package at.ltd.adds.vault;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;

import at.ltd.Main;
import at.ltd.adds.sql.SQLPlayer;
import at.ltd.adds.sql.sqlutils.SQLCollection;
import at.ltd.gungame.guns.utils.GunUtils;
import net.milkbowl.vault.economy.Economy;

public class VaultRegister implements Listener {

	public static void init() {
		if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
			Bukkit.getServer().getServicesManager().register(Economy.class, new LtdMoney(), Main.getPlugin(), ServicePriority.Highest);
		}

		Main.getJavaPlugin().getCommand("givemoney").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
				if (!(sender instanceof Player)) {
					Player target = (Bukkit.getServer().getPlayer(args[0]));
					int coins = Integer.valueOf(args[1]);
					SQLPlayer player = SQLCollection.getPlayer(target);
					player.setCoins(player.getCoins() + coins);
				} else {
					sender.sendMessage(Main.getPrefix() + "Only Console.");
				}

				return false;
			}
		});

		Main.getJavaPlugin().getCommand("givegun").setExecutor(new CommandExecutor() {

			@Override
			public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
				if (!(sender instanceof Player)) {
					Player target = (Bukkit.getServer().getPlayer(args[0]));
					int gunid = Integer.valueOf(args[1]);
					int kills = Integer.valueOf(args[2]);
					target.getInventory().addItem(GunUtils.createGun(GunUtils.getGunInterfaceByID(gunid), target, kills).getItemStack());
				} else {
					sender.sendMessage(Main.getPrefix() + "Only Console. /givegun <player> <gunid> <kills>");
				}

				return false;
			}
		});
	}

}
