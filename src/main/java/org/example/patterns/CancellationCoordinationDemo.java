package org.example.patterns;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CancellationCoordinationDemo {

    private static final AtomicInteger activeWorkers = new AtomicInteger(0);
    private static final AtomicInteger maxActiveWorkers = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {

        final int TOTAL_WORKERS = 6;
        final int MAX_PERMITS = 2;

        CountDownLatch started = new CountDownLatch(TOTAL_WORKERS);
        CountDownLatch finished = new CountDownLatch(TOTAL_WORKERS);
        Semaphore semaphore = new Semaphore(MAX_PERMITS);

        Thread[] workers = new Thread[TOTAL_WORKERS];

        for(int i = 0; i < TOTAL_WORKERS; i++){
            int workerId = i+1;
            workers[i] = new Thread(new WorkerTask(workerId, started, finished, semaphore));
            workers[i].start();
        }

        started.await();

        Thread.sleep(500);


        for(Thread worker : workers){
            worker.interrupt();
        }

        finished.await();
        System.out.println("[Main] Maksimum eyni anda çalışan worker sayı: " + maxActiveWorkers.get());
    }

    static class WorkerTask implements Runnable {

        private final int id;
        private final CountDownLatch started;
        private final CountDownLatch finished;
        private final Semaphore semaphore;

        WorkerTask(int id, CountDownLatch started, CountDownLatch finished, Semaphore semaphore) {
            this.id = id;
            this.started = started;
            this.finished = finished;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {

            started.countDown();
            boolean acquired = false;
            int workUnits = 0;

            try{
                semaphore.acquire();
                acquired = true;

                int currentActiveWorkers = activeWorkers.incrementAndGet();
                updateMaxActiveWorkers(currentActiveWorkers);

                System.out.println(Thread.currentThread().getName() + " joined to semaphore and active workers count " + activeWorkers.get());

                while (!Thread.currentThread().isInterrupted()){
                    Thread.sleep(100);
                    workUnits++;
                }

            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " was interrupted!");
            }  finally {

                if(acquired){
                    activeWorkers.decrementAndGet();
                    semaphore.release();
                    System.out.println(Thread.currentThread().getName() + " is exit from semaphore and work units count is done : " + workUnits);
                }
                finished.countDown();
            }

        }

        private void updateMaxActiveWorkers(int activeWorkersCount) {
            maxActiveWorkers.accumulateAndGet(activeWorkersCount, Math::max);
        }
    }

}
