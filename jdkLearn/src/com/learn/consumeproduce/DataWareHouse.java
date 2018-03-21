package com.learn.consumeproduce;

import java.util.ArrayList;
import java.util.List;

/**
 * 2017/11/27 11:39.
 */
public class DataWareHouse {

	List data = new ArrayList();

	public synchronized void produce(int i) throws InterruptedException {
//		System.err.println("当前生产线程：" + Thread.currentThread().getName() + "：当前长度：" + data.size());
		if (data.size() >= 10) {
//			System.err.println("队列长度到达10 , 阻塞生产者");
			wait();
		} else {
			notifyAll();
			data.add(i);
		}
	}

	public synchronized void consume() throws InterruptedException {
//		System.err.println("当前消费线程：" + Thread.currentThread().getName() + "：当前长度：" + data.size());
		if (data.size() <= 0) {
//			System.err.println("阻塞消费者。");
			wait();
		} else {
			notifyAll();
			Object o = data.remove(0);
			System.err.println(" consume " + o);
		}
	}

}
