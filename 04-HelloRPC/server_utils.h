#include "tools.h"

typedef struct user {
  char * name;
  Queue * messages;
} User;

typedef struct {
    User * user;
    char * message;
    int  * messageId;
} u_add_parameters;

pthread_mutex_t lock;

// User function declarations
User * createUser(char * name);
void * u_add(void * param);
int u_amt(User * user);
char ** u_top(User * user);
void u_pop(User * user);
