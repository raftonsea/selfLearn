package com.learn.nio.mulreactor;

import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Processor {

    private static final ExecutorService service = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    private Selector selector;


    public Processor() throws Exception {
        this.selector = Selector.open();
        start();
    }

    private void start() {

        service.execute(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        if (selector.select(500) <= 0) {
                            continue;
                        }

                        Set<SelectionKey> keys = selector.selectedKeys();
                        Iterator<SelectionKey> itr = keys.iterator();
                        while (itr.hasNext()) {
                            SelectionKey key = itr.next();
                            keys.remove(key);

                            if (key.isReadable()) {
                                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                int count = socketChannel.read(byteBuffer);
                                if (count < 0) {
                                    socketChannel.close();
                                    key.cancel();
                                    System.err.println("  ......... read end .........");
                                    continue;
                                } else if (count == 0) {
                                    System.err.println(" *************** message size is zero *************");
                                } else {
                                    String result = new String(byteBuffer.array());
                                    System.err.println(" CLIENT SAY : " + result);
                                }
                            }


                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void addChannel(SocketChannel socketChannel) throws ClosedChannelException {
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    public void wakeup() {
        this.selector.wakeup();
    }
}
