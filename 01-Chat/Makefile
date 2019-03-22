# GNU Makefile

JC = javac
JFLAGS = #-Xlint
TARGET = MultiThreadChatServer.class MultiThreadChatClient.class ClientThread.class

all: $(TARGET)

ClientThread.class: ClientThread.java
	$(JC) $(JFLAGS) ClientThread.java

MultiThreadChatServer.class: MultiThreadChatServer.java
	$(JC) $(JFLAGS) MultiThreadChatServer.java

MultiThreadChatClient.class: MultiThreadChatClient.java
	$(JC) $(JFLAGS) MultiThreadChatClient.java

#HeartBeat.class: HeartBeat.java
#	$(JC) $(JFLAGS) HeartBeat.java

clean:
	rm -f *~ $(TARGET)
	rm -f *.client*
	rm -f *.serv*
	rm -f *.chat*
