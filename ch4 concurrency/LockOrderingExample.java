/**
 * Example demonstrating lock ordering to prevent deadlocks.
 */
public class LockOrderingExample {
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> useBothLocks("Thread one"));
        Thread threadTwo = new Thread(() -> useBothLocks("Thread two"));

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
    }

    private static void useBothLocks(String threadName) {
        synchronized (lockA) {
            System.out.println(threadName + " acquired lockA");
            sleep(1000);

            synchronized (lockB) {
                System.out.println(threadName + " acquired lockB");
                System.out.println(threadName + " acquired both locks");
            }
        }
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
