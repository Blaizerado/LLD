package at.ltd.adds.utils.threading;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SyncWorker {
	private static ArrayList<SyncWorker> done = new ArrayList<>();
	private static Long begin;
	private static Boolean isRunning = false;
	private static Boolean isWatcher = false;
	private static Integer ContinuousWorkerID = 0;
	private static SyncWorker current;
	private static Worker worker;
	private int workID;
	private Runnable work;
	private static ReentrantLock lock = new ReentrantLock();
	private static ArrayList<SyncWorker> workers = new ArrayList<>();
	private ArrayList<Runnable> hocks = new ArrayList<>();
	private Long ProcessingTime;
	public SyncWorker(Runnable work, Boolean runImmediately) {
		if (!isRunning) {
			genThread();
		}
		ContinuousWorkerID++;
		workID = ContinuousWorkerID.intValue();
		this.work = work;
		if (runImmediately) {
			lock.lock();
			workers.add(this);
			lock.unlock();
		}
	}

	public void addHock(Runnable hock) {
		hocks.add(hock);
	}

	public void runWork() {
		lock.lock();
		workers.add(this);
		lock.unlock();
	}

	private static void genThread() {
		isRunning = true;
		worker = new Worker(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					lock.lock();
					ArrayList<SyncWorker> workersList = (ArrayList<SyncWorker>) workers.clone();
					lock.unlock();

					for (SyncWorker sw : workersList) {
						begin = System.currentTimeMillis();
						done.add(sw);
						current = sw;
						Long pr = System.currentTimeMillis();
						sw.work.run();
						sw.ProcessingTime = System.currentTimeMillis() - pr;
						for (Runnable r : sw.hocks) {
							r.run();
						}
						current = null;
						begin = null;
					}
					for (SyncWorker sw : done) {
						lock.lock();
						SyncWorker.workers.remove(sw);
						lock.unlock();
					}
					workersList.clear();
					done.clear();
				}
			}
		});
		worker.start();
		worker.setName("SyncWorker");
		if (!isWatcher) {
			new Worker(new Runnable() {

				@Override
				public void run() {
					while (true) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (SyncWorker.begin != null) {
							try {
								Long time = System.currentTimeMillis() - SyncWorker.begin;
								if (time > 3000) {
									System.out.println("{SyncWorker} Work took over 3 sec! Skipping work. ID: " + SyncWorker.current.getWorkID());
									begin = null;
									SyncWorker.worker.stop();
									for (SyncWorker sw : done) {
										workers.remove(sw);
									}
									genThread();
								}
							} catch (Exception e) {
								System.out.println("{SyncWorker} Thread Exception! Supposed onLine: 99 This isn't a really big problem.");
								e.printStackTrace();
							}

						}
					}
				}
			}).start();
			new Worker(new Runnable() {

				@Override
				public void run() {
					if (SyncWorker.lock.getQueueLength() > 5) {
						SyncWorker.lock.unlock();
						System.out.println("{SyncWorker} SyncWorker unlocking ReentrantLock!");
					}
				}
			}).start();
		}

	}

	public static ArrayList<SyncWorker> getDone() {
		return done;
	}


	public static Long getBegin() {
		return begin;
	}


	public static Boolean getIsRunning() {
		return isRunning;
	}

	public static Boolean getIsWatcher() {
		return isWatcher;
	}


	public static Integer getContinuousWorkerID() {
		return ContinuousWorkerID;
	}


	public static SyncWorker getCurrent() {
		return current;
	}


	public static Worker getWorker() {
		return worker;
	}


	public int getWorkID() {
		return workID;
	}


	public Runnable getWork() {
		return work;
	}


	public static ReentrantLock getLock() {
		return lock;
	}


	public static ArrayList<SyncWorker> getWorkers() {
		return workers;
	}


	public ArrayList<Runnable> getHocks() {
		return hocks;
	}

	public void setHocks(ArrayList<Runnable> hocks) {
		this.hocks = hocks;
	}
	

	public Long getProcessingTime() {
		return ProcessingTime;
	}

	public static Boolean isSyncWorker() {
		if (Thread.currentThread().getName().equals("SyncWorker")) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isSyncWorker(Thread thread) {
		if (thread.getName().equals("SyncWorker")) {
			return true;
		} else {
			return false;
		}
	}

}
