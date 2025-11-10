import zmq

context = zmq.Context()

# Recebe mensagens dos publicadores (servidores)
xsub = context.socket(zmq.XSUB)
xsub.bind("tcp://*:5557")

# Repassa mensagens aos assinantes (clientes)
xpub = context.socket(zmq.XPUB)
xpub.bind("tcp://*:5558")

# Proxy liga os dois lados
zmq.proxy(xsub, xpub)

xsub.close()
xpub.close()
context.term()