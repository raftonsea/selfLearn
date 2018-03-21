package com.bigdata.zookeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ClientSide {

	String connectionStr = "hadoop01:2181,hadoop02:2181,hadoop03:2181";
	int sessionTimeout = 30000;
	ZooKeeper zooKeeper = null;

	volatile List serverList = new ArrayList();

	String watchPath = "/servers";

	// 连接到zookeeper集群，当连接成功，或者节点有数据变化时候，进入回调
	void connectServer() throws IOException {
		zooKeeper = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.err.println("event invoke ");
				try {
					getServerList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 获取可用的服务端节点
	void getServerList() throws KeeperException, InterruptedException {
		List<String> children = zooKeeper.getChildren(watchPath, true);
		List servers = new ArrayList();
		for (String child : children) {
			byte[] nodeData = zooKeeper.getData(watchPath + "/" + child, false, null);
			servers.add(new String(nodeData));
		}
		serverList = servers;
		System.err.println(serverList);
		// clientWork();
	}

	// 模拟连接成功
	void clientWork() {
		for (int i = 0; i < serverList.size(); i++) {
			String server = (String) serverList.get(i);
			// System.err.println("SERVER IP IS :" + server);
		}
		if (serverList == null || serverList.isEmpty()) {
			System.err.println(" SERVER LIST IS EMPTY.......");
		} else {
			System.err.println("CONNECT TO SERVER: " + serverList.get(0));
		}
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void clientRun() throws Exception {
		connectServer();
		getServerList();
	}

}
