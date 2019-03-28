struct param {
	int arg1;
	int arg2;
};

program PROG {
	version VERS {
		string getMessages(void)   = 1;
		int    sendMessage(string) = 2;
		int    connect(string)    = 3;
		int    func3(param)  = 4;
	} = 1;
} = 0x30009999;
