package com.learn.nio.reacotr;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Reactor implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    private Handler DEFAULT_HANDLER = new Handler() {
        @Override
        public void processRequest(Processor processor, ByteBuffer msg) {

        }
    };
    private Handler handler = DEFAULT_HANDLER;


    public Reactor(int port, int maxClients, Handler serverHandler) throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        this.handler = serverHandler;
        SelectionKey sk = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptor());
    }


    class Acceptor implements Runnable {

        @Override
        public void run() {
            SocketChannel sc;
            try {
                sc = serverSocketChannel.accept();
                if (sc != null) {
                    new Processor(Reactor.this, selector, sc);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void run() {


        while (!serverSocketChannel.socket().isClosed()) {
            try {
                selector.select(1000);
                Set<SelectionKey> keys;
                synchronized (this) {
                    keys = selector.selectedKeys();
                }

                Iterator itr = keys.iterator();
                while (itr.hasNext()) {
                    SelectionKey key = (SelectionKey) itr.next();
                    dispatch(key);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void dispatch(SelectionKey key) {
        Runnable attachment = (Runnable) key.attachment();
        attachment.run();
    }
}


