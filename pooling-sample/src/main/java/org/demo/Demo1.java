package org.demo;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class Demo1 {
    public static void main(String[] args) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);
        config.setTestOnBorrow(true);
//        config.setMinEvictableIdleTimeMillis(100);
//        config.setTimeBetweenEvictionRunsMillis(100);
        final ObjectPool<MyObject> pool = new GenericObjectPool<MyObject>(new MyPooledObjectFactory(), config);

        for(int i = 0; i < 30; i++) {

            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        MyObject myObject = pool.borrowObject();
                        myObject.print();
                        pool.returnObject(myObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.close();
    }
}
