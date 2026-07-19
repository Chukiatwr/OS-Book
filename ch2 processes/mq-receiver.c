/** mq-receiver.c
 * 
 * This program receives messages from a System V message queue 
 * and prints them to the console. It stops receiving when it gets 
 * a message with the text "*END*".
 * 
 * Compile with: gcc -std=c17 -o mq-receiver mq-receiver.c
 * Run with: ./mq-receiver
 */

#include <stdio.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/msg.h>

#define QUEUE_KEY 1234
#define MAX_TEXT 256

struct message {
    long msg_type;
    char text[MAX_TEXT];
};

int main() {
    int msgid;
    struct message msg;

    msgid = msgget(QUEUE_KEY, 0666 | IPC_CREAT);
    if (msgid == -1) {
        perror("msgget");
        return 1;
    }

    while (1) {
        if (msgrcv(msgid, &msg, sizeof(msg.text), 1, 0) == -1) {
            perror("msgrcv");
            return 1;
        }

        if (strcmp(msg.text, "*END*") == 0) {
            printf("Received END signal. Receiver stopped.\n");
            break;
        }

        printf("Received: %s\n", msg.text);
    }

    msgctl(msgid, IPC_RMID, NULL);

    return 0;
}