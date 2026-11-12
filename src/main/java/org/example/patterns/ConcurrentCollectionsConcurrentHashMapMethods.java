package org.example.patterns;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;

public class ConcurrentCollectionsConcurrentHashMapMethods {

    private static final ConcurrentHashMap<String, ArrayList<String>> orderanditems = new ConcurrentHashMap<>();

    static class UserCache {
        public UserCache(String name) {
            System.out.println("Tenant " + name + " is created");
        }
    }

    public static void main(String[] args) throws InterruptedException {

        // putIfAbsent example
        ConcurrentHashMap<Long, String> sessions = new ConcurrentHashMap<>();
        Runnable task = () -> {
            sessions.put(463L, "session-1");
            String response = sessions.putIfAbsent(464L, "session-2");
            System.out.println(response);
        };
        Thread worker1 = new Thread(task);
        worker1.start();
        worker1.join();

        System.out.println();

        ConcurrentHashMap<String, UserCache> tenants = new ConcurrentHashMap<>();
        Runnable task2 = () -> {
            UserCache tenant = tenants.computeIfAbsent("tenant-1", UserCache::new);
            UserCache tenan2 = tenants.computeIfAbsent("tenant-2", UserCache::new);

            System.out.println(Thread.currentThread().getName() + " got " + tenant);
            System.out.println(Thread.currentThread().getName() + " got " + tenan2);
        };

        Thread worker2 = new Thread(task2);
        Thread worker3 = new Thread(task2);

        worker2.start();
        worker3.start();

        worker2.join();
        worker3.join();

        System.out.println(tenants.size());

        System.out.println();

        Runnable task4 = () -> {
            orderanditems.computeIfAbsent("order-1", orderId -> new ArrayList<>()).add("sa");
            orderanditems.computeIfAbsent("order-1", orderId -> new ArrayList<>()).add("se");
        };

        Thread worker4 = new Thread(task4);

        worker4.start();
        worker4.join();

        System.out.println(orderanditems.size());
        System.out.println(orderanditems.get("order-1"));

        System.out.println();

        ConcurrentHashMap<String, LongAdder> endpointCounter = new ConcurrentHashMap<>();

        Runnable task5 = () -> {
            endpointCounter.computeIfAbsent("/orders", endpoint -> new LongAdder()).increment();
            endpointCounter.computeIfAbsent("/billings", endpoint -> new LongAdder()).increment();
            endpointCounter.computeIfAbsent("/items", endpoint -> new LongAdder()).increment();
        };

        Thread worker5 = new Thread(task5);
        worker5.start();
        worker5.join();

        System.out.println();

        CopyOnWriteArrayList<String> passengers = new CopyOnWriteArrayList<>();
        passengers.add("passenger-1");
        passengers.add("passenger-2");

        Thread worker6 = new Thread(() -> {
            for (String passenger : passengers) {
                System.out.println(passenger);
            }
            sleep(200);
        });

        Thread worker7 = new Thread(() -> {
            sleep(100);
            passengers.add("passenger-3");
            System.out.println("writed passsenger 3");
        });

        worker6.start();
        worker7.start();

        worker6.join();
        worker7.join();

        System.out.println(passengers);

    }

    static void sleep(long millis) {

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
