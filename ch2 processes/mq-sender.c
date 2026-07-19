/**
 * mq-sender.c
 *
 * This program sends messages to a System V message queue.
 * It sends a series of messages containing the lyrics of "Twinkle, 
 * Twinkle, Little Star" to the queue. The program stops after sending 
 * all the lyrics and a final message with the text "*END*".
 * 
 * Compile with: gcc -o mq-sender mq-sender.c -std=c17
 * Run with: ./mq-sender
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

    const char *lyrics[] = {
        "Twinkle, twinkle, little star,",
        "How I wonder what you are.",
        "Up above the world so high,",
        "Like a diamond in the sky.",
        "When the blazing sun is gone,",
        "When he nothing shines upon,",
        "Then you show your little light,",
        "Twinkle, twinkle, all the night.",
        "Then the traveler in the dark",
        "Thanks you for your tiny spark."
    };

    msgid = msgget(QUEUE_KEY, 0666 | IPC_CREAT);
    if (msgid == -1) {
        perror("msgget");
        return 1;
    }

    msg.msg_type = 1;

    for (int i = 0; i < 10; i++) {
        snprintf(msg.text, MAX_TEXT, "%s", lyrics[i]);

        if (msgsnd(msgid, &msg, sizeof(msg.text), 0) == -1) {
            perror("msgsnd");
            return 1;
        }

        printf("Sent: %s\n", msg.text);
    }

    snprintf(msg.text, MAX_TEXT, "*END*");
    if (msgsnd(msgid, &msg, sizeof(msg.text), 0) == -1) {
        perror("msgsnd");
        return 1;
    }

    printf("Sent: *END*\n");

    return 0;
}