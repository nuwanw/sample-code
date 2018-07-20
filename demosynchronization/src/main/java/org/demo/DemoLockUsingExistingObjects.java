package org.demo;

import org.demo.obj.MyObjectLockObjects;
import org.demo.obj.MyObjectLockUsingExistingObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DemoLockUsingExistingObjects {

    private static final Set<Integer> synchronizedSet = Collections.synchronizedSet(new HashSet<Integer>());
    private static final Set<Integer> unSynchronizedSet = Collections.synchronizedSet(new HashSet<Integer>());


    public static void main(String[] args) throws InterruptedException {
        final MyObjectLockUsingExistingObject myObject = new MyObjectLockUsingExistingObject();
        ExecutorService service = Executors.newFixedThreadPool(10);
        System.out.println("-------Synchronized--------");
        for (int i = 0; i < 10; i++) {
            service.execute(new Runnable() {
                public void run() {
                    myObject.incrementSynchronized();
                    int value = myObject.getValueSynchronized();
                    synchronizedSet.add(value);
                }
            });
        }

        service.awaitTermination(2000, TimeUnit.MILLISECONDS);
        System.out.println("-------Synchronized--------");
        System.out.println("-------UnSynchronized--------");
        for (int i = 0; i < 10; i++) {
            service.execute(new Runnable() {
                public void run() {
                    myObject.increment();
                    int value = myObject.getValue();
                    unSynchronizedSet.add(value);
                }
            });
        }
        service.awaitTermination(2000, TimeUnit.MILLISECONDS);
        System.out.println("-------UnSynchronized--------");

        System.out.println("-------Synchronized and UnSynchronized--------");
        //only synchronized method will block.
        service.execute(new Runnable() {
            public void run() {
                myObject.sleepSynchronized(3000);
            }
        });

        service.execute(new Runnable() {
            public void run() {
                myObject.increment();
            }
        });

        service.awaitTermination(4000, TimeUnit.MILLISECONDS);
        System.out.println("-------Synchronized and UnSynchronized--------");
        System.out.println("-------Two Synchronized --------");

        //two synchronized method call block one after other.
        service.execute(new Runnable() {
            public void run() {
                myObject.sleepSynchronized(3000);
            }
        });

        //calling method which use the object used to lock the above method.
        service.execute(new Runnable() {
            public void run() {
                myObject.put("Key1", "Value1");
            }
        });

        service.execute(new Runnable() {
            public void run() {
                myObject.incrementSynchronized();
            }
        });
        service.awaitTermination(4000, TimeUnit.MILLISECONDS);
        System.out.println("-------Two Synchronized --------");

        service.shutdown();
        while (!service.isTerminated()) {
        }
        System.out.println("Synchronized Set " + (synchronizedSet.size() == 10) + " " + synchronizedSet);
        System.out.println("UnSynchronized Set " + (unSynchronizedSet.size() == 10) + " " + unSynchronizedSet);

    }

}
