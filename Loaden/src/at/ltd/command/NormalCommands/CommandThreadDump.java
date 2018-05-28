package at.ltd.command.NormalCommands;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import at.ltd.Main;
import at.ltd.adds.Cf;
import at.ltd.adds.utils.net.web.WebData;
import at.ltd.gungame.ranks.GRankPerm;

public class CommandThreadDump implements CommandExecuter {

	@Override
	public void run(Player p, ArrayList<String> args) throws Exception {
		if (!GRankPerm.isHighMember(p)) {
			Cf.rsS(Cf.NO_RIGHTS, p);
			return;
		}
		String rdm = UUID.randomUUID().toString();
		p.sendMessage(Main.getPrefix() + "Creating Dump... UUID: " + rdm);
		ArrayList<String> INFO = new ArrayList<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		String strDate = sdf.format(now);

		INFO.add("#################################################<br>");
		INFO.add("Thread Dump<br>");
		INFO.add("Thread count: " + Thread.getAllStackTraces().size() + "<br>");
		INFO.add("UNIX TIME STAMP: " + System.currentTimeMillis() + "<br>");
		INFO.add("DUMP DATE: " + strDate + "<br>");
		INFO.add("DUMP UUID: " + rdm + "<br>");
		INFO.add("#################################################<br> <br>");
		for (Thread th : Thread.getAllStackTraces().keySet()) {
			INFO.add("---------------[" + th.getName() + "]---------------<br>");
			for (StackTraceElement ste : th.getStackTrace()) {
				INFO.add(ste.toString() + "<br>");
			}
			INFO.add("---------------[END]---------------<br>");
		}

		String info = "";
		for (String st : INFO) {
			info += st;
		}
		String link = WebData.addData(info);
		p.sendMessage(Main.getPrefix() + "Your dump: " + link);

	}

	@Override
	public void onRegister(Plugin plugin) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				String rdm = UUID.randomUUID().toString();
				ArrayList<String> INFO = new ArrayList<>();

				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date now = new Date();
				String strDate = sdf.format(now);

				INFO.add("#################################################" + System.lineSeparator());
				INFO.add("Thread Dump" + System.lineSeparator());
				INFO.add("Thread count: " + Thread.getAllStackTraces().size() + System.lineSeparator());
				INFO.add("UNIX TIME STAMP: " + System.currentTimeMillis() + System.lineSeparator());
				INFO.add("DUMP DATE: " + strDate + System.lineSeparator());
				INFO.add("DUMP UUID: " + rdm + System.lineSeparator());
				INFO.add("#################################################" + System.lineSeparator() + " " + System.lineSeparator());
				for (Thread th : Thread.getAllStackTraces().keySet()) {
					INFO.add("---------------[" + th.getName() + "]---------------" + System.lineSeparator());
					for (StackTraceElement ste : th.getStackTrace()) {
						INFO.add(ste.toString() + System.lineSeparator());
					}
					INFO.add("---------------[END]---------------" + System.lineSeparator());
				}

				String info = "";
				for (String st : INFO) {
					info += st;
				}
				try {
					FileUtils.writeStringToFile(new File("plugins/LTD/ThreadDumps/" + strDate + ".txt"), info);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

}
