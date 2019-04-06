#include "server_utils.h"
#include <pthread.h>

User * createUser(char * name){
   User * temp = (User*) malloc(sizeof(User));
   temp->name = strdup(name);
   temp->messages = createQueue(0);
   return temp;
}

void * u_add(void * arg){
  u_add_parameters * temp = (u_add_parameters*) arg;
  pthread_mutex_lock(&lock);
  add(temp->user->messages, temp->message, *temp->messageId);
  pthread_mutex_unlock(&lock);
  //printf("u_add finished\n");
}

int u_amt(User * user){
    return amt(user->messages);
}

char ** u_top(User * user){
  return top(user->messages);
}

void u_pop(User * user){
  pop(&(user->messages));
}
