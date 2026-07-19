import threading

shared_message = ""

def child_thread(message):
    global shared_message

    # Print the message received from the main thread.
    print("Child thread received:", message)

    # Modify the global variable.
    shared_message = "Bye."

def main():
    global shared_message

    message = "Hello, My thread child."

    # Initialize the global variable.
    shared_message = message

    # Create a child thread and pass a message to it.
    thread = threading.Thread(target=child_thread, args=(message,))

    # Start the child thread.
    thread.start()

    # Wait for the child thread to finish.
    thread.join()

    # Print the modified global variable.
    print("Main thread reads:", shared_message)

if __name__ == "__main__":
    main()