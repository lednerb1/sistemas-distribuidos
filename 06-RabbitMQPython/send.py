#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='localhost'))
channel = connection.channel()

channel.exchange_declare(exchange='plogs', exchange_type='fanout')

result = channel.queue_declare('', exclusive=True)

queueName = result.method.queue

channel.queue_bind(exchange='plogs', queue=queueName)

channel.basic_publish(exchange='plogs',
                      routing_key='',
                      body='testando')

def responseCallback(ch, method, properties, body):
    print('Received answer: {}'.format(body))

channel.basic_consume(responseCallback, queue=queueName, no_ack=True)

channel.start_consuming()
connection.close()
