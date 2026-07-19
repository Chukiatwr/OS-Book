/**
 * This example demonstrates a potential deadlock situation due to inconsistent lock ordering.
 */
public class DeadLockExample {
    private static final Object lockA = new Object();
    private static final Object lockB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread one acquired lockA");
                sleep(1000);

                synchronized (lockB) {
                    System.out.println("Thread one acquired lockB");
                }
            }
        });

        Thread threadTwo = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("Thread two acquired lockB");
                sleep(1000);

                synchronized (lockA) {
                    System.out.println("Thread two acquired lockA");
                }
            }
        });

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
