#include <rpc/rpc.h>
#include <string.h>
// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

char * users[10];

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
char **func0_1_svc(void *a, struct svc_req *req) {
	static char msg[256];
	static char *p;

	printf("FUNC0 (sem parâmetros)\n");
	strcpy(msg, "Hello Func0!");
	p = msg;

	return(&p);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *func1_1_svc(char **a, struct svc_req *req) {
	static int ret = 1;

	printf ("FUNC1 (%s)\n", *a);

	return (&ret);
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *connect_1_svc(char *a, struct svc_req *req) {
	int i;
	for(i=0; i<10; i++){
		if(users[i] == NULL){
			users[i] = strdup(a);
			break;
		}
	}
	if(i < 10){
		printf ("%s Connected\n", a);
		return &i;
	}
	return -1;
}

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
int *func3_1_svc(struct param *a, struct svc_req *req) {
	static int ret=0;

	printf ("FUNC3 (%d/%d)\n", a->arg1, a->arg2);
	ret = a->arg1 * a->arg2;
	return (&ret);
}
