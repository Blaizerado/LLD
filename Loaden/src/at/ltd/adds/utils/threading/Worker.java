package at.ltd.adds.utils.threading;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread {
	public static ArrayList<Worker> workers = new ArrayList<>();
	public static ReentrantLock lock = new ReentrantLock();
	public static Integer continuousID = 0;
	public Runnable run;
	public int ID;
	public Worker(Runnable run) {
		this.run = run;
		lock.lock();
		continuousID++;
		this.setName("Worker-" + continuousID);
		ID = continuousID;
		workers.add(this);
		lock.unlock();

	}

	@Override
	public void run() {
		if (run != null) {
			run.run();
		}
		lock.lock();
		workers.remove(this);
		lock.unlock();
		Thread.currentThread().stop();
	}

	public static Boolean isWorker() {
		if (Thread.currentThread().getName().contains("Worker-")) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isWorker(Thread thread) {
		if (thread.getName().contains("Worker-")) {
			return true;
		} else {
			return false;
		}
	}

	public static Integer getID(Thread thread) {
		if (isWorker(thread)) {
			return Integer.valueOf(thread.getName().replace("Worker-", ""));
		} else {
			return null;
		}
	}

}
