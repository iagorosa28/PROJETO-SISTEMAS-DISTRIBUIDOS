from cadastrarUsuario import cadastrarUsuario

print("Olá!")

def menu():
    while True:
        print()
        print("Menu Inicial...")
        print("1. Cadastrar usuário")
        print("2. Sair")
        opcao = int(input("Digite uma opção: "))
        if opcao == 1:
            return cadastrarUsuario()
        elif opcao == 2:
            print("Tchau!")
            break
        else:
            print("Opcão não existente!")