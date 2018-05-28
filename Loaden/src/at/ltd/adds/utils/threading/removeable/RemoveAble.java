package at.ltd.adds.utils.threading.removeable;

import java.util.ArrayList;
import java.util.HashMap;

import at.ltd.adds.utils.threading.asyncthreadworker.AsyncThreadWorkers;

public class RemoveAble {

	private static HashMap<Runnable, Long> REMOVE_ABLES = new HashMap<>();

	public static void init() {
		AsyncThreadWorkers.submitSchedulerWorkMili(() -> {
			long curtime = System.currentTimeMillis();
			ArrayList<Runnable> remove = new ArrayList<>();
			synchronized (REMOVE_ABLES) {
				for (Runnable rai : REMOVE_ABLES.keySet()) {
					long time = REMOVE_ABLES.get(rai);
					if (time < curtime) {
						remove.add(rai);
					}
				}
				for (Runnable rai : remove) {
					REMOVE_ABLES.remove(rai);
				}
			}
			for (Runnable rai : remove) {
				try {
					rai.run();
				} catch (Exception e) {
					System.out.println("[LTD_REMOVEABLE] Catched exeption.");
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, 1, 1);
	}

	public static void addSec(Runnable run, long sec) {
		synchronized (REMOVE_ABLES) {
			REMOVE_ABLES.put(run, System.currentTimeMillis() + sec * 1000);
		}
	}

	public static void addMin(Runnable run, long min) {
		synchronized (REMOVE_ABLES) {
			REMOVE_ABLES.put(run, System.currentTimeMillis() + min * 60 * 1000);
		}
	}

	public static void addTick(Runnable run, long tick) {
		synchronized (REMOVE_ABLES) {
			REMOVE_ABLES.put(run, System.currentTimeMillis() + tick * 50);
		}
	}

	public static void addMili(Runnable run, long milis) {
		synchronized (REMOVE_ABLES) {
			REMOVE_ABLES.put(run, System.currentTimeMillis() + milis);
		}
	}

}
