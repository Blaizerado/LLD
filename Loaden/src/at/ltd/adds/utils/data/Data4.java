package at.ltd.adds.utils.data;

import at.ltd.adds.utils.data.Data.DataType;

public class Data4<A, B, C, D> {

	public static final DataType TYPE = DataType.DATA_4;
	
	private volatile A first;
	private volatile B second;
	private volatile C third;
	private volatile D fourth;

	public Data4() {}

	public Data4(A first, B second, C third, D fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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
	public D getFourth() {
		synchronized (this) {
			return fourth;
		}

	}
	public void setFourth(D fourth) {
		synchronized (this) {
			this.fourth = fourth;
		}
	}

}