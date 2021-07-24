package com.testland;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SynchronizedAndWait {

    private static final Queue<Integer> queue = new ConcurrentLinkedQueue<>();

    public synchronized Integer getNextInt() {
        Integer retVal = null;
        while (retVal == null) {
            synchronized (queue) {
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                retVal = queue.poll();
            }
        }
        return retVal;
    }

    public synchronized void putInt(Integer value) {
        synchronized (queue) {
            queue.add(value);
            queue.notify();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final SynchronizedAndWait queue = new SynchronizedAndWait();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                queue.putInt(i);
            }
        }, "Thread1");
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                Integer nextInteger = queue.getNextInt();
                System.out.println("Next Int: " + nextInteger.toString());
            }
        });
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Finished");
    }
}