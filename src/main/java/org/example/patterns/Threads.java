package org.example.patterns;

public class Threads {
    static int counter = 0;

    static void increment(){
        counter++;
    }

    static int getCounter(){
        return counter;
    }

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i <= 10; i++){
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 100_000; j++) {
                    increment();
                }
            });
            thread.start();
        }

        System.out.println(getCounter());
    }
}
