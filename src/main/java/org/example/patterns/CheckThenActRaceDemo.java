package org.example.patterns;

import java.util.concurrent.atomic.AtomicInteger;

class Inventory {

    private int quantity = 1;

    boolean purchase() {
        // CHECK: Inspecting state without synchronization
        if (quantity > 0) {

            // Widen the race window so context switches occur between check and act
            Thread.yield();

            // ACT: Modifying state based on stale check
            quantity--;
            return true;
        }

        return false;
    }

    int getQuantity() {
        return quantity;
    }
}

public class CheckThenActRaceDemo {

    private static final int ATTEMPTS = 10_000;

    public static void main(String[] args) throws InterruptedException {
        int raceConditionOccurrences = 0;

        for (int attempt = 0; attempt < ATTEMPTS; attempt++) {
            Inventory inventory = new Inventory();
            AtomicInteger successfulPurchases = new AtomicInteger(0);

            // Thread 1 trying to buy 1 item
            Thread t1 = new Thread(() -> {
                if (inventory.purchase()) {
                    successfulPurchases.incrementAndGet();
                }
            });

            // Thread 2 trying to buy 1 item
            Thread t2 = new Thread(() -> {
                if (inventory.purchase()) {
                    successfulPurchases.incrementAndGet();
                }
            });

            t1.start();
            t2.start();

            // Wait for both threads to finish before inspecting the result
            t1.join();
            t2.join();

            // DETECT INVALID RESULT:
            // Since initial quantity was 1, we should NEVER have 2 purchases,
            // nor should the final quantity drop to -1.
            int finalQuantity = inventory.getQuantity();
            int totalPurchases = successfulPurchases.get();

            if (totalPurchases > 1 || finalQuantity < 0) {
                raceConditionOccurrences++;
            }
        }

        System.out.println("=== Race Condition Analysis ===");
        System.out.println("Total Experiments Executed : " + ATTEMPTS);
        System.out.println("Check-Then-Act Failures    : " + raceConditionOccurrences);
        System.out.printf("Failure Rate               : %.2f%%\n",
                (raceConditionOccurrences / (double) ATTEMPTS) * 100);
    }
}