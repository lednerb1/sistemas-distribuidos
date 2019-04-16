#include <rpc/rpc.h>
#include <string.h>
#include <pthread.h>
// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"
#include "server_utils.h"


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
	static sPacket pack;
	struct serverPacket temp;
	pack.sPacket_val = malloc(sizeof(temp) * amt);
	for(int i=0; i<amt; i++){
		(pack.sPacket_val + sizeof(temp) * amt)->chars.chars_val = strdup(*(msg + i*sizeof(char*)));
	}
	return(&pack);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *sendmessage_1_svc(struct packet *data, struct svc_req *req) {
	pthread_mutex_init(&lock, NULL);
	static int ret = 1;
	char * name = data->nome.nome_val;
	char fileName[100];
	char * message = data->chars.chars_val;
	int messageId = data->idx;
	int t = strlen(message);

	sprintf(fileName, "%s-%d", name, messageId);
	printf("%s", fileName);
	FILE * arq = fopen(fileName, "w");

	if(arq != NULL){
		fseek(arq, 0, SEEK_END);
		fprintf(arq, "%s", message);
	}

	if(message == NULL){
		printf("sendmessage_1_svc: Message is null\n");
		ret = 0;
	}
	else if(t == 0){
		printf("sendmessage_1_svc: Message length is null\n");
		ret = 0;
	}else {
		ret = 1;

		for(int i=0; i<MAX_CLIENTS; i++){
			if(users[i] != NULL){
				pthread_t sendThread;
				pthread_attr_t attr;
				int s = pthread_attr_init(&attr);
				if(s != 0){
					printf("sendmessage_1_svc: Thread attr initialization error\n");
					exit(1);
				}else{
					printf("sendmessage_1_svc: Creating Thread: ADD MESSAGE TO USER %d\n", i);
				}
				u_add_parameters * param = (u_add_parameters*) malloc(sizeof(u_add_parameters));
				param->user = users[i];
				param->message = message;
				param->messageId = &messageId;
				sendThread = pthread_create(&sendThread, &attr, &u_add, param);
			}
		}
	}
	printf("finished adding messages\n");
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
