/**
 * semaphoreExample.java
 * This example demonstrates the use of a Semaphore to control access to a limited resource.
 * In this case, we have a limited number of slots (3) that threads can enter.
 */
import java.util.concurrent.Semaphore;

public class SemaphoreExample {
    private static final int NUM_THREADS = 8;
    private static final Semaphore slots = new Semaphore(3);

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            int id = i;
            threads[i] = new Thread(() -> {
                boolean acquired = false;
                try {
                    slots.acquire();
                    acquired = true;
                    System.out.println("Thread " + id + " enters the limited section");
                    Thread.sleep(1000);
                    System.out.println("Thread " + id + " leaves the limited section");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    if (acquired) {
                        slots.release();
                    }
                }
            });
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}