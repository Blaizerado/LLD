package at.ltd.lobby;

import java.util.List;

import at.ltd.adds.Cf;
import at.ltd.adds.utils.config.Config;
import at.ltd.adds.utils.config.ConfigAble;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class LobbyBroadcast {

	@ConfigAble(key = "intervallSec", value = "30")
	public static int intervall;

	@ConfigAble(key = "", value = "<center>&cBugs gefunden?<newline><center>&a https://www.flargo.net/bugreport/<newline><center>&aund verdiene bis zu &c10.000 Coins!;&a<center>Du hast Probleme mit deiner Performance?<newline><center>Lese unsere Hilfe für die Verbesserung durch<newline><center>&ahttps://www.flargo.net/performance/>>STRING")
	public static List<String> MESSAGES;

	public static int pos = 0;

	public static void init() {
		Config.loadMyClass("LobbyBroadcast", LobbyBroadcast.class).setReloadAble(true).setReloadHandler(() -> {
			Config.loadMyClass("LobbyBroadcast", LobbyBroadcast.class);
			pos = 0;
		});
		AsyncThreadWorkers.submitSchedulerWorkSec(() -> {
			pos++;
			if (MESSAGES.size() == pos) {
				pos = 0;
			}
			String text = MESSAGES.get(pos);
			LobbyUtils.broadcast(Cf.rs(text));
		}, 10, intervall);
	}

}
