package com.learn.consumeproduce;

import java.util.Random;

/**
 * 2017/11/27 11:02.
 */
public class Consumer implements Runnable {

	DataWareHouse dataWareHouse;

	public Consumer(DataWareHouse dataWareHouse) {
		this.dataWareHouse = dataWareHouse;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(new Random().nextInt(1000));
			dataWareHouse.consume();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
