package com.learn.nio.mulreactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class SimpleReactorClient {


    public static void main(String[] args) throws IOException {

        InetSocketAddress address = new InetSocketAddress("localhost", 1024);
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.socket().connect(address);
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);


    }

}
