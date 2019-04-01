#include <time.h>
#include <sys/time.h>
#include "tools.h"

// %%%%%%%%%%%%%%%%%%%%%% Queue function definitions %%%%%%%%%%%%%%%%%%%%%%%%%%
Queue * createQueue(int id){
  Queue * temp = (Queue*) malloc(sizeof(Queue));
  temp->next = NULL;
  temp->data = NULL;
  temp->messageId = id;
  temp->amt = 0;
  return temp;
}

void add(Queue * queue, char * message, int messageId){
  // Start looking for messageId.
  Queue * next = queue;
  size_t tam = strlen(message);
  char * teste = strdup(message);
  printf("Received message to add size %d\n", tam);
  if(tam == 0){
      printf("wasnull\n");
      return;
  }
  while(next->next != NULL && next->messageId != messageId){
    next = next->next;
  }
  // If reached end of queue and have not found messageId create a new Node.
  if(next->next == NULL && next->messageId != messageId){
    next->next = createQueue(messageId);
  }

  // Now add content as normal.
  if(next->amt == 0){
      next->data = (char**)malloc(sizeof(char*));
    *(next->data) = strdup(message);
    printf("\nNova String\n");
    next->amt++;
  }else {
    next->data = (char**)realloc(next->data, sizeof(char*) * next->amt);
    printf("\nConcatenando\n");
    *(next->data + next->amt*sizeof(char*)) = strdup(message);
    // printf("\nString atual: %s\n", *(next->data + next->amt*sizeof(char*)));
    next->amt++;
  }

}

int amt(Queue * queue){
    return queue->amt;
}

char ** top(Queue * queue){
  return queue->data;
}

void pop(Queue ** queue){
  if((*queue)->next == NULL){
    (*queue)->messageId = -1;
    return;
  }
  free(queue);
  Queue * next = (*queue)->next;
  queue = &next;

}
// %%%%%%%%%%%%%%%%%%% FERRAMENTAS p/ VETORES %%%%%%%%%%%%%%%%%%%
// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
double wtime() {
  struct timeval t;
  gettimeofday(&t, NULL);
  return t.tv_sec + (double) t.tv_usec / 1000000;
}

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int writeline (char *msg, FILE *file){
  fprintf(file,"%s", msg);
  return 0;
}

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
char *readline (FILE *file){
  static int cont=0;
  if(file == NULL){
      cont=0;
      return NULL;
  }
  char *getLine = NULL;
  char c;
  int linha=0;
  //printf("function readline: cont = %d\n", cont);
  getLine = (char *) malloc(256*sizeof(char));
  fseek( file, cont, SEEK_SET );

  for (;;cont++) {
    c = fgetc( file );
    if ((linha == 255) || (c ==  EOF)){
      c = '\0';
      getLine[linha] = c;

      //printf ("%d(%c)\n",cont, c);
      return (getLine);
    }
    getLine[linha++] = c;
    //printf ("%d[%d](%c)\n",cont, linha, c);
  }

  return NULL;
}
