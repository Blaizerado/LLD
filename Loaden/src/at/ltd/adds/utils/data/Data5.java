package at.ltd.adds.utils.data;

import at.ltd.adds.utils.data.Data.DataType;

public class Data5<A, B, C, D, E> {

	public static final DataType TYPE = DataType.DATA_5;

	private volatile A first;
	private volatile B second;
	private volatile C third;
	private volatile D fourth;
	private volatile E fifth;

	public Data5() {}

	public Data5(A first, B second, C third, D fourth, E fifth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
		this.fifth = fifth;
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
	public E getFifth() {
		synchronized (this) {
			return fifth;
		}
	}
	public void setFifth(E fifth) {
		synchronized (this) {
			this.fifth = fifth;
		}
	}

}
