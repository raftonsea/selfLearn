package juclearn;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;

/**
 * @author: raftonsea
 * @createTime:2018/3/6 10:35
 * @description:
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier = new CyclicBarrier(N, new Runnable() {
            @Override
            public void run() {
                System.err.println("所有线程执行完毕，启动合并线程。");
            }
        });
        for (int i = 0; i < N; i++)
            new Writer(barrier).start();
    }

    static class Writer extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Writer(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
            try {
                Thread.sleep(new Random().nextInt(1000));      //以睡眠来模拟写入数据操作
                System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
                cyclicBarrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println("所有线程写入完毕，继续处理其他任务...");
        }
    }
}

