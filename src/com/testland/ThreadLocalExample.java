package com.testland;

public class ThreadLocalExample implements Runnable {
  private static final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
  private final int value;

  public ThreadLocalExample(int value) {
    this.value = value;
  }

  @Override
  public void run() {
    threadLocal.set(value);
    Integer integer = threadLocal.get();
    System.out.println(String.format("[%s] - %d", Thread.currentThread().getName(), integer));
  }

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[5];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new ThreadLocalExample(i), "thread-" + String.valueOf(i));
      threads[i].start();
    }

    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }
  }

}