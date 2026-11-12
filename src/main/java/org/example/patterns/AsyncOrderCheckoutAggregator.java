package org.example.patterns;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncOrderCheckoutAggregator {

    record CheckoutResult(
            int productPrice,
            int shipping,
            int total,
            boolean customerValid
    ) {}

    record PriceShipping(
            int productPrice,
            int shipping
    ) {}

    public static void main(String[] args) {

        ExecutorService ioExecutor =
                Executors.newFixedThreadPool(3);

        try {

            CompletableFuture<Integer> product =
                    CompletableFuture
                            .supplyAsync(() -> {
                                sleep(3000);
                                return 100;
                            }, ioExecutor)
                            .orTimeout(
                                    1500,
                                    TimeUnit.MILLISECONDS
                            )
                            .exceptionally(error -> {

                                Throwable cause =
                                        unwrap(error);

                                if (cause instanceof TimeoutException) {
                                    System.out.println(
                                            "Product service timed out"
                                    );

                                    return 70;
                                }

                                throw new CompletionException(cause);
                            });

            CompletableFuture<Integer> shipping =
                    CompletableFuture.supplyAsync(() -> {
                        sleep(1000);
                        return 20;
                    }, ioExecutor);

            CompletableFuture<Boolean> validation =
                    CompletableFuture.supplyAsync(() -> {
                        sleep(500);
                        return true;
                    }, ioExecutor);

            CompletableFuture<PriceShipping> prices =
                    product.thenCombine(
                            shipping,
                            PriceShipping::new
                    );

            CompletableFuture<CheckoutResult> checkout =
                    prices.thenCombine(
                            validation,
                            (ps, isValid) -> {

                                int total =
                                        ps.productPrice()
                                                + ps.shipping();

                                return new CheckoutResult(
                                        ps.productPrice(),
                                        ps.shipping(),
                                        total,
                                        isValid
                                );
                            });

            CheckoutResult result =
                    checkout.join();

            System.out.println(
                    "Result: " + result
            );

        } finally {

            ioExecutor.shutdown();
        }
    }

    private static Throwable unwrap(Throwable error) {

        if (error instanceof CompletionException
                && error.getCause() != null) {

            return error.getCause();
        }

        return error;
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}