package com.learn.consumeproduce;

import java.util.Random;

/**
 * 2017/11/27 11:02.
 */
public class Producer implements Runnable {

	DataWareHouse dataWareHouse;

	int p = 0;

	public Producer(DataWareHouse dataWareHouse, int p) {
		this.dataWareHouse = dataWareHouse;
		this.p = p;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(new Random().nextInt(1000));
			dataWareHouse.produce(p);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
