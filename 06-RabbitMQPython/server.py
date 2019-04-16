#!/usr/bin/env python
import pika

connection = pika.BlockingConnection(pika.ConnectionParameters(
        host='localhost'))
channel = connection.channel()

channel.queue_declare(queue='peter')

def newMessageCallback(ch, method, properties, body):
    print(ch)
    print(method)
    print(properties)
    print(body)
    print(" [x] Received {}".format(body))

channel.basic_consume(newMessageCallback,
                      queue='peter',
                      no_ack=True)

print(' [*] Waiting for messages. To exit press CTRL+C')
channel.start_consuming()

# connection.close()
