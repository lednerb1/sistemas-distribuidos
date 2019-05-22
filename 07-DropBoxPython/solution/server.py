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

def read(file): # void
    with open('Server/' + clients[i].input, 'a') as file:
        print(msg)
        arq.write(msg)

def broadcast(): # void
    pass

def serverLoop(): # void
    while True:
        # Check for file updates
        files = dbx.files_list_folder('/Client')

        for file in files.entries:
            filename = file.name.split('_')[1].split('.')[0]
            for client in clients:
                if filename == client.name:
                    if file.client_modified != client.timestamp:
                        read(file)
                        broadcast(file)

# files = dbx.files_list_folder('')
# for i in range(len(files.entries)):
#     dbx.files_delete(files.entries[i].name)

serverLoop()
