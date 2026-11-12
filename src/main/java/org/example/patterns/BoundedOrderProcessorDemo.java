package org.example.patterns;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BoundedOrderProcessorDemo {

    private static final AtomicInteger accepted =
            new AtomicInteger();

    private static final AtomicInteger completed =
            new AtomicInteger();

    private static final AtomicInteger rejected =
            new AtomicInteger();

    private static final AtomicInteger active =
            new AtomicInteger();

    private static final AtomicInteger maxActive =
            new AtomicInteger();

    public static void main(String[] args) {


        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(
                        2,
                        4,
                        30,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<>(3),
                        new ThreadPoolExecutor.AbortPolicy()
                );

        for (int taskId = 1;
             taskId <= 20;
             taskId++) {

            final int id = taskId;

            try {

                executor.execute(() ->
                        processOrder(id));

                accepted.incrementAndGet();

                System.out.println(
                        "Accepted task " + id
                );

            } catch (RejectedExecutionException e) {

                rejected.incrementAndGet();

                System.out.println(
                        "Rejected task " + id
                );
            }
        }

        executor.shutdown();

        try {

            if (!executor.awaitTermination(
                    10,
                    TimeUnit.SECONDS)) {

                var neverStarted =
                        executor.shutdownNow();

                System.out.println(
                        "Never-started tasks: "
                                + neverStarted.size()
                );

                if (!executor.awaitTermination(
                        5,
                        TimeUnit.SECONDS)) {

                    System.out.println(
                            "Executor still did not terminate"
                    );
                }
            }

        } catch (InterruptedException e) {

            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println();
        System.out.println(
                "Accepted: " + accepted.get()
        );
        System.out.println(
                "Completed: " + completed.get()
        );
        System.out.println(
                "Rejected: " + rejected.get()
        );
        System.out.println(
                "Max active workers: "
                        + maxActive.get()
        );
    }

    private static void processOrder(int taskId) {

        int current =
                active.incrementAndGet();

        maxActive.accumulateAndGet(
                current,
                Math::max
        );

        String thread =
                Thread.currentThread().getName();

        System.out.println(
                "Task " + taskId
                        + " started by "
                        + thread
        );

        try {

            Thread.sleep(1000);

            System.out.println(
                    "Task " + taskId
                            + " completed"
            );

            completed.incrementAndGet();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            System.out.println(
                    "Task " + taskId
                            + " interrupted"
            );

        } finally {

            active.decrementAndGet();
        }
    }
}