package org.example.patterns;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureExceptionallyHandleWhenComplete {
    public static void main(String[] args) {
        CompletableFuture<Integer> price = CompletableFuture.<Integer>supplyAsync(() -> {
                    sleep(1000);
                    throw new IllegalStateException("this istate is eillegal");
                })
                .exceptionally(error -> {
                    if (error != null) {
                        System.out.println(error);
                    }

                    return 99;
                }).thenApply(value -> value + 10);

        CompletableFuture<Integer> anotherfuture = CompletableFuture.<Integer>supplyAsync(() -> {
            sleep(1000);
            throw new IllegalStateException("this istate is eillegal");
        }).handle((value , error) -> {

            if(error != null) {
                System.out.printf(String.valueOf(error));
            }

            return value;
        });

        CompletableFuture<Integer> anotherfuture2 = CompletableFuture.<Integer>supplyAsync(() -> {
            sleep(1000);
            throw new IllegalStateException("this istate is eillegal");
        }).whenComplete((value , error) -> {

            if(error != null) {
                System.out.printf(String.valueOf(error));
            } else {
                System.out.println(value);
            }
        });

        System.out.println(price.join());
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
