package at.ltd.adds.utils.threading;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SingelThreadWorkingLoop {

	// DATA
	private final int LOOP_WAIT_BETWEEN_TICKS;
	private Thread WORKER_THREAD;

	private long TICK_START = 0;
	private long TICK_END = 0;
	private ArrayList<Integer> AVERAGE_TICK_WORK_TIME = new ArrayList<>();

	private long CURRENT_TICK_ID = 0;

	private final UUID LOOP_UUID;
	private boolean RUNNING_STATUS = true;

	private ArrayList<Runnable> WORK = new ArrayList<>();

	private HashMap<Runnable, Long> WORK_FUTURE = new HashMap<>();

	// DATA//

	// CONSTRUCTOR
	public SingelThreadWorkingLoop(int milisbetweenticks) {

		LOOP_WAIT_BETWEEN_TICKS = milisbetweenticks;
		LOOP_UUID = UUID.randomUUID();

		WORKER_THREAD = new Thread(() -> ticker());
		WORKER_THREAD.start();
		WORKER_THREAD.setName(LOOP_UUID + "_WORKER");

		Thread WORK_LOOP_WATCHER = new Thread(() -> {
			while (RUNNING_STATUS) {
				try {
					Thread.sleep(100);
					if (TICK_END != 0 && TICK_START != 0 && RUNNING_STATUS) {
						int duration = (int) (System.currentTimeMillis() - TICK_START);
						if (duration > (LOOP_WAIT_BETWEEN_TICKS * 10)) {
							System.out.println(
									"Thrad is stuck! ToDO: Stopping Thread, Deleting all Work, Creating new Thread!");

							System.err.println("--------------------------------------\nThread Stack:");
							for (StackTraceElement ste : WORKER_THREAD.getStackTrace()) {
								System.err.println(ste);
							}
							System.err.println("--------------------------------------");

							WORKER_THREAD.stop();

							WORKER_THREAD = new Thread(() -> ticker());
							WORKER_THREAD.start();
							WORKER_THREAD.setName(LOOP_UUID + "_WORKER");

						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		WORK_LOOP_WATCHER.setName(LOOP_UUID + "_WATCHER");
		WORK_LOOP_WATCHER.start();

	}

	private void ticker() {
		while (RUNNING_STATUS) {
			try {
				TICK_START = System.currentTimeMillis();
				ArrayList<Runnable> run = null;
				synchronized (this) {
					CURRENT_TICK_ID++;
					if (!WORK.isEmpty()) {
						run = (ArrayList<Runnable>) WORK.clone();
						WORK.clear();
					}
				}

				/* Tick Start Work */
				tickStart(CURRENT_TICK_ID);

				/* Running tick work */
				if (run != null) {
					run.forEach(work -> {
						try {
							work.run();
						} catch (Exception e) {
							e.printStackTrace();
						}
					});
				}

				/* Calculating and adding timings to the average list */
				TICK_END = System.currentTimeMillis();
				int duration = (int) (TICK_END - TICK_START);
				synchronized (AVERAGE_TICK_WORK_TIME) {
					addDuration(duration);
				}

				/* Warning MSG when tick durations is higher as LOOP_WAIT_BETWEEN_TICKS */
				if (duration > LOOP_WAIT_BETWEEN_TICKS) {
					System.out.println("Warning SingelThreadWorkingLoop is overloaded. UUID: " + LOOP_UUID.toString()
							+ " TickDuration: " + duration + " TickMilis: " + LOOP_WAIT_BETWEEN_TICKS);
					return;
				}
				/* Wait between the next tick */
				Thread.sleep(LOOP_WAIT_BETWEEN_TICKS - duration);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private ArrayList<Runnable> WORK_FUT = null;

	private void tickStart(long tickid) {

		synchronized (WORK_FUTURE) {
			WORK_FUTURE.keySet().forEach(work -> {
				long tick = WORK_FUTURE.get(work);
				if (tick <= tickid) {
					if (WORK_FUT == null) {
						WORK_FUT = new ArrayList<>();
					}
					WORK_FUT.add(work);
				}
			});
			if (WORK_FUT != null) {
				WORK_FUT.forEach(work -> WORK_FUTURE.remove(work));
			}
		}

		if (WORK_FUT != null) {
			WORK_FUT.forEach(work -> {
				try {
					work.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
		WORK_FUT = null;

	}

	public void submitWork(Runnable work) {
		synchronized (this) {
			WORK.add(work);
		}
	}

	private ArrayList<Runnable> CANCEL_TASKS = new ArrayList<>();

	public synchronized Runnable schedulerRepeating(Runnable work, int ticks) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				synchronized (CANCEL_TASKS) {
					if (CANCEL_TASKS.contains(this)) {
						CANCEL_TASKS.remove(this);
						return;
					}
				}
				try {
					work.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				schedulerDelay(ticks, this);
			}
		};
		schedulerDelay(ticks, run);

		return run;
	}

	public synchronized Runnable schedulerRepeating(Runnable work, int ticks, int initdelay) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				synchronized (CANCEL_TASKS) {
					if (CANCEL_TASKS.contains(this)) {
						CANCEL_TASKS.remove(this);
						return;
					}
				}
				try {
					work.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
				schedulerDelay(ticks, this);
			}
		};
		schedulerDelay(initdelay, run);
		return run;
	}

	public void cancelTask(Runnable run) {
		synchronized (CANCEL_TASKS) {
			CANCEL_TASKS.add(run);
		}
	}

	public void schedulerDelay(int ticks, Runnable work) {
		synchronized (WORK_FUTURE) {
			WORK_FUTURE.put(work, CURRENT_TICK_ID + ticks);
		}
	}

	public Thread getThread() {
		return WORKER_THREAD;
	}

	public void stop() {
		RUNNING_STATUS = false;
	}

	public int getAvarageTickWorkTime() {
		synchronized (AVERAGE_TICK_WORK_TIME) {
			int sum = (int) 0;
			if (!AVERAGE_TICK_WORK_TIME.isEmpty()) {
				for (int mark : AVERAGE_TICK_WORK_TIME) {
					sum += mark;
				}
				return sum / AVERAGE_TICK_WORK_TIME.size();
			}
			return sum;
		}
	}

	private void addDuration(int dur) {
		int size = 100;
		size--;
		List<Integer> list = AVERAGE_TICK_WORK_TIME;
		if (list.size() > size) {
			list.remove(0);
		}
		list.add(dur);
	}

}
