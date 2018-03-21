package com.learn.nio.reacotr;

import java.nio.ByteBuffer;

public interface Handler {
    void processRequest(Processor processor, ByteBuffer msg);
}
