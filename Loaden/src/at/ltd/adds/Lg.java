package at.ltd.adds;

import org.bukkit.entity.Player;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.gungame.ranks.GRankPerm;

public class Lg {

	public static void lgError(String s) {
		Thread th = Thread.currentThread();
		String threadname = th.getName();
		StackTraceElement stack = th.getStackTrace()[2];
		String classname = stack.getClassName();
		String calledmethodname = stack.getMethodName();
		String msg = "§7[§cLOGERROR §7->§f CALLED BY: THREAD:'" + threadname + "', METHODNAME: '" + calledmethodname + "', CLASS: '" + classname + "'§7]§r\n" + "§7[§cLOGERROR §7->§f MSG: '" + s + "§r'§7]\n";
		for (Player p : AsyncThreadWorkers.getOnlinePlayers()) {
			if (GRankPerm.isHighMember(p)) {
				p.sendMessage(msg);
			}
		}
		System.out.println(msg);
	}

	public static void lgMSG(String s) {
		String msg = "§7[§cLOG §7->§f MSG: '" + s + "§r'§7]\n";
	}

}
