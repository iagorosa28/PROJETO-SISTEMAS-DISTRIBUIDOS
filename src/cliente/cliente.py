import zmq, json
from time import sleep
from menu import menu

context = zmq.Context()
socket = context.socket(zmq.REQ)
socket.connect("tcp://broker:5555")

i = 0
while True:
    mensagem_enviada = menu()
    socket.send(json.dumps(mensagem_enviada).encode("utf-8"))
    
    resposta_recebida_bytes = socket.recv()
    resposta_recebida = resposta_recebida_bytes.decode("utf-8")
    
    try:
        resposta = json.loads(resposta_recebida)
    except json.JSONDecodeError:
        print("Resposta não é JSON:", resposta_recebida)
        break

    print("Resposta do servidor:", resposta)

    i += 1
    sleep(0.5)
