#include <rpc/rpc.h>
#include "server_utils.h"
#include <string.h>
// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

#define MAX_CLIENTS 10

User* users[MAX_CLIENTS];

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
sPacket *getmessages_1_svc(char **name, struct svc_req *req) {
	char ** msg;
	int amt;
	int i;
	for(i=0; i<MAX_CLIENTS; i++){
		if(strcmp(users[i]->name, *name) == 0){
			printf("User found, getting messages\n");
			msg = u_top(users[i]);
			amt = u_amt(users[i]);
			u_pop(users[i]);
		}
	}
	sPacket pack;
	struct serverPacket temp;
	pack.sPacket_val = malloc(sizeof(temp) * amt);
	for(int i=0; i<amt; i++){
		(pack.sPacket_val + sizeof(temp) * amt)->chars.chars_val = strdup(*(msg + i*sizeof(char*)));
	}
	return(&pack);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *sendmessage_1_svc(struct packet *data, struct svc_req *req) {
	static int ret = 1;
	char * message = data->chars.chars_val;
	int messageId = data->idx;
	int t = strlen(message);
	// printf("Message: %s\n", message);
	// printf("Size: %d\n", t);

	if(message == NULL){
		printf("sendmessage_1_svc: Message is null\n");
		ret = 0;
	}
	else if(t == 0){
		printf("sendmessage_1_svc: Message length is null\n");
		ret = 0;
	}else {
		for(int i=0; i<MAX_CLIENTS; i++){
			if(users[i] != NULL){
				printf("Adding message to user %d\n", i);
				u_add(users[i], message, messageId);
			}
		}
	}

	return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *connect_1_svc(char **name, struct svc_req *req) {
	static int i;
	for(i=0; i<MAX_CLIENTS; i++){
		if(users[i] == NULL){
			users[i] = createUser(*name);
			break;
		}
	}
	if(i < 10){
		printf ("%s Connected with id %d\n", *name, i);
		return &i;
	}
	i=-1;
	return &i;
}
