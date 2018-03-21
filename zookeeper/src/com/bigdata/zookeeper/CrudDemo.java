package com.bigdata.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;


public class CrudDemo {

	static String connectionStr = "hadoop01:2181,hadoop02:2181,hadoop03:2181";
	static int sessionTimeout = 3000;
	static ZooKeeper zooKeeper = null;

	static {
		try {
			zooKeeper = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					System.err.println("envent is : " + event.getType());
					try {
						zooKeeper.getChildren("/", true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		CrudDemo crudDemo = new CrudDemo();
		// crudDemo.create();
//		crudDemo.set();
		// crudDemo.delete();
		// crudDemo.getChildren();
//		crudDemo.get();
		Thread.sleep(Long.MAX_VALUE);
	}

	// 增
	public void create() throws KeeperException, InterruptedException {
		String result = zooKeeper.create("/server", "i am server".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.PERSISTENT);
		System.err.println("create result : " + result);
	}

	// 删
	public void delete() throws KeeperException, InterruptedException {
		zooKeeper.delete("/server", -1);
		Stat exists = zooKeeper.exists("/server", false);
		System.err.println((exists == null) ? "已删除，不存在" : "未删除，存在。");
	}

	// 改
	public void set() throws KeeperException, InterruptedException {
		zooKeeper.setData("/server", "hello world".getBytes(), -1);
	}

	// 查
	public void get() throws KeeperException, InterruptedException {
		byte[] data = zooKeeper.getData("/server", false, null);
		System.err.println(new String(data));
	}

	// 获取子节点
	public void getChildren() throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren("/", true);
		for (String child : children) {
			System.err.println(child);
		}
	}

}
