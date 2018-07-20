package org.demo.obj;

import java.util.HashMap;
import java.util.Map;

public class MyObjectLockUsingExistingObject {
    private int valueSync = 0;
    private int value = 0;
    private Map<String, String> map = new HashMap<String, String>();

    public void incrementSynchronized() {
        System.out.println(Thread.currentThread().getName() + " Start incrementSynchronized without sync");
        synchronized (map) {
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
        synchronized (map) {
            System.out.println(Thread.currentThread().getName() + " Start synchronized sleep");
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " finish synchronized sleep");
        }
    }

    public void put(String key, String value) {
        System.out.println(Thread.currentThread().getName() + " Start put");
        map.put(key, value);
        System.out.println(Thread.currentThread().getName() + " finish put");
    }

    public int getValueSynchronized() {
        return valueSync;
    }

    public int getValue() {
        return value;
    }
}
