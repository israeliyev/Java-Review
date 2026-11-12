package org.example.patterns;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureAllOf {

    public static void main(String[] args) {

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supplier runs on: " + Thread.currentThread().getName());
            sleep(1000);
            return 47;
        });

        CompletableFuture<Integer> doubled = future.thenApply(value -> {
            System.out.println("Transforming on: " + Thread.currentThread().getName());
            sleep(1000);
            return value * 23;
        });

        CompletableFuture<Integer> plus = doubled.thenApply(value -> value + 23);


        CompletableFuture<Void> allOffs = CompletableFuture.allOf(future, doubled, plus);

        allOffs.join();

        Integer futureJoin = future.join();
        Integer doubledJoin = doubled.join();
        Integer plusJoin = plus.join();

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
