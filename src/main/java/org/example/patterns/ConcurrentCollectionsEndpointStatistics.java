package org.example.patterns;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class ConcurrentCollectionsEndpointStatistics {

    static class EndpointStatistics {

        private final ConcurrentHashMap<String, LongAdder> counters =
                new ConcurrentHashMap<>();

        private final CopyOnWriteArrayList<String> listeners =
                new CopyOnWriteArrayList<>();

        void recordRequest(String endpoint) {

            counters
                    .computeIfAbsent(
                            endpoint,
                            key -> new LongAdder()
                    )
                    .increment();
        }

        Map<String, Long> snapshot() {

            Map<String, Long> current =
                    counters.entrySet()
                            .stream()
                            .collect(
                                    Collectors.toMap(
                                            Map.Entry::getKey,
                                            entry ->
                                                    entry.getValue().sum()
                                    )
                            );

            return Map.copyOf(current);
        }

        void addListener(String listener) {
            listeners.add(listener);
        }

        CopyOnWriteArrayList<String> listeners() {
            return listeners;
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        EndpointStatistics statistics =
                new EndpointStatistics();

        String[] endpoints = {
                "/orders",
                "/payments",
                "/users"
        };

        Thread[] workers =
                new Thread[6];

        for (int workerId = 0;
             workerId < workers.length;
             workerId++) {

            final int id = workerId;

            workers[workerId] =
                    new Thread(() -> {

                        for (int i = 0;
                             i < 100_000;
                             i++) {

                            String endpoint =
                                    endpoints[
                                            (i + id)
                                                    % endpoints.length
                                            ];

                            statistics.recordRequest(
                                    endpoint
                            );
                        }

                    }, "worker-" + workerId);
        }

        // Start ALL workers first.
        for (Thread worker : workers) {
            worker.start();
        }

        // Only then wait for ALL workers.
        for (Thread worker : workers) {
            worker.join();
        }

        Map<String, Long> snapshot =
                statistics.snapshot();

        long total =
                snapshot.values()
                        .stream()
                        .mapToLong(Long::longValue)
                        .sum();

        System.out.println(snapshot);
        System.out.println("Total: " + total);

        /*
         * CopyOnWriteArrayList demonstration
         */

        statistics.addListener("listener-A");
        statistics.addListener("listener-B");

        CountDownLatch iteratorCreated =
                new CountDownLatch(1);

        Thread reader =
                new Thread(() -> {

                    Iterator<String> iterator =
                            statistics
                                    .listeners()
                                    .iterator();

                    iteratorCreated.countDown();

                    sleep(200);

                    while (iterator.hasNext()) {

                        System.out.println(
                                "Reader saw: "
                                        + iterator.next()
                        );

                        sleep(200);
                    }
                });

        Thread writer =
                new Thread(() -> {

                    try {

                        iteratorCreated.await();

                        statistics.addListener(
                                "listener-C"
                        );

                        System.out.println(
                                "Writer added listener-C"
                        );

                    } catch (InterruptedException e) {

                        Thread.currentThread()
                                .interrupt();
                    }
                });

        reader.start();
        writer.start();

        reader.join();
        writer.join();

        System.out.println(
                "Final listeners: "
                        + statistics.listeners()
        );
    }

    private static void sleep(long millis) {

        try {

            Thread.sleep(millis);

        } catch (InterruptedException e) {

            Thread.currentThread()
                    .interrupt();
        }
    }
}