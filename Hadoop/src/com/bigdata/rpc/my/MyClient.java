package com.bigdata.rpc.my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MyClient {

	public static void main(String[] args) throws IOException {

		Socket socket = new Socket();
		socket.connect(new InetSocketAddress("localhost", 1001));
		InputStream inputStream = socket.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = br.readLine();
		System.err.println(" server return " + line);

	}







}
