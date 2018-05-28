package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.adds.Cf;

public class CommandPing implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		int ping = ((CraftPlayer) p).getHandle().ping;
		Cf.rsS(Cf.COMMAND_PING, p, "[PING]", ping);

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
