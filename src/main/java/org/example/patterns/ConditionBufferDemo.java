package org.example.patterns;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BoundedBuffer<T> {
    Queue<T> queue = new LinkedList<>();
    private final int capacity;


    private Lock lock = new ReentrantLock();

    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public void put(T item) throws InterruptedException {
        lock.lockInterruptibly();
        try {

            while (queue.size() == capacity) {
                System.out.println(Thread.currentThread().getName() + " Queue full, waiting...");
                notFull.await();
            }

            queue.add(item);
            System.out.println(Thread.currentThread().getName() + " Produced " + item);

            notEmpty.signal();

        } finally {
            lock.unlock();
        }
    }

    public void take() throws InterruptedException {
        lock.lockInterruptibly();
        try {

            while (queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " Queue is empty. Waiting...");
                notEmpty.await();
            }

            T item = queue.poll();
            System.out.println(Thread.currentThread().getName() + " Consumed " + item);

            notFull.signal();

        } finally {
            lock.unlock();
        }
    }

    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

public class ConditionBufferDemo {

    public static void main(String[] args) throws InterruptedException {

        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(100);

        Runnable producer = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    buffer.put(i);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };


        Runnable consumer = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    buffer.take();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };


        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);
        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.join();

        System.out.println("Buffer size: " + buffer.size());
    }
}