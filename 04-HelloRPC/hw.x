struct packet {
	char chars<>;
	int idx;
};

program PROG {
	version VERS {
		string getMessages(string)   = 1;
		int    sendMessage(packet) 	 = 2;
		int    connect(string)    	 = 3;
	} = 1;
} = 0x30009999;
