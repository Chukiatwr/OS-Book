/**
 * LiveLockExample.java
 *
 * This example demonstrates a livelock scenario where two threads
 * continuously yield to each other, resulting in neither making progress.
 */
public class LivelockExample {
    private static volatile boolean threadOneTurn = true;
    private static volatile boolean threadTwoTurn = true;

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            int attempts = 0;

            while (threadTwoTurn && attempts < 5) {
                System.out.println("Thread one gives way to thread two");
                threadOneTurn = false;
                sleep(500);
                threadOneTurn = true;
                attempts++;
            }

            System.out.println("Thread one proceeds");
        });

        Thread threadTwo = new Thread(() -> {
            int attempts = 0;

            while (threadOneTurn && attempts < 5) {
                System.out.println("Thread two gives way to thread one");
                threadTwoTurn = false;
                sleep(500);
                threadTwoTurn = true;
                attempts++;
            }

            System.out.println("Thread two proceeds");
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
