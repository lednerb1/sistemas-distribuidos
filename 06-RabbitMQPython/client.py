#!/usr/bin/env python
import pika

user = input('Enter your name: ')

connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='localhost'))
channel = connection.channel()

channel.exchange_declare(exchange='plogs', exchange_type='fanout')
channel.exchange_declare(exchange='ulogs', exchange_type='fanout')

result = channel.queue_declare('', exclusive=True)

queueName = result.method.queue

channel.queue_bind(exchange='ulogs', queue=queueName)

i = 0
while True:
    try:
        j = str(i)
        lixo = input('Pressione ENTER para enviar o arquivo '+user+'-'+j+'.chat')
        file = open(user+'-'+j+'.chat', 'r')
        print('a')
        msg = user+'\\>>\\'+j+'\\>>\\'
        msg += file.read()
        i+=1
        channel.basic_publish(exchange='plogs',
                              routing_key='',
                              body=msg)
    except OSError:
        lixo = input('Pressione ENTER para enviar o arquivo '+user+'-'+str(i)+'.chat')
        continue

def responseCallback(ch, method, properties, body):
    body = body.decode()
    u, i, msg = body.split('\\>>\\')
    if u != user:
        print('Received answer: {}'.format(body.decode()))
        f = open(u+'-'+i+'.client'+user, 'x')
        f.write(msg)

channel.basic_consume(responseCallback, queue=queueName, no_ack=True)

channel.start_consuming()
connection.close()
