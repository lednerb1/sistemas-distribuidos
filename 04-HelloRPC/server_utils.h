#include "tools.h"

typedef struct user {
  char * name;
  Queue * messages;
} User;

// User function declarations
User * createUser(char * name);
void u_add(User * user, char * message, int messageId);
char * u_top(User * user);
void u_pop(User * user);