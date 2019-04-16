#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct queue {
  struct queue* next;
  char** data;
  int messageId;
  size_t amt;
} Queue;

// Queue function declarations
Queue * createQueue(int id);
void add(Queue * queue, char * message, int messageId); // Recebe a mensagem e o id, e coloca no nodo enquanto o id for o mesmo
char ** top(Queue * queue);
int amt(Queue * queue);
void pop(Queue ** queue);


// externos a biblioteca tools
double wtime();

// Biblioteca p/ vetores
char *readline (FILE *file);
int writeline (char *msg, FILE *file);
