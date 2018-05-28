package at.ltd.adds.utils.data;

import java.util.ArrayList;
import java.util.List;

import at.ltd.adds.utils.threading.removeable.RemoveAble;

/**
 * Cooldown on Objects. All methods are thread save. -> Uses RemoveAble
 * 
 * @author NyroForce
 * @since 03.01.2018
 * @version 1.0.1
 * @param <A>
 */
public class Cooldown<A> {

	private final List<A> LIST = new ArrayList<>();

	public boolean checkSec(A obj, int secs) {
		synchronized (LIST) {
			boolean contains = LIST.contains(obj);
			if (!contains) {
				LIST.add(obj);
				RemoveAble.addSec(() -> {
					LIST.remove(obj);
				}, secs);
			}
			return !contains;
		}
	}
	public boolean checkMin(A obj, int mins) {
		synchronized (LIST) {
			boolean contains = LIST.contains(obj);
			if (!contains) {
				LIST.add(obj);
				RemoveAble.addMin(() -> {
					LIST.remove(obj);
				}, mins);
			}
			return !contains;
		}
	}
	public boolean checkMili(A obj, int milis) {
		synchronized (LIST) {
			boolean contains = LIST.contains(obj);
			if (!contains) {
				LIST.add(obj);
				RemoveAble.addMili(() -> {
					LIST.remove(obj);
				}, milis);
			}
			return !contains;
		}
	}
}
