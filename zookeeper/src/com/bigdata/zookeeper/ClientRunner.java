package com.bigdata.zookeeper;

public class ClientRunner {
	public static void main(String[] args) throws Exception {
		ClientSide clientSide = new ClientSide();
		clientSide.clientRun();
		Thread.sleep(Integer.MAX_VALUE);
	}
}
