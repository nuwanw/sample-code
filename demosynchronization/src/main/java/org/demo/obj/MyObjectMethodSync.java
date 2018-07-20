package org.demo.obj;

public class MyObjectMethodSync {
    private int valueSync = 0;
    private int value = 0;

    public synchronized void incrementSynchronized() {
        System.out.println(Thread.currentThread().getName() + " Start incrementSynchronized");
        valueSync++;
        System.out.println(Thread.currentThread().getName() + " finish incrementSynchronized");
    }

    public void increment() {
        System.out.println(Thread.currentThread().getName() + " Start increment");
        value++;
        System.out.println(Thread.currentThread().getName() + " finish increment");
    }

    public synchronized void sleepSynchronized(long time) {
        System.out.println(Thread.currentThread().getName() + " Start sleepSynchronized");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finish sleepSynchronized");
    }

    public int getValueSynchronized() {
        return valueSync;
    }

    public int getValue() {
        return value;
    }
}
