import argparse
import contextlib
import datetime
import os
import six
import time
import unicodedata

import dropbox
import sys
from dropbox.files import WriteMode

acess_token = 'token'
dbx = dropbox.Dropbox(acess_token)


class Cliente(object):

    def __init__(self, nome):
        self.nome = nome
        self.saida = 'f-saida_'+nome+'.txt'
        self.ent = 'f-ent_'+nome+'.txt'
        self.ultimaLinha = 0

def buscaClientes(nome):
    for i in range(len(clientes)):
        if(clientes[i].nome == nome):
            return True
    return False

def atualizaPorCliente(num):
    dbx.files_download_to_file('server/'+clientes[num].saida, '/sdi_aluno8/'+ clientes[num].saida)

    linhas = ""
    n_linhas = clientes[num].ultimaLinha
    cont=0
    saida = open('server/'+clientes[num].saida, 'r')
    for linha in saida:
        if(cont >= n_linhas):
            linhas += linha
            clientes[num].ultimaLinha += 1
        cont+= 1
    if(linhas != ""):
        print(linhas)

    for i in range(len(clientes)):
        if(i != num):
            dbx.files_download_to_file('server/'+clientes[i].ent, '/sdi_aluno8/'+ clientes[i].ent)
            with open('server/' + clientes[i].ent, 'a') as arq:
                arq.write(linhas)
            dbx.files_upload(open('server/' + clientes[i].ent).read(),'/sdi_aluno8/' + clientes[i].ent, mode=WriteMode('overwrite'))

def msgGlobal(msg):
    for i in range(len(clientes)):
        dbx.files_download_to_file('server/'+clientes[i].ent, '/sdi_aluno8/'+ clientes[i].ent)
        with open('server/' + clientes[i].ent, 'a') as arq:
            arq.write(msg)
        dbx.files_upload(open('server/' + clientes[i].ent).read(),'/sdi_aluno8/' + clientes[i].ent, mode=WriteMode('overwrite'))





clientes = []

res = dbx.files_list_folder("/SDI_aluno8/")
for i in range(len(res.entries)):
    dbx.files_delete('/SDI_aluno8/'+res.entries[i].name)


print("---Server Iniciado---")
res = dbx.files_list_folder("/SDI_aluno8/")
for i in range(len(res.entries)):
    print(res.entries[i].name)

while(True):
    res = dbx.files_list_folder("/SDI_aluno8/")
    for i in range(len(res.entries)):
        if(res.entries[i].name[2] == 's'):
            j=8
            nome=""
            while(res.entries[i].name[j]!='.'):
                nome = nome + res.entries[i].name[j]
                j += 1
            cliente1 = Cliente(nome)
            if(not buscaClientes(nome)):
                clientes.append(cliente1)
                print(cliente1.nome + ' entrou!')
                msgGlobal(cliente1.nome + ' entrou!\n')

    for j in range(0,len(clientes)):
        atualizaPorCliente(j)
