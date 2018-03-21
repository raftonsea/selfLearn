package com.bigdata.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientSide1 {

	static String connectionStr = "hadoop01:2181,hadoop02:2181,hadoop03:2181";
	static int sessionTimeout = 3000;
	static ZooKeeper zooKeeper = null;

	public void ttt() throws IOException {

		zooKeeper = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.err.println("envent is : " + event.getType());
			}
		});

	}

	// 连接到zookeeper集群，当连接成功，或者节点有数据变化时候，进入回调
	void connectServer() throws IOException {
		zooKeeper = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.err.println("event invoke " + event.getPath());
			}
		});
	}

	public static void main(String[] args) throws Exception {
		ClientSide1 crudDemo = new ClientSide1();
		crudDemo.connectServer();
		Thread.sleep(Long.MAX_VALUE);
	}

}
