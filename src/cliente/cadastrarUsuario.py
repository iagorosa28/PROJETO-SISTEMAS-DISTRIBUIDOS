from datetime import datetime

def cadastrarUsuario():
    nome_usuario = input("Digite o nome de usuario que deseja cadastrar: ")
    
    data = {
        "service": "login",
        "data": {
            "user": nome_usuario,
            "timestamp": datetime.now().isoformat()
        }
    }

    return data