/**
 * This class demonstrates a race condition in Java.
 */
public class RaceConditionExample {
    private static final int NUM_THREADS = 2;
    private static final int NUM_LOOPS = 100_000;

    private static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < NUM_LOOPS; j++) {
                    counter++;
                }
            });

            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Final counter = " + counter);
    }
}
