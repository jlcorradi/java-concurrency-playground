import java.util.Random;

public class DeadLock implements Runnable {

  private static final Object resource1 = new Object();
  private static final Object resource2 = new Object();
  private static final Integer MAX_ITERACTIONS = 10000;
  private static final Random random = new Random(System.currentTimeMillis());

  @Override
  public void run() {
    for (int i = 0; i < MAX_ITERACTIONS; i++) {
      boolean b = random.nextBoolean();
      if (b) {
        System.out.println(String.format("[%s] Trying to lock resource 1.", Thread.currentThread().getName()));
        synchronized (resource1) {
          System.out.println(String.format("[%s] Locked resource 1.", Thread.currentThread().getName()));
          System.out.println(String.format("[%s] Trying to lock resource 2.", Thread.currentThread().getName()));
          synchronized (resource2) {
            System.out.println(String.format("[%s] Locked resource 1.", Thread.currentThread().getName()));
          }
        }
      } else {
        System.out.println(String.format("[%s] Trying to lock resource 2.", Thread.currentThread().getName()));
        synchronized (resource2) {
          System.out.println(String.format("[%s] Locked resource 2.", Thread.currentThread().getName()));
          System.out.println(String.format("[%s] Trying to lock resource 1.", Thread.currentThread().getName()));
          synchronized (resource1) {
            System.out.println(String.format("[%s] Locked resource 2.", Thread.currentThread().getName()));
          }
        }
      }
    }
  }

  public static void main(String[] args) {
    Thread thread1 = new Thread(new DeadLock(), "Thread 1");
    Thread thread2 = new Thread(new DeadLock(), "Thread 2");
    thread1.start();
    thread2.start();
  }

}