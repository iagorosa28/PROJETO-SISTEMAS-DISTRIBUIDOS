import zmq, json
from datetime import datetime

context = zmq.Context()
socket = context.socket(zmq.REP)
socket.connect("tcp://broker:5556")

while True:
    mensagem_recebida_bytes = socket.recv()
    mensagem_recebida = mensagem_recebida_bytes.decode("utf-8")

    try:
        mensagem = json.loads(mensagem_recebida)
        service = mensagem.get("service")
        if service == "login":
            if "data" in mensagem:
                data = mensagem.get("data")
                user = data.get("user")
                resposta = {
                    "service": "login",
                    "data": {
                        "status": "sucesso",
                        "timestamp": datetime.now().isoformat(),
                        "description": f'{user} cadastrado!'
                    }
                }
                socket.send(json.dumps(resposta).encode("utf-8"))
            else:
                resposta = {
                    "service": "login",
                    "data": {
                        "status": "erro",
                        "timestamp": datetime.now().isoformat(),
                        "description": "data not found"
                    }
                }
                socket.send(json.dumps(resposta).encode("utf-8"))
        else:
            resposta = {
                "service": "unknown-service",
                "data": {
                    "status": "erro",
                    "timestamp": datetime.now().isoformat(),
                    "description": "service not found"
                }
            }     
            socket.send(json.dumps(resposta).encode("utf-8"))    
    except json.JSONDecodeError:
        resposta = {
            "service": "error",
            "data": {
                "status": "erro",
                "timestamp": datetime.now().isoformat(),
                "description": "invalid json"
            }
        }    
        socket.send(json.dumps(resposta).encode("utf-8"))
        continue