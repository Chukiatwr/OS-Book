/**
 * counter.c
 * This program demonstrates a simple counter increment using 
 * multiple threads. The final value of the counter may not be 
 * as expected due to race conditions since the counter variable 
 * is shared among threads without synchronization mechanisms.
 */
#include <stdio.h>
#include <pthread.h>

#define NUM_THREADS 5
#define NUM_LOOPS 100000

int counter = 0;

void* increment_counter(void* arg) {
    for (int i = 0; i < NUM_LOOPS; i++) {
        counter++;
    }
    return NULL;
}

int main() {
    pthread_t threads[NUM_THREADS];

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_create(&threads[i], NULL, increment_counter, NULL);
    }

    for (int i = 0; i < NUM_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Final counter = %d\n", counter);
    return 0;
}
