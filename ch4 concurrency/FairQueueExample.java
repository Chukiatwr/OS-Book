import java.util.LinkedList;
import java.util.Queue;

public class FairQueueExample {
    private static final Queue<Integer> queue = new LinkedList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 6; i++) {
                synchronized (lock) {
                    queue.add(i);
                    System.out.println("Added task " + i);
                    lock.notifyAll();
                }

                sleep(200);
            }
        });

        Thread worker = new Thread(() -> {
            int processed = 0;

            while (processed < 10) {
                int task;

                synchronized (lock) {
                    while (queue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    task = queue.remove();
                }

                System.out.println("Processed task " + task);
                processed++;
            }
        });

        producer.start();
        worker.start();

        producer.join();
        worker.join();
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
