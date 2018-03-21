package com.learn.thread;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo {

	private static class IncreaseDemo {
		volatile int i = 0;

		AtomicInteger ai = new AtomicInteger();

		void increase() {
			i = i + 1;
			// ai.getAndIncrement();
		}

		int getI() {
			return i;
			// return ai.get();
		}
	}

	public static void main(String[] args) {
		final IncreaseDemo increaseDemo = new IncreaseDemo();
		for (int i = 0; i < 1000; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					increaseDemo.increase();
					System.err.println(increaseDemo.getI());
				}
			}).start();
		}
	}
}
