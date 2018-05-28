package at.ltd.adds.utils.time;

import java.util.ArrayList;
import java.util.List;

public class TimeUnit {

	private static ArrayList<TimeUnit> units = new ArrayList<>();
	private Long begin;
	private Long end;
	private List<Long> DurationList;
	private boolean inSession = false;
	private boolean calculate = false;
	private int size;
	private String name;

	public TimeUnit(boolean calculate, Integer size, String name) {
		if (calculate == true) {
			this.DurationList = new ArrayList<Long>();
			this.size = size;
			this.calculate = calculate;
		}
		if (name == null) {
			throw new IllegalArgumentException("Name is null!");
		}
		units.add(this);
		this.name = name;
	}

	public Long getBegin() {
		return begin;
	}

	public void setBegin(Long begin) {
		this.begin = begin;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}


	public boolean isInSession() {
		return inSession;
	}

	public void setSession(Boolean inSession) {
		this.inSession = inSession;
	}

	public List<Long> getDurationList() {
		return DurationList;
	}

	public void setDurationList(List<Long> durationList) {
		DurationList = durationList;
	}

	public boolean isCalculate() {
		return calculate;
	}

	public int getSize() {
		return size;
	}

	public String getName() {
		return name;
	}

	public static ArrayList<TimeUnit> getUnits() {
		return units;
	}

	public static void removeUnit(TimeUnit tu) {
		if (units.contains(tu)) {
			units.remove(tu);
		} else {
			throw new NullPointerException("Remove Unit, Unit not found!");
		}
	}

	public String toString() {
		return "Name: " + name + " Size: " + size + " isSession: " + inSession + " Calculate: " + calculate +  ", List: " + DurationList;
	}

}
