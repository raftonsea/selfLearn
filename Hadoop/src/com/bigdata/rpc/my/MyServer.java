package com.bigdata.rpc.my;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {

	public static void main(String[] args) throws IOException {

		ServerSocket serverSocket = new ServerSocket();
		serverSocket.bind(new InetSocketAddress("localhost", 1001));

		while (true) {

			Socket socket = serverSocket.accept();

			PrintWriter pr = new PrintWriter(socket.getOutputStream());

			pr.println("i am server ");

			pr.flush();

		}

	}
}
