package at.ltd.command.NormalCommands;

import java.util.ArrayList;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class CommandSound implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {

		new AsyncWorker(new Runnable() {

			@Override
			public void run() {
				String[] STRING_ARRAY = {"NOTE_SNARE_DRUM", "ORB_PICKUP", "","","","","","","","",};
				for (Sound s : Sound.values()) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					AsyncThreadWorkers.submitSyncWork(new Runnable() {

						@Override
						public void run() {
							if (!p.isOnline()) {
								return;
							}
							p.sendMessage(Main.getPrefix() + "Sound: " + s.toString());
							p.playSound(p.getLocation(), Sound.valueOf(s.toString()), 1, 1);
						}
					});
				}

			}
		}, true);
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
