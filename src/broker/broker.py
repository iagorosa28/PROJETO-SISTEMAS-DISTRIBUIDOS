# broker.py
import zmq

ctx = zmq.Context()

# front-end: clientes REQ
frontend = ctx.socket(zmq.ROUTER)
frontend.bind("tcp://*:5555")

# back-end: servidores REP
backend = ctx.socket(zmq.DEALER)
backend.bind("tcp://*:5556")

# o device nativo preserva roteamento e frames corretamente
zmq.proxy(frontend, backend)

frontend.close()
backend.close()
ctx.term()
