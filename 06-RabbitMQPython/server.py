#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='sdi-1',
	credentials=pika.PlainCredentials(username='sdi', password='sdi')))
channel = connection.channel()

channel.exchange_declare(exchange='plogs', exchange_type='fanout')
channel.exchange_declare(exchange='ulogs', exchange_type='fanout')

result = channel.queue_declare('',exclusive=True)

queueName = result.method.queue

channel.queue_bind(exchange='plogs', queue=queueName)

def newMessageCallback(ch, method, properties, msg):
    """
    print('--')
    print(ch)
    print('--')
    print(method)
    print('--')
    print(properties)
    print('--')
    print(" [x] Received {}".format(msg))
    print('--')
    """
    msg = msg.decode()
    nome, id, data = msg.split('\\>>\\')
    print(nome, id, data)
    file = open(nome+'-'+id+'.serv', 'w')
    file.write(data)
    channel.basic_publish(exchange='ulogs', routing_key='', body=msg)

channel.basic_consume(newMessageCallback, queue=queueName, no_ack=True)

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()
# connection.close()
