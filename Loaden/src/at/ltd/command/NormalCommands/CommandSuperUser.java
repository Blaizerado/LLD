package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.threading.AsyncAble;

public class CommandSuperUser implements CommandExecuter {

	public static String seckey = "8V8708MVMBCGHRYGULAR";

	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {

		if (args.size() < 2) {
			p.sendMessage(Main.getPrefix() + "/superuser(/su) registrate <registerkey> §7|§6 to get a\n   google register key");
			p.sendMessage(Main.getPrefix() + "/superuser(/su) <google key> §7|§6 makes you superuser");
			p.sendMessage(Main.getPrefix() + "/superuser(/su) logout §7|§6 logs you out");
			return;
		}
		String cmd = args.get(1);
		String uuid = p.getUniqueId().toString();
		if ("registrate".equals(cmd)) {
			if (args.size() == 3) {
				String key = args.get(2);
				if (seckey.equals(key)) {
					if (!SuperUser.isRegistrated(uuid)) {
						String gkey = SuperUser.register(uuid, p.getName());
						p.sendMessage(Main.getPrefix() + "Your key: §4" + gkey);
						p.sendMessage(Main.getPrefix() + "Download this app:\n§2https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2");
						p.sendMessage(Main.getPrefix() + "Then login with the key which is shown in the app. /su <key>");
					} else {
						p.sendMessage(Main.getPrefix() + "You are already registrated!");
					}
				} else {
					p.sendMessage(Main.getPrefix() + "§4Wrong key!");
				}
			} else {
				p.sendMessage(Main.getPrefix() + "/superuser(/su) registrate <registerkey> | Error on command. Did you type it correctly?");
			}
			return;
		}

		if ("logout".equals(cmd)) {
			if (SuperUser.isSuperUser(uuid)) {
				p.sendMessage(Main.getPrefix() + "§2You are now logged out.");
				SuperUser.logout(uuid);
			} else {
				p.sendMessage(Main.getPrefix() + "You are not logged in.");
			}
			return;
		}

		int key = 0;
		try {
			key = Integer.valueOf(cmd);
		} catch (Exception e) {
			p.sendMessage(Main.getPrefix() + "Thats not a key! /su 234234");
			return;
		}

		if (SuperUser.isSuperUser(uuid)) {
			p.sendMessage(Main.getPrefix() + "§cYou are already logged in!");
			return;
		}

		if (SuperUser.login(uuid, key, p)) {
			p.sendMessage(Main.getPrefix() + "§aYou are logged in!");
		} else {
			p.sendMessage(Main.getPrefix() + "§4Wrong key!");
		}

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
