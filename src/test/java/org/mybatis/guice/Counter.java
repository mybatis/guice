package org.mybatis.guice;

public class Counter {
	
	private int count;
	
	public void increment() {
		count++;
	}
	public void reset() {
		count = 0;
	}
	public int getCount() {
		return count;
	}
}
