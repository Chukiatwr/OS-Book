/**
 * threads.cpp
 * This program demonstrates the use of C++ threads.
 * Compile with: g++ -std=c++14 threads.cpp -o threads -lpthread
 */
#include <iostream>
#include <string>
#include <thread>

std::string shared_message;

void child_thread(std::string message) {
    // Print the message received from the main thread.
    std::cout << "Child thread received: " << message << std::endl;

    // Modify the global variable.
    shared_message = "Bye.";
}

int main() {
    std::string message = "Hello, My thread child.";

    // Initialize the global variable.
    shared_message = message;

    // Create a child thread and pass a message to it.
    std::thread t(child_thread, message);

    // Wait for the child thread to finish.
    t.join();

    // Print the modified global variable.
    std::cout << "Main thread reads: " << shared_message << std::endl;

    return 0;
}