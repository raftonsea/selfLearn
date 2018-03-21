package com.learn.nio.reacotr;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.LinkedBlockingQueue;

public class Processor implements Runnable {

    Reactor reactor;
    private SocketChannel socketChannel;
    private SelectionKey sk;
    private final ByteBuffer lenBuffer = ByteBuffer.allocate(4);
    ByteBuffer inputBuffer = lenBuffer;
    private ByteBuffer outputDirectBuffer = ByteBuffer.allocate(1024 * 64);

    private LinkedBlockingQueue<ByteBuffer> outputQueue = new LinkedBlockingQueue<ByteBuffer>();

    public Processor(Reactor reactor, Selector selector, SocketChannel channel) throws IOException {
        this.reactor = reactor;
        this.socketChannel = channel;
        socketChannel.configureBlocking(false);
        sk = socketChannel.register(selector, SelectionKey.OP_READ);
        sk.attach(this);
        selector.wakeup();
    }


    @Override
    public void run() {
        if (socketChannel.isOpen() && sk.isValid()) {
            if (sk.isReadable()) {
            } else if (sk.isWritable()) {
            }
        } else {
        }

    }
}
