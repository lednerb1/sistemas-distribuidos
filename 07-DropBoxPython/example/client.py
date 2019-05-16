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

acess_token = 'token'
dbx = dropbox.Dropbox(acess_token)
msg=""


def leitura():
	linha = 0
	while(True):
		cont = 0
		dbx.files_download_to_file(ent, '/sdi_aluno8/'+ ent)
		arq = open(ent, 'r')
		for line in arq:
			cont += 1
			if(cont > linha):
				print(line)
				linha += 1
		if(msg == nome+': /quit'):
			return;
		time.sleep(0.1)

def escrita():
	msg=""
	while(msg != nome+': /quit'):
		msg = raw_input()
		msg = nome+': '+ msg
		dbx.files_download_to_file(saida, '/sdi_aluno8/'+ saida)
		with open(saida, 'a') as arq:
			arq.write(msg)
			arq.write('\n')
		dbx.files_upload(open(saida).read(),'/sdi_aluno8/' + saida, mode=WriteMode('overwrite'))

nome = raw_input('Nome: ')
saida = 'f-saida_'+nome+'.txt'
ent = 'f-ent_'+nome+'.txt'


#dbx.files_download_to_file('TEST.txt', '/sdi_aluno8/'+ 'TEST.txt')
dbx.files_upload('', '/sdi_aluno8/' + saida, mode=WriteMode('overwrite'))
dbx.files_upload('', '/sdi_aluno8/' + ent, mode=WriteMode('overwrite'))
dbx.files_download_to_file(saida, '/sdi_aluno8/'+ saida)
dbx.files_download_to_file(ent, '/sdi_aluno8/'+ ent)

# opt = 0
# while(opt != 3):
# 	opt = input('[1]Enviar mensagem\n[2]Receber mensagems\n[3]Sair\n')

# 	if(opt == 1): #escreve a mensagem na saida
# 		dbx.files_download_to_file(saida, '/sdi_aluno8/'+ saida)
# 		msg = raw_input('Mensagem: ')
# 		msg = nome+': '+ msg
# 		with open(saida, 'a') as arq:
# 			arq.write(msg)
# 			arq.write('\n')

# 		dbx.files_upload(open(saida).read(),'/sdi_aluno8/' + saida, mode=WriteMode('overwrite'))

# 	if(opt == 2):
# 		dbx.files_download_to_file(ent, '/sdi_aluno8/'+ ent)
# 		arq = open(ent, 'r')
# 		for line in arq:
# 			print(line)

escrita_ = Thread(target=escrita)
leitura_ = Thread(target=leitura)

escrita_.start()
leitura_.start()
