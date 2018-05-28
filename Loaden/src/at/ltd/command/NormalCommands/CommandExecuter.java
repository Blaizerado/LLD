package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface CommandExecuter {
	/**
	 *  /test parm1 parm2 parm 3<br>
	 *    <br>
	 *    parm1 = args.get(1);<br>
	 *    parm2 = args.get(2);<br>
	 *    parm3 = args.get(3);<br>
	 *    
	 * @param p
	 * @param args
	 * @throws Exception
	 */
	public void run(Player p, ArrayList<String> args) throws Exception;
	public void onRegister(Plugin plugin) throws Exception;

}
