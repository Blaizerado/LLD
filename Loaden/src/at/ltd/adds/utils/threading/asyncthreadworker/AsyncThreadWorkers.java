package at.ltd.adds.utils.threading.asyncthreadworker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import at.ltd.Main;
import at.ltd.adds.utils.threading.exception.ThreadExceprionMail;
import at.ltd.adds.utils.time.TimeCounter;

/**
 * All methods are thread save.
 * 
 * @author NyroForce
 * @since 03.12.2017
 * @version 1.0.1
 */
public class AsyncThreadWorkers {

	private static ThreadPoolExecutor executorPool;
	private static ScheduledThreadPoolExecutor executorPoolSche;
	private static int NEXT_THREAD_ID = 0;

	// DATA WITH LOCK
	private final static ArrayList<Player> ONLINE_PLAYERS = new ArrayList<>();
	private final static ArrayList<String> ONLINE_PLAYERS_UUID = new ArrayList<>();
	private final static ReentrantLock PLAYER_LOCK = new ReentrantLock();

	private final static HashMap<Player, Location> PLAYERS_LOCATION = new HashMap<>();
	private final static ReentrantLock PLAYERS_LOCATION_LOCK = new ReentrantLock();

	private final static HashMap<String, Location> ENITITY_LOCATIONS = new HashMap<>();
	private final static ReentrantLock ENITITY_LOCATIONS_LOCK = new ReentrantLock();

	private final static ArrayList<String> ENITITY_STATUS = new ArrayList<String>();
	private final static ReentrantLock ENITITY_STATUS_LOCK = new ReentrantLock();

	private final static ArrayList<Runnable> SYNC_WORK = new ArrayList<>();

	private final static at.ltd.adds.utils.time.TimeUnit TIME_SYNC_COUNTER = new at.ltd.adds.utils.time.TimeUnit(true,
			6000, "AsyncThreadWorkerSyncWork");

	private final static HashMap<Runnable, ScheduledFuture> RUNNABLES = new HashMap<>();

	private static final Map<SyncWork, Object> WORK_DATA = new HashMap<>();

