package org.demo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SingleThreadExecutor {
    public static void main(String[] args) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> System.out.println(Thread.currentThread().getName() + " Hello World"));
        executor.execute(() -> System.out.println(Thread.currentThread().getName() + " Hello World"));
        System.exit(0);

    }
}
