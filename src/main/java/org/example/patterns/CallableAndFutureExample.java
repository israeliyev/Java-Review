package org.example.patterns;

import java.util.concurrent.*;

public class CallableAndFutureExample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(5);


        Callable<Integer> calculationTask = ()-> {

            System.out.println("Calculation started by " + Thread.currentThread().getName());

            Thread.sleep(3000);

            System.out.println("Calculation is finished");

            return 42 * 80;
        };

        Future<Integer> integerFuture = executorService.submit(calculationTask);

        System.out.println("Main thread continues..");

        Integer result = integerFuture.get();

        System.out.println("Calculation Result: " + result);

        executorService.shutdown();

    }

}
