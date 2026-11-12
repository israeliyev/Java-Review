package org.example.patterns;

class Counter{
    int count;

    void increment(){
        count++;
    }

    int get(){
        return count;
    }
}

public class UnsafeCounterDemo {

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i <= 100000; i++){
                counter.increment();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i <= 100000; i++){
                counter.increment();
            }
        });

        Thread thread3 = new Thread(() -> {
            for (int i = 0; i <= 100000; i++){
                counter.increment();
            }
        });

        Thread thread4 = new Thread(() -> {
            for (int i = 0; i <= 100000; i++){
                counter.increment();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();

        System.out.println(counter.get());

    }

}
