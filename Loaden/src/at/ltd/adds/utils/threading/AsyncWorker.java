package at.ltd.adds.utils.threading;

import java.util.ArrayList;

public class AsyncWorker {
	private Runnable work;
	private ArrayList<Runnable> hocks = new ArrayList<>();
	private static Integer ContinuousWorkerID = 0;
	private Integer id;
	private Long ProcessingTime;
	private Thread workerThread;
	public AsyncWorker(Runnable work, Boolean runImmediately) {
		this.work = work;
		ContinuousWorkerID++;
		id = ContinuousWorkerID.intValue();
		if (runImmediately) {
			runWork();
		}
	}

	public void addHock(Runnable hock) {
		hocks.add(hock);
	}

	public void runWork() {
		Worker w = new Worker(new Runnable() {

			@Override
			public void run() {
				Long pr = System.currentTimeMillis();
				work.run();
				ProcessingTime = System.currentTimeMillis() - pr;
				for (Runnable hock : hocks) {
					hock.run();
				}
			}
		});
		w.setName("AsyncWorker-" + id);
		w.start();
		workerThread = w;
	}

	public static Boolean isAsyncWorker() {
		if (Thread.currentThread().getName().contains("AsyncWorker-")) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean isAsyncWorker(Thread thread) {
		if (thread.getName().contains("AsyncWorker-")) {
			return true;
		} else {
			return false;
		}
	}

	public static Integer getID(Thread thread) {
		if (isAsyncWorker()) {
			return Integer.valueOf(thread.getName().replace("AsyncWorker-", ""));
		} else {
			return null;
		}
	}

	public Runnable getWork() {
		return work;
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

	public Integer getId() {
		return id;
	}

	public Thread getWorkerThread() {
		return workerThread;
	}

}