	// CORE METHODS
	public static void init() {

		ThreadFactory threadFactory = new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("AsyncThreadPool-" + NEXT_THREAD_ID++);
				return t;
			}
		};

		executorPool = new ThreadPoolExecutor(15, 1000, 40, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),
				threadFactory);
		executorPoolSche = new ScheduledThreadPoolExecutor(5, threadFactory);
		executorPoolSche.setMaximumPoolSize(100);
		executorPoolSche.setKeepAliveTime(20, TimeUnit.SECONDS);

		executorPool.execute(new ExecutorServiceAnalyzer(executorPool, 120 * 60 * 1000));

		updateData();

		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), () -> {
			updateData();
			TimeCounter.start(TIME_SYNC_COUNTER);
			ArrayList<Runnable> work = null;
			synchronized (SYNC_WORK) {
				work = (ArrayList<Runnable>) SYNC_WORK.clone();
				SYNC_WORK.clear();
			}
			for (Runnable run : work) {
				try {
					run.run();
				} catch (Exception e) {
					ThreadExceprionMail.send(Thread.currentThread(), e);
					System.out.println("[ASYNC_THREAD_WORKERS]CATCHED EXEPTION ON A SYNC RUNNABLE");
					e.printStackTrace();
				}
			}

			TimeCounter.stop(TIME_SYNC_COUNTER);
		}, 0, 0);
	}

	private static void updateData() {
		try {
			PLAYER_LOCK.lock();
			ONLINE_PLAYERS.clear();
			ONLINE_PLAYERS_UUID.clear();
			for (Player p : Bukkit.getOnlinePlayers()) {
				ONLINE_PLAYERS.add(p);
				ONLINE_PLAYERS_UUID.add(p.getUniqueId().toString());
			}
		} finally {
			PLAYER_LOCK.unlock();
		}
		try {
			PLAYERS_LOCATION_LOCK.lock();
			PLAYERS_LOCATION.clear();
			for (Player p : getOnlinePlayers()) {
				PLAYERS_LOCATION.put(p, p.getLocation());
			}
		} finally {
			PLAYERS_LOCATION_LOCK.unlock();
		}

		try {
			ENITITY_LOCATIONS_LOCK.lock();
			ENITITY_STATUS_LOCK.lock();
			ENITITY_LOCATIONS.clear();
			ENITITY_STATUS.clear();
			for (World world : Bukkit.getWorlds()) {
				for (Entity entity : world.getEntities()) {
					String uuid = entity.getUniqueId().toString();
					ENITITY_LOCATIONS.put(uuid, entity.getLocation());
					ENITITY_STATUS.add(uuid);
				}
			}
		} finally {
			ENITITY_LOCATIONS_LOCK.unlock();
			ENITITY_STATUS_LOCK.unlock();
		}

	}

	public static void forceUpdate() throws IllegalAccessException {
		if (Bukkit.isPrimaryThread()) {
			updateData();
		} else {
			throw new IllegalAccessException("This Method is only Accessable on the Main thread.");
		}
	}

	// SYNC METHODS
	public static void submitSyncWork(final Runnable work) {
		synchronized (SYNC_WORK) {
			SYNC_WORK.add(work);
		}
	}

	/**
	 * Executes work in main thread and sends back the result to the caller thread.
	 * 
	 * @param work
	 * @return
	 */
	public static Object submitSyncDataWork(SyncWork work) {
		if (Main.isMainThread()) {
			throw new IllegalAccessError("ONLY ASYNC-CALLS ALLOWED");
		}
		submitSyncWork(() -> {
			Object data = work.work();
			synchronized (WORK_DATA) {
				WORK_DATA.put(work, data);
			}
		});

		while (true) {
			if (isDone(work)) {
				return getData(work);
			}
			try {
				Thread.sleep(0, 820);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static boolean isDone(SyncWork work) {
		synchronized (WORK_DATA) {
			return WORK_DATA.containsKey(work);
		}
	}

	private static Object getData(SyncWork work) {
		synchronized (WORK_DATA) {
			Object obj = WORK_DATA.get(work);
			WORK_DATA.remove(work);
			return obj;
		}
	}

	// WORK METHODS
	public static void submitWork(final Runnable run) {
		executorPool.execute(run);
	}

	public static void submitDelayedWorkMili(final Runnable run, long delay) {
		executorPoolSche.schedule(run, delay, TimeUnit.MILLISECONDS);
	}

	public static void submitDelayedWorkSec(final Runnable run, long delay) {
		executorPoolSche.schedule(run, delay, TimeUnit.SECONDS);
	}

	public static void submitDelayedWorkMin(final Runnable run, long delay) {
		executorPoolSche.schedule(run, delay, TimeUnit.MINUTES);
	}

	public static void submitDelayedWorkTick(final Runnable run, long delay) {
		ScheduledFuture<?> sche = executorPoolSche.schedule(run, delay * 50, TimeUnit.MILLISECONDS);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	public static void submitDelayedTickWork(final Runnable run, long time) {
		if (time == 0) {
			time = 1;
		}
		ScheduledFuture<?> sche = executorPoolSche.schedule(run, time * 50, TimeUnit.MILLISECONDS);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	public static void submitRepeatingTickWork(final Runnable run, int ticks) {
		BukkitTask bt = new BukkitRunnable() {

			@Override
			public void run() {
				run.run();
			}
		}.runTaskTimerAsynchronously(Main.getPlugin(), 0, 0);
		submitDelayedTickWork(new Runnable() {

			@Override
			public void run() {
				bt.cancel();
			}
		}, ticks);
	}

	// SCHEDULED WORK METODS
	public static void submitSchedulerWorkSec(final Runnable run, long initialDelay, long period) {
		ScheduledFuture<?> sche = executorPoolSche.scheduleAtFixedRate(run, initialDelay, period, TimeUnit.SECONDS);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	public static void submitSchedulerWorkMin(final Runnable run, long initialDelay, long period) {
		ScheduledFuture<?> sche = executorPoolSche.scheduleAtFixedRate(run, initialDelay, period, TimeUnit.MINUTES);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	public static void submitSchedulerWorkMili(final Runnable run, long initialDelay, long period) {
		ScheduledFuture<?> sche = executorPoolSche.scheduleAtFixedRate(run, initialDelay, period,
				TimeUnit.MILLISECONDS);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	public static void submitSchedulerWorkTick(final Runnable run, long initialDelay, long period) {
		if (initialDelay == 0) {
			initialDelay = 1;
		}
		if (period == 0) {
			period = 1;
		}
		ScheduledFuture<?> sche = executorPoolSche.scheduleAtFixedRate(run, initialDelay * 50, period * 50,
				TimeUnit.MILLISECONDS);
		synchronized (RUNNABLES) {
			RUNNABLES.put(run, sche);
		}
	}

	// DATA METHODS
	public static List<Player> getOnlinePlayers() {
		try {
			PLAYER_LOCK.lock();
			return (List<Player>) ONLINE_PLAYERS.clone();
		} finally {
			PLAYER_LOCK.unlock();
		}

	}

	public static boolean isPlayerOnline(final Player player) {
		try {
			PLAYER_LOCK.lock();
			return ONLINE_PLAYERS.contains(player);
		} finally {
			PLAYER_LOCK.unlock();
		}
	}

	public static boolean isPlayerOnline(final String uuid) {
		try {
			PLAYER_LOCK.lock();
			return ONLINE_PLAYERS_UUID.contains(uuid);
		} finally {
			PLAYER_LOCK.unlock();
		}
	}

	public static Location getPlayerLocation(final Player player) {
		try {
			PLAYERS_LOCATION_LOCK.lock();
			return PLAYERS_LOCATION.get(player);
		} finally {
			PLAYERS_LOCATION_LOCK.unlock();
		}

	}

	public static Location getEntityLocation(final Entity entity) {
		try {
			ENITITY_LOCATIONS_LOCK.lock();
			return ENITITY_LOCATIONS.get(entity.getUniqueId().toString());
		} finally {
			ENITITY_LOCATIONS_LOCK.unlock();
		}
	}

	public static Location getEntityLocation(final String uuid) {
		try {
			ENITITY_LOCATIONS_LOCK.lock();
			return ENITITY_LOCATIONS.get(uuid);
		} finally {
			ENITITY_LOCATIONS_LOCK.unlock();
		}
	}

	public static boolean getEntityStatus(final Entity entity) {
		try {
			ENITITY_STATUS_LOCK.lock();
			return ENITITY_STATUS.contains(entity.getUniqueId().toString());
		} finally {
			ENITITY_STATUS_LOCK.unlock();
		}
	}

	public static boolean getEntityStatus(final String uuid) {
		try {
			ENITITY_STATUS_LOCK.lock();
			return ENITITY_STATUS.contains(uuid);
		} finally {
			ENITITY_STATUS_LOCK.unlock();
		}
	}

	public static void cancelScheduler(Runnable run) {
		synchronized (RUNNABLES) {
			if (RUNNABLES.containsKey(run)) {
				RUNNABLES.get(run).cancel(false);
				RUNNABLES.remove(run);
			}
		}
	}

}
