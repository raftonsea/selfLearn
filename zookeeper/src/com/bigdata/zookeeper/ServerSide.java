package com.bigdata.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.*;

public class ServerSide {

	private String serverDesc;
	private String ip;

	public ServerSide(String serverDesc, String ip) {
		this.serverDesc = serverDesc;
		this.ip = ip;
	}

	String connectionStr = "hadoop01:2181,hadoop02:2181,hadoop03:2181";
	int sessionTimeout = 3000;
	ZooKeeper zooKeeper = null;

	String watchPath = "/servers";

	// 连接集群
	void connectToCluster() throws IOException {
		zooKeeper = new ZooKeeper(connectionStr, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {

			}
		});
	}

	// 注册信息到集群的指定节点，node类型为ephemeral
	void register2Cluster() throws KeeperException, InterruptedException {
		zooKeeper.create(watchPath + "/" + this.serverDesc, ip.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL);
	}

	// 模拟业务运行
	public void businessRun() {
		System.err.println(this.serverDesc + " start running , ip is :" + ip);
	}

	// 运行
	public void runServer() throws Exception {
		this.connectToCluster();
		this.register2Cluster();
		businessRun();
	}
}
