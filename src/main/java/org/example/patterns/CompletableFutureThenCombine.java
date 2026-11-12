package org.example.patterns;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureThenCombine {
    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        CompletableFuture<Integer> productPrice = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            return 100;
        });


        CompletableFuture<Integer> shipping = CompletableFuture.supplyAsync(() -> {
            sleep(1200);
            return 20;
        });

        CompletableFuture<Integer> total = productPrice.thenCombine(shipping, Integer::sum);

        System.out.println("Total: " + total.join());

        System.out.println(
                "Elapsed: "
                        + (System.currentTimeMillis() - start)
                        + " ms"
        );
    }

    private static void sleep(long milliSeconds) {
        try {
            Thread.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
