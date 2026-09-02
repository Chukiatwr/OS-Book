/**
 * JavaThreads.java
 * This program demonstrates the use of Java threads.
 */
public class JavaThreads {

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

            // Modify the shared variable.
            sharedMessage = "Bye.";
        }
    }

    public static void main(String[] args) {
        sharedMessage = "Hello.";

        System.out.println("Main thread sends: " + sharedMessage);

        // Create a child task.
        ChildTask task = new ChildTask(sharedMessage);

        // Create a thread to run the task.
        Thread childThread = new Thread(task);

        // Start the child thread.
        childThread.start();

        try {
            // Wait until the child thread finishes.
            childThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread was interrupted.");
        }

        System.out.println("Shared message after child thread: " + sharedMessage);
    }
}