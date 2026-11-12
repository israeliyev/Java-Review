package org.example.patterns;

public class VisibilityDemo {

    static class Worker {

        private boolean running = true;

        void run() {
            while (running) {
                // intentionally empty
            }
        }

        void stop() {
            running = false;
        }
    }

    public static void main(String[] args)
            throws InterruptedException {

        Worker worker = new Worker();

        Thread workerThread =
                new Thread(worker::run);

        // Useful because an unsafe worker may never terminate.
        workerThread.setDaemon(true);

        workerThread.start();

        Thread.sleep(100);

        worker.stop();

        workerThread.join(1000);

        System.out.println(
                "Worker terminated: "
                        + !workerThread.isAlive()
        );
    }
}