package org.example.patterns;

import java.util.concurrent.atomic.AtomicInteger;

public class Atomics {

    static class Inventory {

        private final AtomicInteger quantity =
                new AtomicInteger(1);

        private final AtomicInteger casFailures =
                new AtomicInteger();

        boolean purchase() {

            while (true) {

                int current = quantity.get();

                if (current <= 0) {
                    return false;
                }

                int next = current - 1;

                if (quantity.compareAndSet(current, next)) {
                    return true;
                }

                casFailures.incrementAndGet();
            }
        }

        int getQuantity() {
            return quantity.get();
        }

        int getCasFailures() {
            return casFailures.get();
        }
    }

    private static final int ATTEMPTS = 10_000;

    public static void main(String[] args)
            throws InterruptedException {

        int invariantFailures = 0;
        int totalCasFailures = 0;

        for (int attempt = 0;
             attempt < ATTEMPTS;
             attempt++) {

            Inventory inventory = new Inventory();

            AtomicInteger successfulPurchases =
                    new AtomicInteger();

            Thread t1 = new Thread(() -> {
                if (inventory.purchase()) {
                    successfulPurchases.incrementAndGet();
                }
            });

            Thread t2 = new Thread(() -> {
                if (inventory.purchase()) {
                    successfulPurchases.incrementAndGet();
                }
            });

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            int successes =
                    successfulPurchases.get();

            int remaining =
                    inventory.getQuantity();

            if (successes > 1 || remaining < 0) {
                invariantFailures++;
            }

            totalCasFailures +=
                    inventory.getCasFailures();
        }

        System.out.println(
                "Invariant failures: "
                        + invariantFailures
        );

        System.out.println(
                "CAS retries: "
                        + totalCasFailures
        );
    }
}