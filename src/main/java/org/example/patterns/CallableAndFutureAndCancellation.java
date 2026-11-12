package org.example.patterns;

import java.util.concurrent.*;

public class CallableAndFutureAndCancellation {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {

            int workUnits = 0;

            try {
                while (true) {
                    Thread.sleep(200);
                    workUnits++;
                    System.out.println("Work units: " + workUnits);
                }
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Worker noticed cancellation");
                throw e;
            }
        });

        Thread.sleep(1000);

        boolean cancelResult = future.cancel(true);

        System.out.println("Cancel result : " + cancelResult);

        System.out.println("Future : " + future.isCancelled());

        System.out.println("Future : " + future.isDone());


        try {
            System.out.println(future.get());

        }  catch (CancellationException e){
            System.out.println("Future was cancelled");
        }
        catch (Exception e) {
            throw e;
        }

        executorService.shutdown();
    }
}
