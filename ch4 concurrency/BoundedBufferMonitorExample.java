/**
 * This example demonstrates a bounded buffer implementation using a monitor pattern.
 * It includes a producer and a consumer that operate on the bounded buffer.
 */
public class BoundedBufferMonitorExample {
    static class BoundedBuffer {
        private final int[] buffer;
        private int in = 0;
        private int out = 0;
        private int count = 0;

        public BoundedBuffer(int size) {
            buffer = new int[size];
        }

        public synchronized void put(int item) throws InterruptedException {
            while (count == buffer.length) {
                wait();
            }

            buffer[in] = item;
            in = (in + 1) % buffer.length;
            count++;

            notifyAll();
        }

        public synchronized int take() throws InterruptedException {
            while (count == 0) {
                wait();
            }

            int item = buffer[out];
            out = (out + 1) % buffer.length;
            count--;

            notifyAll();

            return item;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(4);

        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    buffer.put(i);
                    System.out.println("Produced item " + i);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 8; i++) {
                try {
                    int item = buffer.take();
                    System.out.println("Consumed item " + item);
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