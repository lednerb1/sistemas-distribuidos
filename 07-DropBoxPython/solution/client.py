import argparse
import contextlib
import datetime
import os
import six
import sys
import time
import unicodedata

import dropbox
import sys
from dropbox.files import WriteMode
from threading import Thread

acess_token = 'herJZmKT9SEAAAAAAAAAZu69yoo3Bbvik3TmSGPjFepL6Kp0b9QwkykA20KbPjx9'
dbx = dropbox.Dropbox(acess_token)
msg=""

def leitura():
	linha = 0
	while(True):
		cont = 0
		dbx.files_download_to_file(inp, '/Client/'+ inp)
		arq = open(inp, 'r')
		for line in arq:
			cont += 1
			if(cont > linha):
				print(line)
				linha += 1
		if(msg == name+': /quit'):
			return;
		time.sleep(0.1)

def escrita():
	global msg
	while(msg != name+': /quit'):
		msg = input()
		msg = name+': '+ msg
		dbx.files_download_to_file(output, '/Client/'+ output)
		with open(output, 'a') as arq:
			arq.write(msg)
			arq.write('\n')
		dbx.files_upload(open(output).read().encode(),'/Client/' + output, mode=WriteMode('overwrite'))

name = input('Nome: ')
output = 'f-saida_'+name+'.txt'
inp = 'f-ent_'+name+'.txt'

dbx.files_upload(''.encode(), '/Client/' + output, mode=WriteMode('overwrite'))
dbx.files_upload(''.encode(), '/Client/' + inp, mode=WriteMode('overwrite'))
dbx.files_download_to_file(output, '/Client/'+ output)
dbx.files_download_to_file(inp, '/Client/'+ inp)

leitura_ = Thread(target=leitura)
leitura_.start()
escrita()
