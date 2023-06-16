package org.demo.response;

import org.demo.ResponseWriter;

import java.io.PrintWriter;

public class TimeoutResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        long time = 10000;
        System.out.println("Sleeping for " + time + " ms");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
