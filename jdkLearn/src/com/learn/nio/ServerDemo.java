package com.learn.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

public class ServerDemo {


    public static void main(String[] args) throws Exception {

        startNioServer(80);

    }


    static void startNioServer(int port) throws Exception {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(port));

        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);


        while (true) {
            if (selector.select() == 0) {
                continue;
            }
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();

                //事件为accept
                if (selectionKey.isAcceptable()) {
                    System.err.println("i am accept ");
                }
                //事件为 read
                if (selectionKey.isReadable()) {
                    System.err.println("i am read ");
                }

                //事件为writeable
                if (selectionKey.isValid() && selectionKey.isWritable()) {
                    System.err.println("i am write ");
                }

                keyIterator.remove();
            }

        }


    }


}
