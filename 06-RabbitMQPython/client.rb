require 'bunny'
conn = Bunny.new(hostname: 'sdi-1', user: 'sdi', pass:'sdi')
conn.start
channel = conn.create_channel
$queue = channel.fanout('plog') # fila de escrita
$queue2 = channel.fanout('ulog')

$listener = channel.queue('', exclusive: true)

$listener.bind($queue2)

$user = gets.strip

def message
    idx=0
    puts "Message thread"
    while true
        puts "Hmm"
        puts "Message Loop #{$user}"
	puts $user + "-" + "#{idx}.chat" 
        if exist?($user + "-" + "#{idx}.chat") then
            puts "Exists"
	    faiol = File.open($user + "-" + "#{idx}.chat", mode="r")
            msg = $user + "\\>>\\#{idx}\\>>\\" + faiol.read
            channel.default_exchange.publish(msg,routing_key: queue.name)
            idx = idx+1
        else
            puts "Pressione ENTER para enviar o arquivo " + user + "-#{idx}.chat"
            gets
        end
    end
end

def listen
    puts "Listen thread"
    $listener.subscribe(block: true) do |_delivery_info, _properties, body|
        puts "listen loop?"
        nome,l,msg = body.split("\\>>\\")
        faiol = File.write(nome + "-#{l}.client" + $user, msg)
        puts "#{nome}: #{msg}"
    end
end

threads = []

threads << Thread.new { listen }
threads << Thread.new { message }

threads.each { |thr| thr.join }


