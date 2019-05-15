package com.testland;

public class NotSyncronized implements Runnable {

  static int counter = 0;

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[10];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new NotSyncronized(), String.format("Thread %d", counter));
      threads[i].start();
    }

    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }

    System.out.println("All threads finished");
  }

  @Override
  public void run() {
    synchronized (NotSyncronized.class) {
      while (counter < 10) {
        System.out.format("Before: %s - %d\n", Thread.currentThread().getName(), counter);
        counter++;
        System.out.format("After: %s - %d\n", Thread.currentThread().getName(), counter);
      }
    }
  }
}