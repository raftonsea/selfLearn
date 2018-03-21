package com.learn.threadpool;

import java.util.concurrent.*;

/**
 * 2017/12/22 11:07.
 */
public class CallableAndFutureDemo {

	public static void main(String[] args) throws ExecutionException, InterruptedException {

		ExecutorService pool = Executors.newSingleThreadExecutor();
		Future<String> future = pool.submit(new Callable<String>() {
			@Override
			public String call() throws Exception {
				Thread.sleep(2000);
				return "ok";
			}
		});

		System.out.println("等待结果");
		System.out.println("获取到结果：" + future.get());


		ExecutorService fix = Executors.newFixedThreadPool(1);




	}

}
