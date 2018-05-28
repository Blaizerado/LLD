package at.ltd.command.NormalCommands;

import java.util.ArrayList;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.utils.threading.AsyncWorker;
import at.ltd.adds.utils.threading.SyncWorker;
import at.ltd.adds.utils.threading.Worker;
import at.ltd.adds.utils.time.TimeCounter;
import at.ltd.adds.utils.time.TimeUnit;

public class CommandStatus implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		p.sendMessage(Main.getPrefix() + "Listing times in ms/nano:");
		for (TimeUnit tu : TimeUnit.getUnits()) {
			if (tu.isCalculate()) {
				long cal = TimeCounter.calculateDuration(tu);
				long durationInMs = java.util.concurrent.TimeUnit.MILLISECONDS.convert(cal, java.util.concurrent.TimeUnit.NANOSECONDS);
				p.sendMessage(Main.getPrefix() + "§3Time Unit: §c" + tu.getName() + "§3,§2 \n      Time: §c" + durationInMs + "/" + cal + "§3,§2 Durlist:§c " + tu.getDurationList().size() + "/" + tu.getSize());
			}
		}
		p.sendMessage("");
		p.sendMessage(Main.getPrefix() + "AsyncThreads (Worker, AsyncWorker): " + getWorkers());
		p.sendMessage(Main.getPrefix() + "Current amount of SyncWorkers waiting: " + SyncWorker.getWorkers().size());
	}

	public static Integer getWorkers() {
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Integer i = 0;
		for (Thread t : threadSet) {
			if (Worker.isWorker(t) || AsyncWorker.isAsyncWorker(t)) {
				i++;
			}
		}
		return i;
	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
	}

}
