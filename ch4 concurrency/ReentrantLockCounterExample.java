import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockCounterExample {
    private static final int NUM_THREADS = 2;
    private static final int NUM_LOOPS = 100_000;

    private static int counter = 0;
    private static final ReentrantLock lock = new ReentrantLock();

    static class CounterThread extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < NUM_LOOPS; i++) {
                lock.lock();

                try {
                    counter++;
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new CounterThread();
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final counter = " + counter);
    }
}