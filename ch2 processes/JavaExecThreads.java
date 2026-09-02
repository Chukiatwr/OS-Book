/**
 * JavaThreads.java
 * This program demonstrates the use of Java threads.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JavaExecThreads {
    static String sharedMessage;

    static class ChildTask implements Runnable {
        private String message;

        public ChildTask(String message) {
            this.message = message;
        }

        @Override
        public void run() {
            // Print the message received from the main thread.
            System.out.println("Child thread received: " + message);

            // Modify the global variable.
            sharedMessage = "Bye.";
        }
    }

    public static void main(String[] args) throws Exception {
        String message = "Hello, My thread child.";

        // Initialize the global variable.
        sharedMessage = message;

        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Create a child task and submit it to the executor.
        ChildTask task = new ChildTask(message);
        Future<?> future = executor.submit(task);

        // Wait for the child thread to finish.
        future.get();

        // Print the modified global variable.
        System.out.println("Main thread reads: " + sharedMessage);

        executor.shutdown();
    }
}