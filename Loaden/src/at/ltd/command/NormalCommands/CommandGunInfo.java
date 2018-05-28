package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.gungame.guns.utils.Gun;
import at.ltd.gungame.guns.utils.GunInterface;
import at.ltd.gungame.guns.utils.GunUtils;

public class CommandGunInfo implements CommandExecuter {

	@Override
	@AsyncAble
	public void run(Player p, ArrayList<String> args) throws Exception {
		ItemStack is = p.getInventory().getItemInMainHand();
		Gun gun = GunUtils.getGunMemoryManaged(is);
		if (gun != null) {
			GunInterface gi = gun.getGunInterface();
			p.sendMessage(Main.getPrefix() + "§8GunName§7:§6 " + ChatColor.stripColor(gi.getName()));
			p.sendMessage(Main.getPrefix() + "§8GunID§7:§6 " + gi.getGunID());
			p.sendMessage(Main.getPrefix() + "§8MagSize§7:§6 " + gi.getMagazineSize());
			p.sendMessage(Main.getPrefix() + "§8Damage§7:§6 " + gi.getDamage());
			p.sendMessage(Main.getPrefix() + "§8Speed§7:§6 " + gi.getBulletSpeed());
			p.sendMessage(Main.getPrefix() + "§8FireSpeed§7:§6 " + gi.getFireSpeed());
			p.sendMessage(Main.getPrefix() + "§8InstantHit§7:§6 " + gi.isInstantHit());
			p.sendMessage(Main.getPrefix() + "§8Akimbo:§7:§6 " + gi.isAkimbo());
		} else {
			p.sendMessage(Main.getPrefix() + "Keine Waffe gefunden.");
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
