import java.util.concurrent.Semaphore;

public class ReadersWritersExample {
    private static int sharedData = 0;
    private static int readCount = 0;
    private static final Object readCountLock = new Object();
    private static final Semaphore resourceLock = new Semaphore(1);

    static class Reader extends Thread {
        private final int id;

        public Reader(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                synchronized (readCountLock) {
                    readCount++;
                    if (readCount == 1) {
                        resourceLock.acquire();
                    }
                }
                System.out.println("Reader " + id + " reads sharedData = " + sharedData);
                Thread.sleep(1000);
                synchronized (readCountLock) {
                    readCount--;
                    if (readCount == 0) {
                        resourceLock.release();
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    static class Writer extends Thread {
        private final int id;

        public Writer(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            try {
                resourceLock.acquire();
                sharedData++;
                System.out.println("Writer " + id + " writes sharedData = " + sharedData);
                Thread.sleep(1000);
                resourceLock.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] readers = { new Reader(1), new Reader(2), new Reader(3) };
        Thread[] writers = { new Writer(1), new Writer(2) };
        for (Thread reader : readers) {
            reader.start();
        }
        for (Thread writer : writers) {
            writer.start();
        }
        for (Thread reader : readers) {
            reader.join();
        }
        for (Thread writer : writers) {
            writer.join();
        }
    }
}