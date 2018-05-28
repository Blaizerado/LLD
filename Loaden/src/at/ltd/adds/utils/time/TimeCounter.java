package at.ltd.adds.utils.time;

import java.util.List;

public class TimeCounter {

	public static Long start(TimeUnit tu) {
		tu.setSession(true);
		long cur = getTime();
		tu.setBegin(cur);
		return cur;
	}

	public static Long stop(TimeUnit tu) {
		long cur = getTime();
		long dur = cur - tu.getBegin();
		tu.setEnd(cur);
		if (tu.isCalculate()) {
			addDuration(tu, dur);
		}
		tu.setSession(false);
		return dur;
	}

	private static synchronized void addDuration(TimeUnit tu, long dur) {
		int size = tu.getSize();
		size--;
		List<Long> list = tu.getDurationList();
		if (list.size() > size) {
			list.remove(0);
		}
		list.add(dur);
		tu.setDurationList(list);
	}

	/**
	 * Nano
	 * 
	 * @param tu
	 * @return
	 */
	public static long calculateDurationMili(TimeUnit tu) {
		List<Long> list = tu.getDurationList();
		Long sum = (long) 0;
		if (!list.isEmpty()) {
			for (Long mark : list) {
				sum += mark;
			}
			return java.util.concurrent.TimeUnit.MILLISECONDS.convert(sum.longValue() / list.size(), java.util.concurrent.TimeUnit.NANOSECONDS);
		}
		return sum;
	}

	/**
	 * Mili
	 * 
	 * @param tu
	 * @return
	 */
	public static long calculateDuration(TimeUnit tu) {
		List<Long> list = tu.getDurationList();
		Long sum = (long) 0;
		if (!list.isEmpty()) {
			for (Long mark : list) {
				sum += mark;
			}
			return sum.longValue() / list.size();
		}
		return sum;
	}

	private static Long getTime() {
		return System.nanoTime();
	}

}
