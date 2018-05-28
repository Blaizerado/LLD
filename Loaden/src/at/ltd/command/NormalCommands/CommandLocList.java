package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.superuser.SuperUser;
import at.ltd.adds.utils.LocUtils;
import at.ltd.adds.utils.net.web.WebData;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.events.custom.EventPlayerInteractAsync;
import at.ltd.events.custom.EventPlayerQuitAsync;

public class CommandLocList implements CommandExecuter, Listener {

	public static boolean inUse = false;
	public static ArrayList<String> LOCS = new ArrayList<>();
	public static Player currenuser;
	
	@AsyncAble
	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (inUse == true) {
			p.sendMessage(Main.getPrefix() + "Gerade in benutzung!");
		}
		if (!SuperUser.isSuperUser(p)) {
			Cf.rsS(Cf.SUPERUSER_NORIGHTS, p);
			return;
		}
		p.sendMessage(Main.getPrefix() + "Rechts klick -> neue Location! Links klick -> Fertig");
		currenuser = p;
		inUse = true;
	}

	@EventHandler
	public void on(EventPlayerInteractAsync e) {
		if (inUse) {
			if (e.getPlayer() == currenuser) {
				Player p = e.getPlayer();
				if (e.getAction() == Action.LEFT_CLICK_BLOCK | e.getAction() == Action.LEFT_CLICK_AIR) {
					if (LOCS.size() == 0) {
						p.sendMessage(Main.getPrefix() + "Abbruch.");
						return;
					}
					p.sendMessage(Main.getPrefix() + "Fertig: " + WebData.addData(StringUtils.join(LOCS, "|")));
					LOCS.clear();
					currenuser = null;
					inUse = false;
				} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					String loc = LocUtils.locationToString(e.getClickedBlock().getLocation());
					if (!LOCS.contains(loc)) {
						LOCS.add(loc);
						p.sendMessage(Main.getPrefix() + "Hinzugefügt! " + LocUtils.locationToString(e.getClickedBlock().getLocation()));
					}
				}
			}
		}
	}

	@EventHandler
	public void on(EventPlayerQuitAsync e) {
		if (inUse) {
			if (e.getPlayer() == currenuser) {
				LOCS.clear();
				currenuser = null;
				inUse = false;
			}
		}
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Main.registerListener(this);
	}

}
