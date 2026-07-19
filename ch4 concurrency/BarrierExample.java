/**
 * BarrierExample.java
 * This example demonstrates the use of a CyclicBarrier to synchronize 
 * multiple threads. Each thread performs some work in phase 1, waits 
 * at the barrier, and then proceeds to phase 2.
 */
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class BarrierExample {
    private static final int NUM_THREADS = 4;
    private static final CyclicBarrier barrier = new CyclicBarrier(NUM_THREADS);

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new WorkerThread(i);
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }

    static class WorkerThread extends Thread {
        private final int id;

        WorkerThread(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                System.out.println("Thread " + id + ": phase 1 starts");
                Thread.sleep((id + 1) * 500L);
                System.out.println("Thread " + id + ": phase 1 ends");

                barrier.await();

                System.out.println("Thread " + id + ": phase 2 starts");

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (BrokenBarrierException e) {
                System.out.println("Barrier was broken");
            }
        }
    }
}
