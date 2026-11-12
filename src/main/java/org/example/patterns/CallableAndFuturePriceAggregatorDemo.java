package org.example.patterns;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class CallableAndFuturePriceAggregatorDemo {

    private record Supplier(String name, Integer price) {
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        Future<Integer> supplierFuture1 = executorService.submit(() -> {
                Thread.sleep(700);
                return 100;
        });

        Future<Integer> supplierFuture2 = executorService.submit(() -> {
                Thread.sleep(1200);
                return 90;
        });

        Future<Integer> supplierFuture3 = executorService.submit(() -> {
                Thread.sleep(3000);
                return 80;
        });

        Integer supplier1sPrice = getPrice("Supplier1", supplierFuture1);
        Integer supplier2sPrice = getPrice("Supplier2", supplierFuture2);
        Integer supplier3sPrice = getPrice("Supplier3", supplierFuture3);

       Supplier bestPriceSupplier = findBestPrice(supplier1sPrice, supplier2sPrice, supplier3sPrice);

       if(Objects.nonNull(bestPriceSupplier)){
           System.out.println(
                   "Best price: "
                           + bestPriceSupplier.name()
                           + " "
                           + bestPriceSupplier.price()
           );
       } else {
           System.out.println(
                   "No supplier returned a valid price"
           );
       }

        executorService.shutdown();

        try {
            if (!executorService.awaitTermination(
                    5,
                    TimeUnit.SECONDS)) {

                executorService.shutdownNow();
            }

        } catch (InterruptedException e) {

            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    static Integer getPrice(String supplier, Future<Integer> future) {
        Integer supplierPrice;
        int TIMEOUT = 1500;
        try {
            supplierPrice = future.get(TIMEOUT, TimeUnit.MILLISECONDS);
            System.out.println("Supplier and it's price: " + supplier + " " + supplierPrice);
        } catch (TimeoutException e) {
            System.out.println(supplier + "'s future timedout, timeout is: " + TIMEOUT);
            boolean cancellation = future.cancel(true);
            System.out.println("Cancellation's status: " + cancellation);
            return null;
        } catch (ExecutionException e){
            System.out.println("Real cause of execution exception: " + e.getCause());
            return null;
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            return null;
        } catch (CancellationException e){
            System.out.println("Is " + supplier + "'s future cancelled? : " + future.isCancelled());
            System.out.println("Is " + supplier + "'s future done? : " + future.isDone());
            return null;
        }
        return supplierPrice;
    }

    static Supplier findBestPrice(  Integer price1,
                                    Integer price2,
                                    Integer price3){

        return Stream.of(  new Supplier("Supplier1", price1),
                new Supplier("Supplier2", price2),
                new Supplier("Supplier3", price3)).filter(s -> s.price != null).min(Comparator.comparingInt(Supplier::price)).orElse(null);

    }
}
