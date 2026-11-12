package org.example.patterns;

import java.util.concurrent.*;

public class CallableAndFutureAndTimeOut {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future<String> future = executorService.submit(() -> {

            System.out.println("Task is started");

            Thread.sleep(3000);

            System.out.println("Task is finished");

            return "Success";
        });


        try {
            System.out.println(future.get(1, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }
}
