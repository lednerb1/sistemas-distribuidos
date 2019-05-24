# https://dropbox-sdk-python.readthedocs.io/en/latest/api/dropbox.html

from __future__ import print_function

import argparse
import contextlib
import datetime
import os
import six
import sys
import time
import unicodedata
import linecache
import dropbox

if sys.version.startswith('2'):
    input = raw_input  # noqa: E501,F821; pylint: disable=redefined-builtin,undefined-variable,useless-suppression

# OAuth2 access token.  TODO: login etc.
TOKEN = 'herJZmKT9SEAAAAAAAAAZu69yoo3Bbvik3TmSGPjFepL6Kp0b9QwkykA20KbPjx9' # Como usar meu token ?

dbx = dropbox.Dropbox(TOKEN)
dbx.users_get_current_account()

class Client:
    def __init__(self, name):
        self.name = name
        self.input = 'f-ent_' + name + '.txt'
        self.output = 'f-saida_' + name + '.txt'
        self.timestamp = None

clients = []

def read(file, client): # void
    dbx.files_download_to_file('Client/' + file.name, '/Client/' + file.name)
    with open('Client/' + client.input, 'a') as f:
        print(msg)
        f.write(msg)
    dbx.files_upload(open('Client/' + client.input).read().encode(),'/Server/' + client.output, mode=WriteMode('overwrite'))

def broadcast(file, client): # void
    with open('Client/' + client.input) as f:
        txt = f.read()
        for c in clients:
            if c == client:
                continue
            with open('Client/' + c.input) as up:
                up.write(txt)
            dbx.files_upload(open('Client/' + c.input).read().encode(),'/Client/' + c.input, mode=WriteMode('overwrite'))

def serverLoop(): # void
    while True:
        # Check for file updates
        files = dbx.files_list_folder('/Client')

        for file in files.entries:
            if '-ent_' in file.name:
                continue
            filename = file.name.split('_')[1].split('.')[0]
            print(filename)
            for client in clients: # Loop pelos clientes para ver de quem eh a msg
                if filename == client.name:
                    if file.client_modified != client.timestamp:                # Verificar se a comparacao faz sentido
                        read(file, client)
                        broadcast(file, client)
                        break
            else:   # Se nao for de ngm cria um novo cliente
                clients.append(Client(filename))
                read(file, clients[-1])
                broadcast(file, clients[-1])


serverLoop()
