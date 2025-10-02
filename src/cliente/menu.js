import promptSync from "prompt-sync";

const prompt = promptSync(/*{ sigint: true }*/); // para interação com o usuário
// { sigint: true } -> para fazer o esquema do ctrl+c (não quis tentar fazer, mas bom saber que tem isso)

export function menu(){
    console.log("Menu Inicial...");
    console.log("1. Cadastrar usuário");
    console.log("2. Listar usuários");
    console.log("3. Sair");
    const opcao = Number(prompt("Digite uma opção: ")); // Number -> não preciso me preocupar em fazer o trim()

    if(opcao === 1){
        return cadastrarUsuario();
    }else if(opcao === 2){
        return listarUsuarios();
    }else if(opcao === 3){
        console.log("Tchau!");
        return "sair";
    }else{
        console.log("Opção inválida!");
        return "voltar";
    }
}

function cadastrarUsuario(){
    const nomeUsuario = prompt("Digite o nome de usuario que deseja cadastrar: ").trim(); // trim() -> corta espaços no começo e no fim

    return {
        "service": "login",
        "data": {
            "user": nomeUsuario,
            "timestamp": new Date().toISOString()
        }
    }
}

function listarUsuarios(){
    return {
        "service": "users",
        "data": {
            "timestamp": new Date().toISOString()
        }
    }
}