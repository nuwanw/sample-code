package org.demo;

import java.util.concurrent.atomic.AtomicInteger;

public class MyObject {
    private static AtomicInteger atomicInteger = new AtomicInteger(1);
    private int id;

    public MyObject() {
        this.id = atomicInteger.getAndIncrement();
    }

    public void print() {
        System.out.println("Object ID " + this.id);
    }

}
