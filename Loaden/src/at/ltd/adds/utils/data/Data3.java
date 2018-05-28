package at.ltd.adds.utils.data;

import at.ltd.adds.utils.data.Data.DataType;

public class Data3<A, B, C> {

	public static final DataType TYPE = DataType.DATA_3;
	
	private volatile A first;
	private volatile B second;
	private volatile C third;

	public Data3() {}

	public Data3(A first, B second, C third) {
		this.first = first;
		this.second = second;
		this.third = third;
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

	public C getThird() {
		synchronized (this) {
			return third;
		}

	}
	public void setThird(C third) {
		synchronized (this) {
			this.third = third;
		}

	}

}
