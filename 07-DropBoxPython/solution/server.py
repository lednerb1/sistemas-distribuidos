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

    __init__(self,name):
        self.name = name

clients = []

def getFile(): # returns file (?)
    pass

def broadcast(): # void
    pass

def newConnection(): # returns client
    pass

def dropConnection(client): # void
    pass

def serverLoop(): # void
    for client in clients:
        if client.update():
            broadcast(client)
