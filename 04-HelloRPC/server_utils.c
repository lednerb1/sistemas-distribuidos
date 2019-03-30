#include "server_utils.h"

User * createUser(char * name){
   User * temp = (User*) malloc(sizeof(User));
   temp->name = strdup(name);
   temp->messages = createQueue(0);
   return temp;
}

void u_add(User * user, char * message, int messageId){
  add(user->messages, message, messageId);
}

char * u_top(User * user){
  return top(user->messages);
}

void u_pop(User * user){
  pop(&(user->messages));
}
