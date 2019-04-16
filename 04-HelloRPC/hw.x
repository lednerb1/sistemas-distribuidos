struct packet {
	char nome<>;
	char chars<>;
	int idx;
};

struct serverPacket {
	char chars<>;
	int amt;
};

typedef serverPacket sPacket<>;

program PROG {
	version VERS {
		sPacket getMessages(string)	= 1;
		int		sendMessage(packet)	= 2;
		int		connect(string)		= 3;
	} = 1;
} = 0x30009999;
