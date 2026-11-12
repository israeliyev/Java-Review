package org.example.patterns;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class SynchronizedBufferDemo {

    static class BoundedBuffer<T> {
        private final Queue<T> queue = new ArrayDeque<>();
        private final int capacity;

        public BoundedBuffer(int capacity) {
            this.capacity = capacity;
        }

        public synchronized void put(T item) throws InterruptedException {
                while (queue.size() == capacity) {
                    System.out.println(Thread.currentThread().getName() + " Queue full, waiting...");
                    wait();
                }

                queue.add(item);
                System.out.println(Thread.currentThread().getName() + " Produced " + item);

                notifyAll();
        }

        public synchronized T take() throws InterruptedException {

                while (queue.isEmpty()) {
                    System.out.println(Thread.currentThread().getName() + " Queue is empty. Waiting...");
                    wait();
                }

                T item = queue.poll();
                System.out.println(Thread.currentThread().getName() + " Consumed " + item);

                notifyAll();

                return item;
        }

        public synchronized int size() {
                return queue.size();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(3);
        AtomicInteger produced =
                new AtomicInteger();

        AtomicInteger consumed =
                new AtomicInteger();

        Runnable producer = () -> {

            for (int i = 0;
                 i < 100;
                 i++) {

                try {
                    buffer.put(i);

                    produced.incrementAndGet();

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };

        Runnable consumer = () -> {

            for (int i = 0;
                 i < 100;
                 i++) {

                try {
                    Integer value = buffer.take();

                    // Real application would process value here.
                    if (value != null) {
                        consumed.incrementAndGet();
                    }

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };

        Thread producer1 =
                new Thread(producer, "producer-1");

        Thread producer2 =
                new Thread(producer, "producer-2");

        Thread consumer1 =
                new Thread(consumer, "consumer-1");

        Thread consumer2 =
                new Thread(consumer, "consumer-2");

        producer1.start();
        producer2.start();
        consumer1.start();
        consumer2.start();

        producer1.join();
        producer2.join();
        consumer1.join();
        consumer2.join();

        System.out.println(
                "Produced: " + produced.get()
        );

        System.out.println(
                "Consumed: " + consumed.get()
        );

        System.out.println(
                "Buffer size: " + buffer.size()
        );
    }
}