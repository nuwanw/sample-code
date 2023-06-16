package org.demo.response;

import org.demo.ResponseWriter;

import java.io.PrintWriter;

public class DelayResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        long delay = 10000;
        System.out.println("Sleeping for " + delay + " ms");
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new SimpleJsonResponse().write(out);
//        new MultiTCPSegmentResponse().write(out);
        try {
            Thread.sleep(180000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
