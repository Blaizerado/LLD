package at.ltd.command;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.threading.AsyncAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.adds.utils.time.TimeUnit;
import at.ltd.command.NormalCommands.CommandExecuter;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandManager implements Listener {

	public static HashMap<String, CommandExecuter> normal = new HashMap<>();
	public static TimeUnit CommandExecutor = new TimeUnit(true, 50, "CommandExecutor (Sync)");

	public static void register(CommandExecuter cdx, String... exe) {
		try {
			cdx.onRegister(Main.getPlugin());
			for (String s : exe) {
				normal.put(s.toLowerCase(), cdx);
				System.out.println("§4I have registred a command called: " + s.toLowerCase());
			}

		} catch (Exception e) {
			System.out.println("Can't register command: " + exe);
			e.printStackTrace();
		}

	}
	public HashMap<String, Long> cooldowns = new HashMap<String, Long>();

	public static CommandExecuter getCommandExecuter(String command) {
		return	normal.get(command.toLowerCase());
	}
	
	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent e) {
		TimeCounter.start(CommandExecutor);
		Player p = e.getPlayer();
		try {
			if (e.getMessage().startsWith("/")) {
				String args[] = e.getMessage().split(" ");
				args[0] = args[0].replaceFirst("/", "").toLowerCase();
				if (normal.containsKey(args[0])) {
					e.setCancelled(true);
					int cooldownTime = 2;
					if (cooldowns.containsKey(p.getName())) {
						long secondsLeft = ((cooldowns.get(p.getName()) / 1000) + cooldownTime) - (System.currentTimeMillis() / 1000);
						if (secondsLeft > 0) {
							Cf.rsS(Cf.COMMAND_COOLDOWN, p, "[SECLEFT]", secondsLeft);
							return;
						} else {
							cooldowns.remove(p.getName());
						}
					}
					if (!GRankPerm.isHighMember(p)) {
						cooldowns.put(p.getName(), System.currentTimeMillis());
					}
					CommandExecuter cx = normal.get(args[0]);
					if (CommandExecutor.isInSession()) {
						TimeCounter.stop(CommandExecutor);
					}
					try {
						if (isAsync(cx)) {
							AsyncThreadWorkers.submitWork(() -> {
								try {
									cx.run(p, new ArrayList<String>(Arrays.asList(args)));
								} catch (Exception e2) {
									System.out.println("Error on Async Command");
									e2.printStackTrace();
								}
							});

						} else {
							cx.run(p, new ArrayList<String>(Arrays.asList(args)));
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			if (CommandExecutor.isInSession()) {
				TimeCounter.stop(CommandExecutor);
			}
		} finally {
			if (CommandExecutor.isInSession()) {
				TimeCounter.stop(CommandExecutor);
			}
		}

	}
	@EventHandler
	public void on(PlayerQuitEvent e) {
		if (cooldowns.containsKey(e.getPlayer().getName())) {
			cooldowns.remove(e.getPlayer().getName());
		}
	}

	public static String getLastArgs(ArrayList<String> args, int startfrom) {
		int i = 0;
		String reason = "";
		for (String s : args) {
			i++;
			if (i > startfrom) {
				reason = reason + s + " ";
			}
		}
		reason = reason.substring(0, reason.length() - 1);
		return reason;
	}

	private static final HashMap<CommandExecuter, Boolean> ASYNC_LIST = new HashMap<>();

	public static boolean isAsync(CommandExecuter cmd) {
		synchronized (ASYNC_LIST) {
			if (ASYNC_LIST.containsKey(cmd)) {
				return ASYNC_LIST.get(cmd);
			}
			try {
				Method method = cmd.getClass().getMethod("run", Player.class, ArrayList.class);
				boolean isasync = method.isAnnotationPresent(AsyncAble.class);
				ASYNC_LIST.put(cmd, isasync);
				return isasync;
			} catch (Exception e) {
				System.out.println("[LTD] Catched!");
				e.printStackTrace();
			}
			return false;
		}

	}

	@EventHandler
	public void onCommandPreprocessBlock(PlayerCommandPreprocessEvent e) {
		if (e.getMessage().startsWith("/")) {
			if (e.getMessage().contains("bukkit:")) {
				e.getPlayer().sendMessage(Main.getPrefix() + "Blocked.");
				e.setCancelled(true);
			}
		}
	}

}
