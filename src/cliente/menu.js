import promptSync from "prompt-sync";

const prompt = promptSync(/*{ sigint: true }*/); // para interação com o usuário
// { sigint: true } -> para fazer o esquema do ctrl+c (não quis tentar fazer, mas bom saber que tem isso)

export function menu(){
    console.log("Menu Inicial...");
    console.log("1. Listar usuários");
    console.log("2. Publicar para usuário");
    console.log("3. Entrar no canal");
    console.log("4. Listar canais");
    console.log("5. Publicar no canal");
    console.log("6. Sair");
    const opcao = Number(prompt("Digite uma opção: ")); // Number -> não preciso me preocupar em fazer o trim()

    if(opcao === 1){
        return listarUsuarios();
    }else if(opcao === 2){
        return publicarParaUsuario();
    }else if(opcao === 3){
        return entrarNoCanal();
    }else if(opcao === 4){
        return listarCanais();
    }else if(opcao === 5){
        return publicarNoCanal();
    }else if(opcao === 6){
        console.log("Tchau!");
        return "sair";
    }else{
        console.log("Opção inválida!");
        return "voltar";
    }
}

export function logarUsuario(){
    const nomeUsuario = prompt("Digite o nome de usuario que deseja logar: ").trim(); // trim() -> corta espaços no começo e no fim

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

function publicarParaUsuario(userOrigin, userDestin, message){
    return {
        "service": "message",
        "data": {
            "src": userOrigin,
            "dst": userDestin,
            "message": message,
            "timestamp": new Date().toISOString()
        }
    }
}

function entrarNoCanal(){
    const nomeCanal = prompt("Digite o nome do canal que deseja iniciar: ").trim(); // trim() -> corta espaços no começo e no fim

    return {
        "service": "channel",
        "data": {
            "channel": nomeCanal,
            "timestamp": new Date().toISOString()
        }
    }
}

function listarCanais(){
    return {
        "service": "channels",
        "data": {
            "timestamp": new Date().toISOString()
        }
    }
}

function publicarNoCanal(user, channel, message){
    return {
        "service": "publish",
        "data": {
            "user": user,
            "channel": channel,
            "message": message,
            "timestamp": new Date().toISOString()
        }
    }
}