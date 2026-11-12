package org.example.patterns;

import java.util.concurrent.*;

public class CallableAndFutureAndException {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<Integer> task = () -> {

            System.out.println("Task executing...");

            throw new IllegalStateException("Calculation Failed");
        };


        Future<Integer> future =  executorService.submit(task);


        try {
            System.out.println(future.get());
        } catch (InterruptedException e){
            System.out.println("Main thread is interrupted");
        } catch (ExecutionException e) {
            System.out.println("Worker thread is failed");
            System.out.println("Cause " + e.getCause());
        } finally {
            executorService.shutdown();
        }

    }
}
