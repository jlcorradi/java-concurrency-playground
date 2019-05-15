package com.testland;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorExample implements Runnable {

    private static AtomicInteger counter = new AtomicInteger();
    private final int taskId;

    public ThreadPoolExecutorExample(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId(){
        return taskId;
    }

    @Override
    public void run() {
        try {
            System.out.println(String.format("[%s] - Executing", Thread.currentThread().getName()));
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>(10);

        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                int currentCount = counter.getAndIncrement();
                System.out.println("Creating new Thread: " + currentCount);
                return new Thread(r, "mythread" + currentCount);
            }
        };

        RejectedExecutionHandler rejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                if (r instanceof ThreadPoolExecutorExample) {
                    final ThreadPoolExecutorExample example = (ThreadPoolExecutorExample) r;
                    System.out.println("Rejecting task with id " + example.getTaskId());
                }
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, queue, threadFactory, rejectedExecutionHandler);

        for(int i = 0; i < 100; i++) {
            executor.execute(new ThreadPoolExecutorExample(i));
        }

        executor.shutdown();
    }

}
