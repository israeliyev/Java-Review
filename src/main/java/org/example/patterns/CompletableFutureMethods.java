package org.example.patterns;

import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureMethods {
    public static void main(String[] args) {

        CompletableFuture<Integer> futureOrTime = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supplier runs on: " + Thread.currentThread().getName());
            sleep(1000);
            return 47;
        }).orTimeout(1000, TimeUnit.MILLISECONDS);

        CompletableFuture<String> futureCompleteOnTimeout = CompletableFuture.supplyAsync(() -> {
            System.out.println("Supplier runs on: " + Thread.currentThread().getName());
            sleep(1000);
            return "Succes bro!";
        }).completeOnTimeout("Fallback",1000, TimeUnit.MILLISECONDS);


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

        System.out.println("Main thread works");

        Integer result = doubled.join();

        System.out.println("Result: " + result);
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
