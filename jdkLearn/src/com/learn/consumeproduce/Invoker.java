package com.learn.consumeproduce;

/**
 * 2017/11/27 11:02.
 */
public class Invoker {

	DataWareHouse dataWareHouse = new DataWareHouse();

	public static void main(String[] args) {

		Invoker invoker = new Invoker();

		for (int i = 0; i < 100; i++) {
			Producer producer = new Producer(invoker.dataWareHouse, i);
			new Thread(producer).start();
		}

		for (int i = 0; i < 100; i++) {
			Consumer consumer = new Consumer(invoker.dataWareHouse);
			new Thread(consumer).start();
		}

	}

}
