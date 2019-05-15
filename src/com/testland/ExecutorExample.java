package com.testland;

import java.util.Random;
import java.util.concurrent.*;

public class ExecutorExample implements Callable<Integer> {

    private static Random random = new Random(System.currentTimeMillis());

    @Override
    public Integer call() throws Exception {
        Thread.sleep(10000);
        return random.nextInt(100);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<Integer>[] futures = new Future[5];

        for (int i = 0; i < futures.length; i++) {
            futures[i] = executorService.submit(new ExecutorExample());
        }

        for (int i = 0; i < futures.length; i++) {
            System.out.println("Value Retrieved: " + futures[i].get());
        }

        executorService.shutdown();
    }
}
