package com.bigdata.zookeeper;

public class ServerRunner {

	public static void main(String[] args) throws Exception {

		ServerSide serverSide = new ServerSide("server01", "192.168.1.100");
		ServerSide serverSide1 = new ServerSide("server02", "192.168.1.101");
		ServerSide serverSide2 = new ServerSide("server03", "192.168.1.102");
		serverSide.runServer();
		serverSide1.runServer();
		serverSide2.runServer();
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
