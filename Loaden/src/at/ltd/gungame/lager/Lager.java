package at.ltd.gungame.lager;

import org.bukkit.entity.Player;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class Lager {

	public static void init() {

	}

	public static void lagerTP(Player p) {
		AsyncThreadWorkers.submitWork(() -> {
			int x = LagerUtils.getX(p);
			if (x == 1) {
				LagerUnit lu = LagerUtils.registerLager(p);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				AsyncThreadWorkers.submitSyncWork(() -> {
					lu.tp();
				});
				return;
			} else {
				LagerUnit lu = new LagerUnit(p);
				AsyncThreadWorkers.submitSyncWork(() -> {
					lu.tp();
				});
			}

		});

	}

}
