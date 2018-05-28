package at.ltd.adds.utils.data;

import at.ltd.adds.utils.data.Data.DataType;

public class Data2<A, B> {

	public static final DataType TYPE = DataType.DATA_2;
	
	private volatile A first;
	private volatile B second;

	public Data2() {}

	public Data2(A first, B second) {
		this.first = first;
		this.second = second;
	}

	public A getFirst() {
		synchronized (this) {
			return first;
		}
	}
	public void setFirst(A first) {
		synchronized (this) {
			this.first = first;
		}
	}
	public B getSecond() {
		synchronized (this) {
			return second;
		}
	}
	public void setSecond(B second) {
		synchronized (this) {
			this.second = second;
		}
	}

}
