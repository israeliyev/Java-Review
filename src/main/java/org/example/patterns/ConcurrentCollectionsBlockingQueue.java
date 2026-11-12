package org.example.patterns;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class ConcurrentCollectionsBlockingQueue {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(3);
//        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(3);
//        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(3);

        Thread producerWorker = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.put(i);
//                    queue.offer(i);
                    queue.offer(i, 500, TimeUnit.MILLISECONDS);
                    System.out.println(i + " produced");
                } catch (InterruptedException e) {

                    Thread.currentThread()
                            .interrupt();

                    return;
                }

            }
        });

        Thread consumerWorker = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.take();
//                    queue.poll();
//                    queue.poll(500, TimeUnit.MILLISECONDS);
                    System.out.println(i + " taked");
                } catch (InterruptedException e) {
                    Thread.currentThread()
                            .interrupt();
                    return;
                }
            }
        });

        producerWorker.start();
        consumerWorker.start();

        producerWorker.join();
        consumerWorker.join();

        System.out.println(queue.size());

    }
}
