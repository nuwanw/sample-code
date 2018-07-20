package org.demo.obj;

public class MyObjectBlockSync {
    private int valueSync = 0;
    private int value = 0;

    public void incrementSynchronized() {
        System.out.println(Thread.currentThread().getName() + " Start incrementSynchronized without sync");
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " Start incrementSynchronized");
            valueSync++;
            System.out.println(Thread.currentThread().getName() + " finish incrementSynchronized");
        }
        System.out.println(Thread.currentThread().getName() +  " finish incrementSynchronized without sync");
    }

    public void increment() {
        System.out.println(Thread.currentThread().getName() + " Start increment");
        value++;
        System.out.println(Thread.currentThread().getName() + " finish increment");
    }

    public void sleepSynchronized(long time) {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " Start sleepSynchronized");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finish sleepSynchronized");
        }
    }

    public int getValueSynchronized() {
        return valueSync;
    }

    public int getValue() {
        return value;
    }
}
