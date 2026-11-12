package org.example.patterns;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureThenCompose {

    record User(Long id, String name){};

    public static void main(String[] args) {

        CompletableFuture<String> result = findUserAsync(90L)
                .thenCompose(CompletableFutureThenCompose::fetchGreetingsUser);

        System.out.println(result.join());
    }

    static CompletableFuture<User> findUserAsync(Long id) {

        return CompletableFuture.supplyAsync(() -> {
            sleep(500);

            System.out.println("User loaded by : " + Thread.currentThread().getName());

            return new User(id, "Isi");
        });
    }

    static CompletableFuture<String> fetchGreetingsUser(User user) {

        return CompletableFuture.supplyAsync(() -> {
            sleep(500);
            return "Hello " + user.name;
        });
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
