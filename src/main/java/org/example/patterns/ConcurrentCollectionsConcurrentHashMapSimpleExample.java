package org.example.patterns;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentCollectionsConcurrentHashMapSimpleExample {

    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<String , Integer> map = new ConcurrentHashMap<>();

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                map.put(Thread.currentThread().getName() + " " + i, i);
            }
        };

        Thread thread1 = new Thread(task, "worker-thread-1");
        Thread thread2 = new Thread(task, "worker-thread-2");

        thread1.start();
        thread2.start();


        thread1.join();
        thread2.join();

        System.out.println(map.size());

    }

}
