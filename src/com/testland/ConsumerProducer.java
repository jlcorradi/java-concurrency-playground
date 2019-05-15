package com.testland;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ConsumerProducer {

  private static final Queue<Integer> queue = new ConcurrentLinkedDeque<>();
  private static final long startMillis = System.currentTimeMillis();

  public static class Consumer implements Runnable {

    @Override
    public void run() {
      while (System.currentTimeMillis() < (startMillis + 10000)) {
        synchronized (queue) {
          try {
            queue.wait();
          } catch (InterruptedException ex) {
            ex.printStackTrace();
          }
        }

        if (!queue.isEmpty()) {
          Integer integer = queue.poll();
          System.out.println(String.format("[%s]: Polled: %d", Thread.currentThread().getName(), integer));
        }
      }
    }
  }

  public static class Producer implements Runnable {

    @Override
    public void run() {
      int i = 0;
      while (System.currentTimeMillis() < (startMillis + 10000)) {
        queue.add(i++);
        synchronized (queue) {
          queue.notify();
        }

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        synchronized (queue) {
          queue.notifyAll();
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread[] consumerThreads = new Thread[1];
    for (int i = 0; i < consumerThreads.length; i++) {
      consumerThreads[i] = new Thread(new Consumer(), String.format("consumer-%d", i));
      consumerThreads[i].start();
    }

    Thread producerThread = new Thread(new Producer(), "producer");
    producerThread.start();

    for (int i = 0; i < consumerThreads.length; i++) {
      consumerThreads[i].join();
    }
    producerThread.join();
    System.out.println("Program finished");
  }

}