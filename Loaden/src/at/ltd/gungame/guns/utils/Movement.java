package at.ltd.gungame.guns.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import at.ltd.Main;
import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;
import at.ltd.events.custom.EventPlayerMoveBlockEventAsync;

public class Movement implements Listener {

	private final static HashMap<Player, Long> RUNNING_PLAYER = new HashMap<>();
	private final static ReentrantLock lock = new ReentrantLock();
	public static enum MovementType {
		STAYING, SPRINTING, SNEAKING, WALKING
	};

	public static void init() {
		Main.registerListener(new Movement());

		AsyncThreadWorkers.submitSchedulerWorkTick(() -> {
			ArrayList<Player> ar = new ArrayList<>();
			try {
				lock.lock();
				for (Player p : RUNNING_PLAYER.keySet()) {
					long time = RUNNING_PLAYER.get(p);
					if (System.currentTimeMillis() - time > 200) {
						ar.add(p);
					}
				}

				for (Player p : ar) {
					RUNNING_PLAYER.remove(p);
				}
			} finally {
				lock.unlock();
			}
		}, 10, 3);
	}

	@EventHandler
	public void on(EventPlayerMoveBlockEventAsync e) {
		Player p = e.getPlayer();
		try {
			lock.lock();
			if (RUNNING_PLAYER.containsKey(p)) {
				RUNNING_PLAYER.remove(p);
			}
			if (!RUNNING_PLAYER.containsKey(p)) {
				RUNNING_PLAYER.put(p, System.currentTimeMillis());
			}
		} finally {
			lock.unlock();
		}

	}

	public static MovementType getMovement(Player p) {
		if (p.isSneaking()) {
			try {
				lock.lock();
				if (!RUNNING_PLAYER.containsKey(p)) {
					return MovementType.SNEAKING;
				}
			} finally {
				lock.unlock();
			}

		}

		if (p.isSprinting()) {
			return MovementType.SPRINTING;
		}

		try {
			lock.lock();
			if (RUNNING_PLAYER.containsKey(p)) {
				return MovementType.WALKING;
			} else {
				return MovementType.STAYING;
			}
		} finally {
			lock.unlock();
		}

	}

	public static boolean isStatic(Player p) {
		MovementType mt = getMovement(p);
		if (mt == MovementType.SNEAKING) {
			return true;
		}
		if (mt == MovementType.STAYING) {
			return true;
		}
		return false;

	}

}
