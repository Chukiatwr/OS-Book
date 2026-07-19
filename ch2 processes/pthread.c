/**
 * pthread.c
 * This program demonstrates the use of POSIX threads (pthreads) in C. 
 * Compile with: gcc -std=c17 pthread.c -o pthread -lpthread
 */
#include <stdio.h>
#include <string.h>
#include <pthread.h>

char shared_message[100];

void* child_thread(void* arg) {
    char* message = (char*)arg;

    // Print the message received from the main thread.
    printf("Child thread received: %s\n", message);

    // Modify the global variable.
    strcpy(shared_message, "Bye.");

    return NULL;
}

int main() {
    pthread_t thread;
    char message[] = "Hello, My thread child.";

    // Initialize the global variable.
    strcpy(shared_message, message);

    // Create a child thread and pass a message to it.
    pthread_create(&thread, NULL, child_thread, message);

    // Wait for the child thread to finish.
    pthread_join(thread, NULL);

    // Print the modified global variable.
    printf("Main thread reads: %s\n", shared_message);

    return 0;
}