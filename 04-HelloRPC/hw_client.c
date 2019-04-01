#include <stdio.h>
#include "tools.h"
#include <rpc/rpc.h>
#include <pthread.h>
#include <unistd.h>

// Interface gerada pelo RPCGen a partir da IDL (hw.x) especificada
#include "hw.h"

CLIENT *cl;
char * name;
int fileCounter = 0;

static void *sendMessageThread(void * arg){

	int e;
	int messageId=0;
	getchar();

	while(1){
		struct packet * p = (struct packet *) malloc(sizeof(struct packet));
		char * fileName = (char*) malloc(sizeof(char)*100);
		FILE * arq;
		char * line;
		p->idx = messageId;

		printf("SENDING_THREAD: Pressione ENTER para enviar o arquivo chat%s-%d.chat\n", name, fileCounter);
		getchar();

		sprintf(fileName, "chat%s-%d.chat", name, fileCounter);
		arq = fopen(fileName, "r");

		if(arq == NULL){
			printf("SENDING_THREAD: Arquivo \"%s\" nao encontrado\n", fileName);
			free(fileName);
			continue;
		}
		char * reset = readline(NULL);
		if(reset != NULL)
			continue;
		printf("SENDING_THREAD: Enviando arquivo\n");
		while((p->chars.chars_val = readline(arq)) != NULL){
			p->chars.chars_len = strlen(p->chars.chars_val);
			if(p->chars.chars_len == 0)
				break;
			printf("SENDING_THREAD: Enviando fragmento\n");
			if(*(sendmessage_1(p, cl)) == 0){
				free(p->chars.chars_val);
				break;
			}
			free(p->chars.chars_val);
		}
		printf("SENDING_THREAD: Arquivo enviado\n");
		fileCounter++;
		messageId++;
		free(arq);
		free(fileName);
	}

	return 1;
}

int main (int argc, char *argv[]) {
	// Estrutura RPC de comunicação

	// Parâmetros das funçcões
	char         *par_f1 = (char *) malloc(256*sizeof(char));
	int           par_f2;
	struct packet par_f3;

	// Retorno das funções
	char **ret_f0 = NULL;
	int   *ret_f1 = NULL;
	int   *ret_f2 = NULL;
	int   *ret_f3 = NULL;


	// Verificação dos parâmetros oriundos da console
	if (argc != 3) {
		printf("ERRO: ./client <hostname> <msg>\n");
		exit(1);
	}

	// Conexão com servidor RPC
	cl = clnt_create(argv[1], PROG, VERS, "tcp");
	if (cl == NULL) {
		clnt_pcreateerror(argv[1]);
		exit(1);
	}

	name = malloc(sizeof(char)*30);
	printf("Nome: ");
	scanf("%[^\n]s", name);

	int * connected = connect_1(&name, cl);
	if(connected == NULL){
		printf("nao pode conectar");
		exit(1);
	}
	pthread_t sendThread;
	pthread_attr_t attr;
	int s = pthread_attr_init(&attr);
	if(s != 0){
		printf("thread attr initialization error");
		exit(1);
	}
	int state = pthread_create(&sendThread, &attr, &sendMessageThread, NULL);
	int a = pthread_join(sendThread, NULL);
	printf("E");
	// Atribuições de valores para os parâmetros
	// strcpy (par_f1, argv[2]);
	// par_f2 = 1;
	// par_f3.arg1 = 5;
	// par_f3.arg2 = 4;
	//
	// // Chamadas das funções remotas
	// printf ("Chamando func0 (sem parâmetros)\n");
	// ret_f0 = func0_1(NULL, cl);
	// if (ret_f0 == NULL) {
	// 	clnt_perror(cl,argv[1]);
	// 	exit(1);
	// }
	// printf ("Retorno func0 (%s)\n", *ret_f0);
	//
	// printf ("Chamando func1 (%s)\n", par_f1);
	// ret_f1 = func1_1(&par_f1, cl);
	// if (ret_f1 == NULL) {
	// 	clnt_perror(cl,argv[1]);
	// 	exit(1);
	// }
	// printf ("Retorno func1 (%d)\n", *ret_f1);
	//
	// printf ("Chamando func2 (%d)\n", par_f2);
	// ret_f2 = func2_1(&par_f2, cl);
	// if (ret_f2 == NULL) {
	// 	clnt_perror(cl,argv[1]);
	// 	exit(1);
	// }
	// printf ("Retorno func2 (%d)\n", *ret_f2);
	//
	// printf ("Chamando func3 (%d/%d)\n", par_f3.arg1, par_f3.arg2);
	// ret_f3 = func3_1(&par_f3, cl);
	// if (ret_f3 == NULL) {
	// 	clnt_perror(cl,argv[1]);
	// 	exit(1);
	// }
	// printf ("Retorno func3 (%d)\n", *ret_f3);


	return 0;
}
