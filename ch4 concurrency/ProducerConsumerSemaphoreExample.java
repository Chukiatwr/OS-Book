import java.util.concurrent.Semaphore;

public class ProducerConsumerSemaphoreExample {
    private static final int BUFFER_SIZE = 3;
    private static final int NUM_ITEMS = 8;
    private static final int[] buffer = new int[BUFFER_SIZE];
    private static int in = 0;
    private static int out = 0;
    private static final Semaphore empty = new Semaphore(BUFFER_SIZE);
    private static final Semaphore full = new Semaphore(0);
    private static final Object lock = new Object();

    private static void insertItem(int item) {
        buffer[in] = item;
        in = (in + 1) % BUFFER_SIZE;
    }

    private static int removeItem() {
        int item = buffer[out];
        out = (out + 1) % BUFFER_SIZE;
        return item;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= NUM_ITEMS; i++) {
                try {
                    empty.acquire();
                    synchronized (lock) {
                        insertItem(i);
                        System.out.println("Produced item " + i);
                    }
                    full.release();
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= NUM_ITEMS; i++) {
                try {
                    full.acquire();
                    int item;
                    synchronized (lock) {
                        item = removeItem();
                        System.out.println("Consumed item " + item);
                    }
                    empty.release();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
    }
}