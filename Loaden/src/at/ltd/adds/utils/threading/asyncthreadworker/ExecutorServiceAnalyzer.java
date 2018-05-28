package at.ltd.adds.utils.threading.asyncthreadworker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class ExecutorServiceAnalyzer implements Runnable {
	private final ThreadPoolExecutor threadPoolExecutor;
	private final int timeDiff;

	public ExecutorServiceAnalyzer(ExecutorService executorService, int timeDiff) {
		this.timeDiff = timeDiff;
		if (executorService instanceof ThreadPoolExecutor) {
			threadPoolExecutor = (ThreadPoolExecutor) executorService;
		} else {
			threadPoolExecutor = null;
			System.out.println("This executor doesn't support ThreadPoolExecutor ");
		}

	}

	@Override
	public void run() {
		if (threadPoolExecutor != null) {
			do {
				System.out.println("[LTD_THREADING]#### Thread Report:: Active:" + threadPoolExecutor.getActiveCount() + " Pool: " + threadPoolExecutor.getPoolSize() + " MaxPool: " + threadPoolExecutor.getMaximumPoolSize() + " ####");
				try {
					Thread.sleep(timeDiff);
				} catch (Exception e) {
				}
			} while (true);
		}

	}
}