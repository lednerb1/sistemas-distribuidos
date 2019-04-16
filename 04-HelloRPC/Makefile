all: client server main_file

hw.h: hw.x
	rpcgen hw.x

hw_svc.c hw_clnt.c hw_client.c: hw.h

hw_xdr.o: hw_xdr.c
	gcc -g -o hw_xdr.o -c hw_xdr.c

tools.o: tools.c
	gcc -g -c tools.c -o tools.o

server_utils.o: server_utils.c
	gcc -g -c server_utils.c -pthread

main_file: main_file.c tools.o server_utils.o
	gcc -g -o main_file tools.o server_utils.o main_file.c

client: hw_client.o hw_clnt.o hw_xdr.o
	gcc -g -o client hw_client.o hw_clnt.o hw_xdr.o tools.c -lnsl -pthread

server: hw_server.o hw_svc.o server_utils.o tools.o
	gcc -g -o server hw_server.o hw_svc.o hw_xdr.o tools.o server_utils.o -lnsl -pthread

.PHONY: clean

clean:
	-rm *.o
	-rm client
	-rm server
	-rm main_file
	-rm *.serv
	-rm hw.h
	-rm hw_clnt.c
	-rm hw_svc.c
